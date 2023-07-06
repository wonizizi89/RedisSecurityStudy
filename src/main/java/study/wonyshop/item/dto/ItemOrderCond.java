package study.wonyshop.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

/**
 * 검색 조건 Dto
 */
@Getter
@NoArgsConstructor
public class ItemOrderCond {

  private String direction;
  private String properties;

  public void setDirection(String direction) {
    this.direction = StringUtils.hasText(direction)? direction : "DESC";
  }

  public void setProperties(String properties) {
    this.properties = StringUtils.hasText(properties)? properties : "createdDate";
  }
}
