package study.wonyshop.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum OrderStatus {
  ORDER,
  ORDER_ACCEPT,
  CANCEL

}
