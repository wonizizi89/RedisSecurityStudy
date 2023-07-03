package study.wonyshop.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor(force = true , access = AccessLevel.PROTECTED)
public class OrderRequest {

  private final Long itemId;
  private final int quantity;

  public OrderRequest(Long itemId, int quantity) {
    this.itemId = itemId;
    this.quantity = quantity;
  }
}
