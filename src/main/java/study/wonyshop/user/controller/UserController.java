package study.wonyshop.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.wonyshop.security.dto.TokenResponse;
import study.wonyshop.security.jwt.JwtProvider;
import study.wonyshop.security.service.UserDetailsImpl;
import study.wonyshop.user.dto.LoginRequest;
import study.wonyshop.user.dto.SignUpRequest;
import study.wonyshop.user.dto.UserResponse;
import study.wonyshop.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {


  private final UserService userService;
  private final JwtProvider jwtProvider;

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
   * 로그인
   */

  @PostMapping("/login")
  public TokenResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
    UserResponse user = userService.login(loginRequest);
    TokenResponse token = jwtProvider.createTokenByLogin(user.getEmail(),
        user.getRole());//atk, rtk 생성
    response.addHeader(jwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 에세스 토큰만 싣기
    return token;
  }

  @DeleteMapping("/logout")
  public ResponseEntity logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request){
    String accessToken = jwtProvider.resolveToken(request);
    return userService.logout(accessToken,userDetails.getUsername());//username = email

  }

  @GetMapping("/{id}")
  public UserResponse getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long id){
    return userService.getUserInfo(userDetails.getUsername(),id);
  }


}


//
//    //3. 로그아웃
//    @DeleteMapping("/logout")
//    public MessageResponseDto logout(@AuthenticationPrincipal UserDetailsImpl userDetails
//            , HttpServletRequest request) {
//        String accessToken = jwtUtil.resolveToken(request);
//        return new MessageResponseDto(userService.logout(accessToken, userDetails.getUsername()));
//    }