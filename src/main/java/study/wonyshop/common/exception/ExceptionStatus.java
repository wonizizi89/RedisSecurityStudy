package study.wonyshop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionStatus {
  DUPLICATED_USERNAME(409, "이미 사용중인 아이디입니다."),
  DUPLICATED_NICKNAME(408, "이미 사용중인 닉네임입니다."),
  DUPLICATED_EMAIL(407, "이미 사용중인 이메일입니다."),
  DUPLICATED_PHONENUMBER(406, "이미 사용중인 휴대폰번호입니다."),
  SIGNUP_WRONG_USERNAME(409, "최소 4자 이상, 10자 이하이며, 영문과 숫자만 입력하세요."),
  WRONG_EMAIL(404, "이메일을 잘못 입력 하였거나 등록되지 않은 이메일 입니다."),

  WRONG_PASSWORD(400, "잘못된 비밀번호 입니다."),
  AUTHENTICATION(500, "인증 실패"),
  WRONG_PROFILE(404, "프로필이 존재하지 않습니다.");

  private final int StatusCode;
  private final String message;
}
