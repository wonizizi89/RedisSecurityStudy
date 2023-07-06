package study.wonyshop.order.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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
import study.wonyshop.order.repository.OrderRepository;
import study.wonyshop.orderItem.OrderItem;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final DeliveryRepository deliveryRepository;

  @Transactional
  public OrderResponse orderItem(Long userId, Long itemId, int quantity) {
    User user = userRepository.findById(userId).orElseThrow(
        ()-> new CustomException(ExceptionStatus.WRONG_USER)
    );
    Item findItem = itemRepository.findById(itemId).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_EXIST)

    );
    //배송 생성
    Delivery delivery = new Delivery(user.getAddress(), DeliveryStatus.READY);
    // 주문상품 생성
    OrderItem orderItem = OrderItem.createOrderItem(findItem, findItem.getPrice(), quantity);
    // 주문생성
    Order order = Order.createOrder(user, delivery, orderItem);
    orderRepository.save(order);
    deliveryRepository.save(delivery);
    userRepository.save(user);
    return new OrderResponse(order);

    //todo 주문하는 과정 : 결제 후 주문완료 처리 하고, 배송준비 설정

  }
  /**
   *  내 주문 조회 하기
   */
  public  OrderResponse getUserOrders( Long id){
    Order order = orderRepository.findById(id).orElseThrow(
        ()-> new IllegalArgumentException("존재하지 않는 주문 입니다.")
    );
    return new OrderResponse(order);

  }
  /**
   * 주문 취소 : 주문 취소시 바로 환불
   */
@Transactional
  public ResponseEntity cancelOrder(User user, Long orderId) {
    //주문 엔티티 조회
    Order order = orderRepository.findByIdAndUser(orderId, user).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_EXIST)
    );
    //주문 취소
    order.cancel();
    user.refundPayment(order.getTotalPrice());
    userRepository.save(user);
    return ResponseEntity.ok("주문 취소 완료 ");
  }


  // 셀러 할일
  // 2.주문 대기 리스트 확인 ->  배송준비
  // 3. 배송이 완료 되면 배송 완료 처리 하기




}
