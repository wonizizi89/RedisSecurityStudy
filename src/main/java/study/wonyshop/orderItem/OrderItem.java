package study.wonyshop.orderItem;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.item.entity.Item;
import study.wonyshop.order.entity.Order;

/**
 * 주문 상품
 */
@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id", nullable = false)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item; // 한 상품을 여러번 주문 할 수 있으니 m:1

  private int orderPrice; // 주문가격
  private int count; //주문수량


  public void setItem(Item item) {
    this.item = item;
  }

  public void setOrderPrice(int orderPrice) {
    this.orderPrice = orderPrice;
  }

  public void setCount(int count) {
    this.count = count;
  }

  //==생성 메서드==//
  //static 하는 이유 인스턴스 생성없이 메서드 호출하기 위함
  public static OrderItem createOrderItem(Item findItem, int price, int quantity) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(findItem);
    orderItem.setOrderPrice(price);
    orderItem.setCount(quantity);
    findItem.removeStock(quantity);
    return orderItem;
  }

  public void setOrder(Order order) {
    this.order  = order;
  }

////==조회 로직==//
/** 주문상품 전체 가격 조회 -개당 상품 */

  public int getTotalPrice() {
    return getOrderPrice()*getCount();
  }

  //==비즈니스 로직==//
  /** 주문 취소 */
  public void cancel() {
    getItem().addStock(count);
  }

}
/**
 * 양방향 편의 메서드는 무조건 필요하지 않다. 필요에 따라 편의 메서드 대신 생성자를 통해 값을 초기화 하기도 한다.
 * 일대다 단방향 관계는 지양하자. -> 사용할거면 다대일 함께 양방향 지향
 */

