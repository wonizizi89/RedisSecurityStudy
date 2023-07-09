package study.wonyshop.user.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.delivery.Address;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.entity.UserRoleEnum;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class SignUpRequest {

  // 회원가입 비밀번호 재확인 추가
// 정규식이 틀렸을때 발생하는 예외 MethodArgumentNotValidException
// @NotBlank = null 과 "" 과 " " 모두 비허용, @Notnull = "" 이나 " " 은 허용, @NotEmpty = null 과 "" 은 불가, " " 은 허용
  @Email
  private final String email;
  @Pattern(regexp = "[a-zA-Z0-9]{4,8}$", message = "닉네임은 최소 4자 이상, 8자 이하이며, 영문과 숫자만 입력하세요.")
  private final String nickName;

  @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])^[a-zA-Z0-9~!@#$%^&*()+|=]{8,15}$", message = "비밀번호는 최소 8자 이상, 15자 이하이며, 영문과 숫자, 특수문자만 입력하세요.")
  private final String password;

  @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[0-9])^[a-zA-Z0-9~!@#$%^&*()+|=]{8,15}$", message = "비밀번호를 확인해주세요.")
  private final String password2;

  @Pattern(regexp = "(?=.*[0-9])^[0-9]{11}$", message = "-을 제외한 10자리 번호를 입력해주세요")
  private final String phoneNumber;

  @NotBlank
  private final String city;

  @NotBlank
  private final String street;

  @NotBlank
  private final String zipcode;


//  private boolean admin = false;
//
//  private String adminToken = "";

  @Builder
  public SignUpRequest(String email, String nickName, String password, String password2,
      String phoneNumber, String city, String street, String zipcode) {
    this.email = email;
    this.nickName = nickName;
    this.password = password;
    this.password2 = password2;
    this.phoneNumber = phoneNumber;
    this.city = city;
    this.street = street;
    this.zipcode = zipcode;
  }

  /**
   * DTO -> Entity
   * @param role
   * @param encodedPassword
   * @return
   */
  public User toEntity(UserRoleEnum role, String encodedPassword) {
    return User.builder()
        .nickname(nickName)
        .email(email)
        .phoneNumber(phoneNumber)
        .password(encodedPassword)
        .address(new Address(city,street,zipcode))
        .role(role)
        .build();
  }
}
