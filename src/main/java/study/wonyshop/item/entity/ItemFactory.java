package study.wonyshop.item.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.wonyshop.item.dto.ItemRequest;
import study.wonyshop.item.dto.UpdateRequest;
import study.wonyshop.item.repository.ItemRepository;

@Component
public class ItemFactory {


  /**
   * 팩토리 패턴을 이용
   * 타입에 따라 아이템 객체 생성
   * @param sellerNickname
   * @param request
   * @return
   */
  public Item createItem(String sellerNickname, ItemRequest request) {
    ItemType type = request.getType();
    if (type.equals(ItemType.ALBUM)) {
      Album album = Album.builder()
          .name(request.getName())
          .price(request.getPrice())
          .stockQuantity(request.getStockQuantity())
          .itemDetail(request.getItemDetail())
          .image(request.getImage())
          .sellerNickname(sellerNickname)
          .artist(request.getArtist())
          .etc(request.getEtc())
          .build();
      return album;
    } else if (type.equals(ItemType.BOOK)) {
      Book book = Book.builder()
          .name(request.getName())
          .price(request.getPrice())
          .stockQuantity(request.getStockQuantity())
          .itemDetail(request.getItemDetail())
          .image(request.getImage())
          .sellerNickname(sellerNickname)
          .author(request.getAuthor())
          .isbn(request.getIsbn())
          .build();
      return book;
    } else if (type.equals(ItemType.MOVIE)) {
      Movie movie = Movie.builder()
          .name(request.getName())
          .price(request.getPrice())
          .stockQuantity(request.getStockQuantity())
          .itemDetail(request.getItemDetail())
          .image(request.getImage())
          .sellerNickname(sellerNickname)
          .actor(request.getActor())
          .director(request.getDirector())
          .build();
      return movie;


    } else {
      throw new IllegalStateException("유효하지 않은 상품 유형입니다: " + request.getType());
    }

  }

  /**팩토리 패턴 이용
   * 타입에 따라 아이템 정보 수정
   * @param request
   * @param item
   */
  public void updateItem(UpdateRequest request,Item item){
  ItemType type = request.getType();
  if(type.equals(ItemType.ALBUM)){
    Album album = (Album) item;
    album.updateAlbum(request.getArtist(), request.getEtc());

  } else if (type.equals(ItemType.BOOK)) {
    Book book = (Book) item;
    book.updateBook(request.getAuthor(),request.getIsbn());
  } else{
    Movie movie =(Movie) item;
    movie.updateMovie(request.getDirector(),request.getActor());
  }

}

}
