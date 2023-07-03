package study.wonyshop.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.wonyshop.order.dto.OrderRequest;
import study.wonyshop.order.dto.OrderResponse;
import study.wonyshop.order.service.OrderService;
import study.wonyshop.security.service.UserDetailsImpl;
import study.wonyshop.user.entity.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/orders")
public class OrderController {
  private final OrderService orderService;

  /**
   * 상품 주문
   *
   * 추후 비회원도 만든다면.  비회원일때는??? 유저정보를 다 넣는 dto가 있어야 겠지 .
   */
  @PostMapping("")
  public OrderResponse orderItem(@AuthenticationPrincipal UserDetailsImpl userDetails
  ,@RequestBody OrderRequest orderRequest){
    Long itemId = orderRequest.getItemId();
    int quantity = orderRequest.getQuantity();

    return orderService.orderItem(userDetails.getUser(), itemId,quantity);
  }
  /**
   * 주문 조회
   */

  /**
   * 주문 전체 취소
   */
  @DeleteMapping("/{id}")
  public ResponseEntity cancelOrder(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long id){
    User user = userDetails.getUser();
    return orderService.cancelOrder(user,id);
  }
}
