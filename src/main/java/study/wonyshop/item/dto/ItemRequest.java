package study.wonyshop.item.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import study.wonyshop.item.entity.Item;
import study.wonyshop.item.entity.ItemType;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class ItemRequest {
  private final ItemType type;

  private final String name; //상품명
  private final int price; // 가격
  private final int stockQuantity; // 재고 수량

//  private final List<Category> categories = new ArrayList<>();
  private final String image;
  private final String itemDetail;


  //앨범
  private String artist;
  private String etc;

  //도서
  private String author;
  private String isbn;

  //음반
  private String director;
  private String actor;
  @Builder
  public ItemRequest(ItemType type, String name, int price, int stockQuantity, String image,
      String itemDetail, Long sellerId) {
    this.type = type;
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.image = image;
    this.itemDetail = itemDetail;
  }


}
