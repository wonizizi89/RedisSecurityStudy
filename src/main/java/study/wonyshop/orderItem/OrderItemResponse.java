package study.wonyshop.orderItem;

import lombok.Getter;
import study.wonyshop.item.dto.ItemResponse;

@Getter
public class OrderItemResponse {

  private final ItemResponse itemName; // 한 상품을 여러번 주문 할 수 있으니 m:1

  private final int orderPrice; // 주문가격
  private final int count; //주문수량

  public OrderItemResponse(String itemName, int orderPrice, int count) {
    this.itemName = new ItemResponse(itemName);
    this.orderPrice = orderPrice;
    this.count = count;
  }
}
