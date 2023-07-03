package study.wonyshop.delivery;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.order.entity.Order;
//import study.wonyshop.order.entity.Order;

@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @OneToOne(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
  private Order order;

  @Embedded
  private Address address;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus deliveryStatus ;// [READY(준비), COMP(배송)]

  public Delivery(Address address, DeliveryStatus deliveryStatus) {
    this.address = address;
    this.deliveryStatus = this.deliveryStatus;
  }



  public void setOrder(Order order) {
    this.order = order;
  }

//  public void setAddress(String address) {
//    this.address = address;
//  }
//
//  public void setDeliveryStatus(DeliverStatus status) {
//    this.deliverStatus = status;
//  }
}
