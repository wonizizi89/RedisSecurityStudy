package study.wonyshop.order.service;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.wonyshop.common.exception.CustomException;
import study.wonyshop.common.exception.ExceptionStatus;
import study.wonyshop.delivery.Delivery;
import study.wonyshop.delivery.DeliveryStatus;
import study.wonyshop.delivery.respository.DeliveryRepository;
import study.wonyshop.item.entity.Item;
import study.wonyshop.item.repository.ItemRepository;
import study.wonyshop.order.dto.OrderResponse;
import study.wonyshop.order.entity.Order;
import study.wonyshop.order.entity.OrderStatus;
import study.wonyshop.order.repository.OrderRepository;
import study.wonyshop.orderItem.OrderItem;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.entity.UserRoleEnum;
import study.wonyshop.user.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final DeliveryRepository deliveryRepository;

  /**
   * customer 상품 주문 결제는 포인트 적립 후 포인트로 가능함 주문 시 결제 해야 함 즉시 셀러에게 결제 포인트가 적립됨
   */
  @Transactional
  public OrderResponse orderItem(Long userId, Long itemId, int quantity) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new CustomException(ExceptionStatus.WRONG_USER)
    );
    Item findItem = itemRepository.findById(itemId).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_EXIST)
    );
    String sellerNickname = findItem.getSellerNickname();
    User itemSeller = userRepository.findByNickname(sellerNickname).orElseThrow(
        () -> new CustomException(ExceptionStatus.WRONG_SELLER)
    );

    //배송 생성
    Delivery delivery = new Delivery(user.getAddress(), DeliveryStatus.NONE);
    // 주문상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), quantity);
    // 주문생성
    Order order = Order.createOrder(user, delivery, sellerNickname, orderItem);
    // 주문 시 포인트로 결제  , 즉시 셀러에게 포인트 적립 됨
    int price = findItem.getPrice();
    int totalPrice = price * quantity;
    user.payForOrder(totalPrice);
    itemSeller.receivePayment(totalPrice);

    orderRepository.save(order);
    deliveryRepository.save(delivery);
    userRepository.save(user);
    userRepository.save(itemSeller);
    return new OrderResponse(order);

    //todo 주문하는 과정 : 결제 후 주문완료 처리 하고, 배송준비 설정

  }

  /**
   * 내 주문 조회 하기
   */
  public OrderResponse getUserOrders(Long id) {
    Order order = orderRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("존재하지 않는 주문 입니다.")
    );
    return new OrderResponse(order);

  }

  /**
   * 주문 취소 신청
   */
  @Transactional
  public ResponseEntity cancelOrder(Long userId, Long orderId) {
    //주문 엔티티 조회
    Order order = orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_EXIST)
    );
    User findUser = userRepository.findById(userId).orElseThrow(
        () -> new CustomException(ExceptionStatus.WRONG_USER)
    );
    String sellerNickname = order.getSellerNickname();
    User seller = userRepository.findByNickname(sellerNickname).orElseThrow(
        ()-> new CustomException(ExceptionStatus.WRONG_SELLER)
    );

    //주문 취소 하기
    order.cancel();
    //유저게에 환불
    int totalPrice = order.getTotalPrice();
    seller.refundPayment(totalPrice);
    findUser.receivePayment(totalPrice);

    orderRepository.delete(order);
    userRepository.save(findUser);
    userRepository.save(seller);
    return ResponseEntity.ok("주문 취소 신청 완료 ");
  }

  /**
   * 셀러 주문 대기자 리스트 확인
   */
  public Page<OrderResponse> getMyWaitingList(String sellerNickname, int page, int size,
      Direction direction, String properties) {
    Page<Order> ordersPage = orderRepository.findAllBySellerNickname(
        PageRequest.of(page - 1, size, direction, properties), sellerNickname);
    Page<OrderResponse> pageResponse = ordersPage.map(OrderResponse::new);
    return pageResponse;
  }

  /**
   * 주문처리과정 주문 디비 조회 가져와 (셀러닉네임) 각 주문에 있는 주문 상태를 order-> order_ accept 배송 상태 변경하기
   * DELEVERYSTATUS.READY
   *
   * @param sellerNickname
   * @param orderId
   * @return
   */
  @Transactional
  public ResponseEntity orderCompletionProcessing(String sellerNickname, Long orderId) {
    Order findOrder = findBySellerNicknameAndId(sellerNickname, orderId);
    findOrder.setStatus(OrderStatus.ORDER_ACCEPT);
    //배송 상태 준비 중으로 변경하기
    Delivery delivery = findOrder.getDelivery();
    delivery.changeStatus(DeliveryStatus.READY);

    deliveryRepository.save(delivery);
    orderRepository.save(findOrder);
    return ResponseEntity.ok("주문 수락 완료");

  }



  private Order findBySellerNicknameAndId(String sellerNickname, Long orderId) {
    return orderRepository.findBySellerNicknameAndId(
        sellerNickname, orderId).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_EXIST)
    );
  }

  @Transactional
  public ResponseEntity deliveryProcessing(String sellerNickname, Long orderId) {
    Order findOrder = findBySellerNicknameAndId(sellerNickname, orderId);
    Delivery delivery = findOrder.getDelivery();
    delivery.changeStatus(DeliveryStatus.COMP);
    deliveryRepository.save(delivery);
    orderRepository.delete(findOrder);
    //orderRepository.save(findOrder);
    return ResponseEntity.ok("배송 완료 처리" );
  }

  /**
   *  3개월 경과한 주문완료 및 취소 건에 대해 소프트 삭제
   *
   * @param adminId
   * @param role
   * @return
   */
 // public ResponseEntity softDeleteOrder(Long adminId, UserRoleEnum role) {
//    User admin = userRepository.findById(adminId).orElseThrow(
//        ()-> new CustomException(ExceptionStatus.WRONG_ADMIN)
//    );
    // createdDate  로부터 3개월 지난 건에 대해 소프트 연산

  //}
}







