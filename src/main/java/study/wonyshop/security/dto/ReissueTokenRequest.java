package study.wonyshop.security.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class ReissueTokenRequest {
  private String refreshToken;

  public ReissueTokenRequest(String refreshToken) {
    this.refreshToken = refreshToken;
  }
}
