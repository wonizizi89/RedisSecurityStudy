package study.wonyshop.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.delivery.Delivery;
import study.wonyshop.delivery.DeliveryStatus;
import study.wonyshop.item.entity.Item;
import study.wonyshop.orderItem.OrderItem;
import study.wonyshop.user.entity.User;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE orders SET deleted = true WHERE order_id = ? ") // 딜리트 쿼리 발생시 딜리트 대신 수행 sql
public class Order extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인
  @JoinColumn(name = "user_id")
  private User user; // 주문자

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  @Column(nullable = false)
  private String sellerNickname;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery; //배송정보

  @Enumerated(EnumType.STRING)
  private OrderStatus status; //ORDER, CANCEL

  private Boolean deleted = false ;// 데이터 삭제 여부
  private LocalDateTime orderDate;// 주문 날짜


  @Builder
  public Order(User user, Delivery delivery, OrderStatus status, LocalDateTime orderDate, Boolean deleted) {
    this.user = user;
    this.delivery = delivery;
    this.status = status;
    this.orderDate = orderDate;
    this.deleted = deleted;

  }

  //--- 양방향 연관관계 편의 메서드 ------// 편의메서드는 컨트롤 하는 쪽에 만들어주면 됨
  // 다 쪽이  연관관계 주인으로 값 의 변경사항은 주인쪽에서 함
  // 다 쪽 : 일의 값은 set 으로 저장
  // 일 쪽 : 리스트(다) 를 조회하여 add


  // order : user  = m :1
  public void setUser(User user) {
    this.user = user;
    user.getOrders().add(this);
  }

  //order : orderItem  = 1: m
  public void addOrderItem(OrderItem orderItem) {
    this.orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  // order : delivery  = 1: 1
  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;

  }

  public void setSellerNickname(String sellerNickname) {
    this.sellerNickname = sellerNickname;
  }

  //===== 생성 메서드 =====//
  //OrderItem... orderItems에서 ...은 가변 인자를 선언하는 부분입니다. 이는 OrderItem 타입의 인자를 0개 이상 받을 수 있다는 의미입니다.
  //Order order = createOrder(user, delivery, item1, item2);
  public static Order createOrder(User user, Delivery delivery, String sellerNickname,
      OrderItem... orderItems) {
    Order order = new Order();
    order.setUser(user);
    order.setDelivery(delivery);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setSellerNickname(sellerNickname);
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }
  //---  비지니스 로직 -------//

  /**
   * 주문취소
   */

  public void cancel() {
    if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException(" 이미 배송이 완료된 상품은 취소가 불가능 합니다. "); //Non-cancellable product
    }
    this.setStatus(OrderStatus.CANCEL);
    // 주문 상품을 다 취소 시켜야함
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }

  //=== 조회 로직 =====//

  /**
   * 전체 주문 가격 조회
   */
  public int getTotalPrice() {
    int totalPrice = 0;
    for (OrderItem orderItem : orderItems) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }

}

