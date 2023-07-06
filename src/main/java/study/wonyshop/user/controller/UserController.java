package study.wonyshop.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.wonyshop.item.dto.ItemResponse;
import study.wonyshop.item.dto.ItemOrderCond;
import study.wonyshop.item.dto.ItemSearchCond;
import study.wonyshop.item.service.ItemService;
import study.wonyshop.security.dto.ReissueTokenRequest;
import study.wonyshop.security.dto.TokenResponse;
import study.wonyshop.security.jwt.JwtProvider;
import study.wonyshop.security.service.UserDetailsImpl;
import study.wonyshop.user.dto.EarnPointRequest;
import study.wonyshop.user.dto.LoginRequest;
import study.wonyshop.user.dto.SignUpRequest;
import study.wonyshop.user.dto.UserResponse;
import study.wonyshop.user.service.UserService;
// 1. 유저 머니 충전하기( 추후 시간 될때)
//2. 유저 머니 지불하기 ( 추후 시간될때)
//3. 주문 조회 ( /api/orders 에 주문 관련 있음)
/**
 * 상품전체조회 및 단건 조회 는 권한 허용하기 api/users/items
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


  private final UserService userService;
  private final JwtProvider jwtProvider;
  private final ItemService itemService;


  /**
   * 회원가입
   *
   * @param signUpRequest
   * @return 회원가입 성공
   */
  @PostMapping("/signup")
  public ResponseEntity signup(@RequestBody @Valid SignUpRequest signUpRequest) {
    //패스워드 1과 패스워드 2 가 동일 한지 체크
    if (!signUpRequest.getPassword().equals(signUpRequest.getPassword2())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
    }
    return userService.signup(signUpRequest);
  }

  /**
   * 로그인 로그인 시 atk, rtk 이 생성되어 response header 에 담아 보낸다. rtk 는 레디스에 저장한다. 추후 atk 만료시 rtk를 이용해 atk
   * 재발급 하기 위함
   */

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    UserResponse user = userService.login(loginRequest);
    TokenResponse token = jwtProvider.createTokenByLogin(user.getEmail(),
        user.getRole());//atk, rtk 생성
    response.addHeader(jwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 에세스 토큰만 싣기
    return token;
  }

  /**
   * 로그아웃 현 accessToken 은 다시 사용하지 못하도록 레디스에 저장해두고, 로그아웃시 레디스에 저장된 refreshToken 삭제
   *
   * @param userDetails
   * @param request
   * @return
   */

  @DeleteMapping("/logout")
  public ResponseEntity logout(@AuthenticationPrincipal UserDetailsImpl userDetails,
      HttpServletRequest request) {
    String accessToken = jwtProvider.resolveToken(request);
    return userService.logout(accessToken, userDetails.getUsername());//username = email

  }

  /**
   * AccessToken  재발급 매 API 호출 시 시큐리티필터를 통해 인증인가를 받게  된다. 이때 만료된 토큰인지 검증하고 만료시 만료된토큰임을 에러메세지로 보낸다.
   * 그럼 클라이언트에서 에러메세지를 확인 후 이 api(atk 재발급 ) 을 요청 하게 된다.
   *
   * @param userDetails
   * @param tokenRequest : refreshToken
   * @return AccessToken + RefreshToken
   */
  @PostMapping("/reissue-token")
  public TokenResponse reissueToken(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody ReissueTokenRequest tokenRequest) {
    //유저 객체 정보를 이용하여 토큰 발행
    UserResponse user = UserResponse.of(userDetails.getUser());
    return jwtProvider.reissueAtk(user.getEmail(), user.getRole(), tokenRequest.getRefreshToken());
  }

  /**
   * 해당 유저 정보 조회
   *
   * @param userDetails
   * @return
   */
  @GetMapping("/user-info")
  public UserResponse getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return userService.getUserInfo(userDetails.getUsername());
    //return userService.getUserInfo(userDetails.getUser().getEmail());
  }


  /**
   * 상품 전체 조회
   *
   * @param page
   * @param size
   * @param direction
   * @param properties
   * @return 상품 리스트
   */
  @GetMapping("/items")
  public Page<ItemResponse> getItems(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "5") int size,
      @RequestParam(value = "direction", required = false, defaultValue = "DESC") Direction direction,
      @RequestParam(value = "properties", required = false, defaultValue = "createdDate") String properties) {
    return itemService.getItemList(page, size, direction, properties);
  }


  /**
   * 단일 상품 조회
   */

  @GetMapping("/items/{id}")
  public ItemResponse getSelectedItem(@PathVariable Long id) {
    return itemService.getSelectedItem(id);
  }

  /**
   * 상품 검색 기능 조회 . 동적인 검색 기능 + 페이징 (QueryDsl 의 OrderSpecifier + page 이용 ) V2
   * 검색조건 : name, priceGoe , priceLoe 3가지
   */
  @GetMapping("/items/search")
  public Page<ItemResponse> searchItemByDynamicCond(
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      @RequestParam(value = "size", required = false, defaultValue = "5") int size,
      @RequestParam(value = "itemOrderCondStr", required = false) String itemOrderCondStr,
      @RequestParam(value = "itemSearchCondStr", required = false) String itemSearchCondStr) {
    ItemOrderCond itemOrderCond = new ItemOrderCond();
    if (StringUtils.hasText(itemOrderCondStr)) {
      String[] value = itemOrderCondStr.split(",");
      itemOrderCond.setDirection(value[0]); // null 경우 DESC 반환
      itemOrderCond.setProperties(value[1]); // null 경우 createdDate 반환
    }
    ItemSearchCond itemSearchCond = new ItemSearchCond();
    if (StringUtils.hasText(itemSearchCondStr)) {
      // 문자열 값을 ItemSearchCond 객체에 설정
      String[] values = itemSearchCondStr.split(",");
      itemSearchCond.setName(values[0]);
      itemSearchCond.setPriceGoe(values[1]);
      itemSearchCond.setPriceLoe(values[2]);
    }
    return itemService.searchItemByDynamicCond(page, size, itemOrderCond, itemSearchCond);
  }

/**
 *  포인트 충전하기
 *
 *  포인트 얼마 적립 디티오로 받아오면 유저 객체 불러와서 저장하기
 */
@PostMapping("/points")
  public ResponseEntity earnPoint(@AuthenticationPrincipal UserDetailsImpl userDetails,
    @RequestBody EarnPointRequest earnPointRequest){
  int point = earnPointRequest.getPoint();
  return userService.earnPoint(userDetails.getUser().getId(),point);
}
}

