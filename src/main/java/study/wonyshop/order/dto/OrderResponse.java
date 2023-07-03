package study.wonyshop.order.dto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.order.entity.Order;
import study.wonyshop.order.entity.OrderStatus;
import study.wonyshop.orderItem.OrderItemResponse;

@Getter
public class OrderResponse extends TimeStamped {

  private final Long orderId; // 주문번호
  private final String nickname;
  private final List<OrderItemResponse> orderItems;

  private final int totalPrice; // 총 금액
  private final OrderStatus status; // 주문 상태

  public OrderResponse(Order order) {
    this.orderId = order.getId();
    this.nickname = order.getUser().getNickname();
    this.orderItems = order.getOrderItems().stream().map(o-> new OrderItemResponse(o.getItem(),o.getOrderPrice(),o.getCount())).collect(Collectors.toList());
    this.totalPrice = order.getTotalPrice();
    this.status = order.getStatus();
  }
}
