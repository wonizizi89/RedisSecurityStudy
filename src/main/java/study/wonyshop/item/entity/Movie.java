package study.wonyshop.item.entity;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.item.dto.UpdateRequest;

@Entity
@DiscriminatorValue("M")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends Item {
  private String director;
  private String actor;
  @Builder

  public Movie(String name, int price, int stockQuantity, String itemDetail, String image,
      String sellerNickname,  String director,
      String actor) {
    super(name, price, stockQuantity, itemDetail, image, sellerNickname);
    this.director = director;
    this.actor = actor;
  }
public void updateMovie(String director,String actor){
    this.director = director;
    this.actor = actor;
}
}
