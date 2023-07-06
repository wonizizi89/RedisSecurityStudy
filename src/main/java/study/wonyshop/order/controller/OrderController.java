package study.wonyshop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.wonyshop.order.dto.OrderRequest;
import study.wonyshop.order.dto.OrderResponse;
import study.wonyshop.order.service.OrderService;
import study.wonyshop.security.service.UserDetailsImpl;
import study.wonyshop.user.entity.User;

/**
 * 결제는 포인트 적립 후 포인트로 가능함 주문 시 결제 해야 함
 * <p>
 * 셀러가 주문 리스트 확인 오더 수락 과정 오더 취소 과정 필요함
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {

  private final OrderService orderService;

  /**
   * 상품 주문
   * <p>
   * 추후 비회원도 만든다면.  비회원일때는??? 유저정보를 다 넣는 dto가 있어야 겠지 .
   */
  @PostMapping("")
  public OrderResponse orderItem(@AuthenticationPrincipal UserDetailsImpl userDetails
      , @RequestBody OrderRequest orderRequest) {
    Long itemId = orderRequest.getItemId();
    int quantity = orderRequest.getQuantity();

    return orderService.orderItem(userDetails.getUser().getId(), itemId, quantity);
  }

  /**
   * 주문 조회
   */
  @GetMapping("/{id}/users")
  public OrderResponse getOrderList(@AuthenticationPrincipal UserDetailsImpl userDetails
      , @PathVariable Long id) {
    return orderService.getUserOrders(id);
  }

  /**
   * 주문 전체 취소
   */
  @DeleteMapping("/{id}/users")
  public ResponseEntity cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long id) {
    User user = userDetails.getUser();
    return orderService.cancelOrder(user, id);
  }


  /**
   * seller 주문 대기 고객 리스트 확인하기 본인이 판매하는 상품을 주문한사람 체크 - 셀러 아이디
   */
  @GetMapping("/sellers/waiting-list")
  public Page<OrderResponse> getMyWaitingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "5") int size,
      @RequestParam(value = "direction", required = false, defaultValue = "DESC") Direction direction,
      @RequestParam(value = "properties", required = false, defaultValue = "createdDate") String properties){
      return orderService.getMyWaitingList(userDetails.getUser().getNickname(),page,size,direction,properties);
    }

  }
