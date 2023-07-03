package study.wonyshop.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.item.entity.ItemType;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class UpdateRequest {

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


}


