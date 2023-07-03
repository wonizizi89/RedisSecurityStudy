package study.wonyshop.item.entity;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.wonyshop.common.TimeStamped;
import study.wonyshop.common.exception.NotEnoughStockException;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@Inheritance(strategy = SINGLE_TABLE)// 상속 전략을 싱글테이블로 지정
@DiscriminatorColumn(name = "dtype") // 싱글테이블에서 상품을 구분하기위한 키 타입 컬럼 추가
public abstract class Item extends TimeStamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  private String name; //상품명
  private int price; // 가격
  private int stockQuantity; // 재고 수량

  private String itemDetail; // 상품정보
  private String image;  // 상품이미지
  private String sellerNickname;  // 판매자


  public Item(String name, int price, int stockQuantity, String itemDetail, String image,
      String sellerNickname) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.itemDetail = itemDetail;
    this.image = image;
    this.sellerNickname = sellerNickname;


  }

//  @ManyToMany(mappedBy = "items")
//  private List<Category> categories = new ArrayList<>();

  //== 비지니스 로직 === //
  public void addStock(int quantity) {
    this.stockQuantity += quantity;
  }

  public void removeStock(int quantity) {
    int restStock = stockQuantity - quantity;
    if (restStock < 0) {
      throw new NotEnoughStockException("need more stock");
    }
    this.stockQuantity = restStock;
  }


  public void update(String name, int price, int stockQuantity, String image,
      String itemDetail) {
    this.name = name;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.image = image;
    this.itemDetail = itemDetail;
  }
}
