package study.wonyshop.user.entity;


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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.delivery.Address;
import study.wonyshop.order.entity.Order;
//import study.wonyshop.order.entity.Order;

@Entity
@Table(name = "USERS") //테이블 user 예약어 있어서 사용할 수 없음)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false)
  private Long id;
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String nickname;
  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Address address;
  @Setter
  private String profileImage;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;
  @Column(nullable = false, unique = true)
  private String phoneNumber;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//,fetch = FetchType.EAGER
  private List<Order> orders = new ArrayList<>();
  //todo  즉시로딩 필요함 -> 추후 변경하기 Lazy 로

  /**
   * 결제 수단은 포인트로 가능 ! 하다는 가정
   */
  @Column(nullable = false)
  private int point = 1000;  // 기본 가입 포인트


  private Boolean inUser; // 추후 휴면계정 관리 할때 사용 하기 위함

  @Builder
  public User(String email, String nickname, String password, Address address, String profileImage,
      UserRoleEnum role, String phoneNumber) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.address = address;
    this.profileImage = profileImage;
    this.role = role;
    this.phoneNumber = phoneNumber;
  }

  // admin , seller 용
  @Builder
  public User(String email, String nickname, String password, Address address, String profileImage,
      UserRoleEnum role, String phoneNumber, int point) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.address = address;
    this.profileImage = profileImage;
    this.role = role;
    this.phoneNumber = phoneNumber;
    this.point = point;
  }

  /**
   * 포인트 충전 하기
   */
  public void earnPoint(int point){
    this.point += point;
  }


  /**
   * 소비자가 -> 셀러 에게 지불
   *
   * @param totalPrice
   */
  public void payForOrder(int totalPrice) {
    int restPoint = this.point - totalPrice;
    if (restPoint < 0) {
      throw new IllegalArgumentException("포인트가 부족합니다. 포인트 충전 후 다시 이용해 주세요.");
    }
    this.point = restPoint;
  }

  /**
   * 셀러가 판매해서 받은 돈
   *
   * @param totalPrice
   */
  public void receivePayment(int totalPrice) {
    this.point += totalPrice;
  }

  /**
   * 취소 시 환불
   *
   * @param refundPayment
   */
  public void refundPayment(int refundPayment) {
    this.point -= refundPayment;
  }

}