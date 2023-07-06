package study.wonyshop.item.repository;

import static study.wonyshop.item.entity.QItem.item;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.wonyshop.item.dto.ItemSearchCond;
import study.wonyshop.item.entity.Item;

/**
 * 규칙 : 이렇게 해야 spring data Jpa 가 인식함 itemRepository or 사용자정의인터페이스 명 + Impl
 */
public class SearchItemCustomImpl implements SearchItemCustom {

  private final JPAQueryFactory jpaQueryFactory;

  public SearchItemCustomImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Page searchItemByDynamicCond(PageRequest page,
      ItemSearchCond itemSearchCond) {

    List<Item> content = jpaQueryFactory
        .selectFrom(item) //QItem.item static import
        .where(nameContains(itemSearchCond.getName()),
            priceGoe(itemSearchCond.getPriceGoe()),
            priceLoe(itemSearchCond.getPriceLoe()))
        .offset(page.getOffset())
        .limit(page.getPageSize())
        .orderBy(itemSort(page))
        .fetch();

    JPAQuery<Item> countQuery = jpaQueryFactory
        .selectFrom(item) //QItem.item static import
        .offset(page.getOffset())
        .limit(page.getPageSize())
        .orderBy(itemSort(page));
// CountQuery 최적화 //PageableExecutionUtils.getPage()로 최적화
    return PageableExecutionUtils.getPage(content, page, countQuery::fetchCount);
  }
  private BooleanExpression nameContains(String name) {
    return StringUtils.hasText(name) ? item.name.contains(name) : null;
  }


  private BooleanExpression priceGoe(Integer priceGoe) {
    return priceGoe == null ? null : item.price.goe(priceGoe);
  }

  private BooleanExpression priceLoe(Integer priceLoe) {
    return priceLoe == null ? null : item.price.loe(priceLoe);

  }
  /*** 정렬조건
   *  OrderSpecifier 를 쿼리로 반환하여 정렬조건을 맞춰준다
   *  리스트 정렬
   */
  private OrderSpecifier<?> itemSort(PageRequest page) {
    //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
    if (!page.getSort().isEmpty()) {
      //정렬값이 들어 있으면 for 를 사용하여 Sort.Order 객체 리스트로 정렬정보를 가져온다.
      for (Sort.Order order : page.getSort()) {
        // 서비스에서 넣어준 DESC or ASC 를 가져온다.
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
        switch (order.getProperty()) {
          case "name":
            return new OrderSpecifier<>(direction, item.name);
          case "price":
            return new OrderSpecifier<>(direction, item.price);
          case "createdDate":
            return new OrderSpecifier<>(direction, item.createdDate);
        }
      }
    }
    return null;
  }



}
