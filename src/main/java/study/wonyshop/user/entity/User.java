package study.wonyshop.user.entity;


import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import org.springframework.beans.factory.annotation.Value;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.order.entity.Order;

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
  private String address;
  @Setter
  private String profileImage;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;
  @Column(nullable = false, unique = true)
  private String phoneNumber;
  @OneToMany(mappedBy = "user")
  private List<Order> orders = new ArrayList<>();

  private Boolean inUser ; // 추후 휴면계정 관리 할때 사용 하기 위함

  @Builder
  public User(String email, String nickname, String password, String address, String profileImage,
      UserRoleEnum role, String phoneNumber) {
    this.email = email;
    this.nickname = nickname;
    this.password = password;
    this.address = address;
    this.profileImage = profileImage;
    this.role = role;
    this.phoneNumber = phoneNumber;
  }
}
