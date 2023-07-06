package study.wonyshop.item.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.wonyshop.item.dto.ItemRequest;
import study.wonyshop.item.dto.ItemResponse;
import study.wonyshop.item.dto.UpdateRequest;
import study.wonyshop.item.service.ItemService;
import study.wonyshop.security.service.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

  private final ItemService itemService;

  /**
   * 상품 등록
   */
  @PostMapping("/sellers/items")
  public ResponseEntity registerItem(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody ItemRequest request) {
    ItemResponse item = itemService.resisterItem(userDetails.getUser().getNickname(), request);
    return ResponseEntity.ok(item);
  }
  /**
   * 판매자가 등록한 상품 전체 조회.
   */
  @GetMapping("/sellers")
  public Page<ItemResponse> getRegisteredItemList(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "5") int size,
      @RequestParam(value = "direction", required = false, defaultValue = "DESC") Direction direction,
      @RequestParam(value = "properties", required = false, defaultValue = "createdDate") String properties) {
    return itemService.getRegisteredItemList(page, size, direction, properties,userDetails.getUser().getNickname());
  }


  /**
   * 상품 수정 : 해당 판매자와 관리자 삭제 가능
   * @param userDetails
   * @param request
   * @param id
   * @return
   */
  @PutMapping("/sellers/{id}")
public ItemResponse updateItem(@AuthenticationPrincipal UserDetailsImpl userDetails,
    @RequestBody UpdateRequest request,
    @PathVariable Long id){
    return itemService.updateItem(userDetails.getUser().getNickname(),userDetails.getUser().getRole(),id,request);
}


  /**
   * 상품 삭제 : 해당 판매자와 관리자 삭제 가능
   */
  @DeleteMapping("/{id}")
  public ResponseEntity deleteItem(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long id){
    return itemService.deleteItem(userDetails.getUser().getNickname(),userDetails.getUser().getRole(),id);
  }
}
