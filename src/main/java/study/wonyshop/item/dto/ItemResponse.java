package study.wonyshop.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import study.wonyshop.item.entity.Item;

@Getter
public class ItemResponse {

  private final Long id;
  private final String name; //상품명
  private final int price; // 가격
  private final int stockQuantity; // 재고 수량

  //  private final List<Category> categories = new ArrayList<>();
  private final String image;
  private final String itemDetail;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private final LocalDateTime createdDate;

  public ItemResponse(Item item) {
    this.id =item.getId();
    this.name = item.getName();
    this.price = item.getPrice();
    this.stockQuantity = item.getStockQuantity();
    this.image = item.getImage();
    this.itemDetail = item.getItemDetail();
    this.createdDate = item.getCreatedDate();
  }
}
