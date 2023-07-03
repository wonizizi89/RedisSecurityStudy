package study.wonyshop.item.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.item.dto.UpdateRequest;


@Entity
@Getter
@DiscriminatorValue("A")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item {

  private String artist;
  private String etc;
@Builder
  public Album(String name, int price, int stockQuantity, String itemDetail, String image,
      String sellerNickname, String artist, String etc) {
    super(name, price, stockQuantity, itemDetail, image, sellerNickname);
    this.artist = artist;
    this.etc = etc;
  }

  public void updateAlbum(String artist, String etc) {
    this.artist = artist;
    this.etc = etc;
  }

}
