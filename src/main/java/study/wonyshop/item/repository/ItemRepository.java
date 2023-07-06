package study.wonyshop.item.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import study.wonyshop.item.dto.ItemOrderCond;
import study.wonyshop.item.dto.ItemSearchCond;
import study.wonyshop.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>, SearchItemCustom {

  Optional<Item> findBySellerNicknameIsAndId(String sellerNickname, Long id);


  Page<Item> findAllBySellerNickname(PageRequest of, String sellerNickname);


  /**
   * 동적 검색 조건에 따른 조회 검색 조건 : name, price, createdDate,itemDetail QueryDsl 의 sort 와 page 이용 (
   * OrderSpecifier) 1. QueryDsl 설정 build.gradle 2. 사용자 정의 인터페이스 작성 searchItemCustom 3. 사용자 정의 인터페이스
   * 구현 4. itemRepository 에 사용자 정의 인터페이스 상속하기
   *
   * @param page 페이징 객체
   * @return
   */
  //Page searchItemByDynamicCond(String keyword,PageRequest page);
  Page searchItemByDynamicCond(PageRequest page , ItemSearchCond itemSearchCond);
}
