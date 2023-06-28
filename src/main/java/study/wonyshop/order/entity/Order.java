package study.wonyshop.order.entity;

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
import org.hibernate.annotations.ManyToAny;
import study.wonyshop.user.entity.User;

@Entity
@Table(name = "ORDERS")
@Getter
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) //연관관계 주인
  @JoinColumn(name = "user_id")
  private User user;


  public Order(User user) {
    this.user = user;
  }


}
