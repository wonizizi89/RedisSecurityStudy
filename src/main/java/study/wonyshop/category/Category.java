//package study.wonyshop.category;
//
//import java.util.ArrayList;
//import java.util.List;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.JoinTable;
//import javax.persistence.ManyToMany;
//import javax.persistence.ManyToOne;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import study.wonyshop.item.entity.Item;
//
///**
// *  실무에서는 @ManyToMany 를 사용하지 말자
// *    > @ManyToMany 는 편리한 것 같지만, 중간 테이블( CATEGORY_ITEM )에 컬럼을 추가할 수 없고,
// *    세밀하게 쿼 리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있다. 중간 엔티티
// *    ( CategoryItem 를 만들고 @ManyToOne , @OneToMany 로 매핑해서 사용하자.
// *    정리하면 다대다 매핑을 일대다, 다대일 매핑으로 풀어 내서 사용하자.
// */
//@Entity
//@Table(name = "category")
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Category {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "id", nullable = false)
//  private Long id;
//  @ManyToMany
//  @JoinTable(name = "category_item"
//      , joinColumns = @JoinColumn(name = "category_id")
//      , inverseJoinColumns = @JoinColumn(name = "item_id")) // 중간 테이블 생성
//  private List<Item> items = new ArrayList<>();
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "parent_id")
//  @Setter
//  private Category parent;
//
//  @OneToMany(mappedBy = "parent")
//  private List<Category> child = new ArrayList<>();
//
//  //== 연관관계 메서드 ==//
//  public void addChildCategory(Category child){
//    this.child.add(child);
//    child.setParent(this);
//  }
//
//
//}
