package study.wonyshop.orderItem;

import lombok.Getter;
import study.wonyshop.item.entity.Item;

@Getter
public class OrderItemResponse {

  private final Item item; // 한 상품을 여러번 주문 할 수 있으니 m:1

  private final int orderPrice; // 주문가격
  private final int count; //주문수량

  public OrderItemResponse(Item item, int orderPrice, int count) {
    this.item = item;
    this.orderPrice = orderPrice;
    this.count = count;
  }
}
