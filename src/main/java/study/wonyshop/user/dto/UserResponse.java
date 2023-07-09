package study.wonyshop.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.delivery.Address;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.entity.UserRoleEnum;

@Getter
@NoArgsConstructor(force = true)
public class UserResponse {

  private final String email;
  private final String nickname;
  private final String phoneNumber;
  private final UserRoleEnum role;
  private final String profileImage;

  private final Address address;
  private final int point ;

  /**
   * 유저 생성자를 private로 외부에서 생성 할수 없도록 함
   *
   * @param user
   */
  private UserResponse(User user) {
    this.email = user.getEmail();
    this.nickname = user.getNickname();
    this.phoneNumber = user.getPhoneNumber();
    this.role = user.getRole();
    this.profileImage = user.getProfileImage();
    this.address = user.getAddress();
    this.point = user.getPoint();
  }

  /**
   * 유저 생성자를 private로 외부에서 생성 할수 없도록 함으로 써 of 메서드를 통해 유저 객체를 DTO에 담아 반환해줍니다.
   *
   * @param user
   * @return
   */
  public static UserResponse of(User user) {
    return new UserResponse(user);
  }


}
