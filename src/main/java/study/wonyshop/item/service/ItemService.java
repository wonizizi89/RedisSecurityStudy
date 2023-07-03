package study.wonyshop.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.wonyshop.common.exception.CustomException;
import study.wonyshop.common.exception.ExceptionStatus;
import study.wonyshop.item.dto.ItemRequest;
import study.wonyshop.item.dto.ItemResponse;
import study.wonyshop.item.dto.UpdateRequest;
import study.wonyshop.item.entity.Item;
import study.wonyshop.item.entity.ItemFactory;
import study.wonyshop.item.repository.ItemRepository;
import study.wonyshop.user.entity.UserRoleEnum;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final ItemFactory itemFactory;

  /**
   * 상품 등록
   */
  @Transactional
  public ItemResponse resisterItem(String sellerNickname, ItemRequest itemRequest) {
    Item item = itemFactory.createItem(sellerNickname, itemRequest);
    itemRepository.save(item);
    ItemResponse itemDto = new ItemResponse(item);
    return itemDto;
  }

  /**
   * 판매자가 등록한 상품 리스트 조회
   *
   * @param page
   * @param size
   * @param direction
   * @param properties
   * @param sellerNickname
   * @return
   */
  public Page<ItemResponse> getRegisteredItems(int page, int size, Direction direction,
      String properties, String sellerNickname) {
    Page<Item> itemList = itemRepository.findAllBySellerNickname(
        PageRequest.of(page - 1, size, direction, properties), sellerNickname);
    Page<ItemResponse> itemPageList = itemList.map(o -> new ItemResponse(o));
    return itemPageList;
  }

  /**
   * 상품 삭제 : 판매자 및 관리자만 삭제 가능
   *
   * @param sellerNickname
   * @param role
   * @param id
   * @return
   */
  public ResponseEntity deleteItem(String sellerNickname, UserRoleEnum role, Long id) {
    Item findItem = findById(id);
    isSellerOrAdmin(sellerNickname, role, findItem);
    itemRepository.delete(findItem);
    return ResponseEntity.ok("상품 삭제 완료");
  }


  /**
   * 상품조회 메서드
   */

  public Item findById(Long id) {
    Item findItem = itemRepository.findById(id).orElseThrow(
        () -> new CustomException(ExceptionStatus.NOT_EXIST)
    );
    return findItem;
  }

  @Transactional
  public ItemResponse updateItem(String sellerNickname, UserRoleEnum role, Long id,
      UpdateRequest updateRequest) {
    Item findItem = findById(id);
    isSellerOrAdmin(sellerNickname, role, findItem);
    String name = updateRequest.getName();
    int price = updateRequest.getPrice();
    int stockQuantity = updateRequest.getStockQuantity();
    String image = updateRequest.getItemDetail();
    String itemDetail = updateRequest.getItemDetail();
    findItem.update(name, price, stockQuantity, image, itemDetail);
    itemFactory.updateItem(updateRequest,findItem);
    itemRepository.save(findItem);
    return new ItemResponse(findItem);

  }


  public void isSellerOrAdmin(String sellerNickname, UserRoleEnum role, Item item) {
    if (!sellerNickname.equals(item.getSellerNickname()) && !role.equals(UserRoleEnum.ADMIN)) {
      throw new CustomException(ExceptionStatus.WRONG_USER_T0_CONTACT);
    }
  }

}
