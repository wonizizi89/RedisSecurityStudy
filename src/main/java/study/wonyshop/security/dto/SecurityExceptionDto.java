package study.wonyshop.security.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class SecurityExceptionDto {

  private int statusCode;
  private String msg;

  public SecurityExceptionDto(int statusCode, String msg) {
    this.statusCode = statusCode;
    this.msg = msg;
  }
}