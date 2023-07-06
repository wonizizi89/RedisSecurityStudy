package study.wonyshop.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true , access = AccessLevel.PROTECTED)
public class EarnPointRequest {

  private final int point ;

  public EarnPointRequest(int point) {
    this.point = point;
  }
}
