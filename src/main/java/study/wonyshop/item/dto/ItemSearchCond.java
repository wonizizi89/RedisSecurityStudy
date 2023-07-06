package study.wonyshop.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
public class ItemSearchCond {
  // 아이템명 , 가격 ( 이상 , 이하 )
  private String name;
  private Integer priceGoe;

  private Integer priceLoe;

  public void setName(String name) {
    this.name = StringUtils.hasText(name) ? name : null;

  }

  public void setPriceGoe(String priceGoe) {
    this.priceGoe = StringUtils.hasText(priceGoe) ? Integer.parseInt(priceGoe): null;
  }

  public void setPriceLoe(String priceLoe) {
    this.priceLoe = StringUtils.hasText(priceLoe) ? Integer.parseInt(priceLoe): null;
  }
}
