package study.wonyshop.item.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.item.dto.UpdateRequest;


@Entity
@DiscriminatorValue("B") // 싱글테이블에서 상품을 구분하기 위해 컬럼과 밸류를 추가함
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {

  private String author;
  private String isbn;

  @Builder
  public Book(String name, int price, int stockQuantity, String itemDetail, String image,
      String sellerNickname, String author, String isbn) {
    super(name, price, stockQuantity, itemDetail, image, sellerNickname);
    this.author = author;
    this.isbn = isbn;
  }

  public void updateBook(String author, String isbn){
    this.author = author;
    this.isbn = isbn;
  }

}

