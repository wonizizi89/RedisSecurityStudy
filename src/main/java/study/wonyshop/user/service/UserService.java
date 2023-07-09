package study.wonyshop.user.service;

import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.wonyshop.common.exception.CustomException;
import study.wonyshop.common.exception.ExceptionStatus;
import study.wonyshop.delivery.Address;
import study.wonyshop.redis.CacheNames;
import study.wonyshop.redis.RedisDao;
import study.wonyshop.security.jwt.JwtProvider;
import study.wonyshop.user.dto.LoginRequest;
import study.wonyshop.user.dto.SignUpRequest;
import study.wonyshop.user.dto.UserResponse;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.entity.UserRoleEnum;
import study.wonyshop.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final JwtProvider jwtProvider;
  private final RedisDao redisDao;

  /**
   * 회원가입
   *
   * @param signUpRequest
   * @return
   */
  @Transactional
  public ResponseEntity signup(@Valid SignUpRequest signUpRequest) {
    String email = signUpRequest.getEmail();
    String nickname = signUpRequest.getNickName();
    String password = passwordEncoder.encode(signUpRequest.getPassword());
    String phoneNumber = signUpRequest.getPhoneNumber();


    //닉네임 중복 확인
    Optional<User> findNickname = userRepository.findByNickname(nickname);
    if (findNickname.isPresent()) {
      throw new CustomException(ExceptionStatus.DUPLICATED_NICKNAME);
    }
    // 이메일 중복 확인
    Optional<User> findEmail = userRepository.findByEmail(email);
    if (findEmail.isPresent()) {
      throw new CustomException(ExceptionStatus.DUPLICATED_EMAIL);
    }
    // 폰번호 중복 확인
    Optional<User> findPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
    if (findPhoneNumber.isPresent()) {
      throw new CustomException(ExceptionStatus.DUPLICATED_PHONENUMBER);
    }
    UserRoleEnum role = UserRoleEnum.MEMBER;
    User user = signUpRequest.toEntity(role, password);

    user.setProfileImage("default.png");

    userRepository.save(user);
    return ResponseEntity.ok("회원가입 성공");
  }

  /**
   * 로그인 반환값으로 user를 userResponseDto 담아 반환하고  컨트롤러에서 반환된 객체를 이용하여 토큰 발행한다.
   */
  @Cacheable(cacheNames = CacheNames.LOGINUSER, key = "'login'+ #p0.getEmail()", unless = "#result== null")
  @Transactional
  public UserResponse login(LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new CustomException(ExceptionStatus.WRONG_EMAIL)
    );
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new CustomException(ExceptionStatus.WRONG_PASSWORD);
    }
    return new UserResponse().of(user);// user객체를 dto에 담아서 반환
  }
  @CacheEvict(cacheNames = CacheNames.USERBYEMAIL, key = "'login'+#p1")
  @Transactional
  public ResponseEntity logout(String accessToken, String email) {
    // 레디스에 accessToken 사용못하도록 등록
    Long expiration = jwtProvider.getExpiration(accessToken);
    redisDao.setBlackList(accessToken, "logout", expiration);
    if (redisDao.hasKey(email)) {
      redisDao.deleteRefreshToken(email);
    } else {
      throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
    }
    return ResponseEntity.ok("로그아웃 완료");
  }


  public UserResponse getUserInfo(Long id) {
    User findUser = userRepository.findById(id).orElseThrow(
        () -> new IllegalArgumentException("없음 "));
    return UserResponse.of(findUser);
  }

  public ResponseEntity earnPoint(Long id, int point) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new CustomException(ExceptionStatus.WRONG_USER)
    );
   user.earnPoint(point);
   userRepository.save(user);
   return ResponseEntity.ok(point +"P 적립되었습니다. ");
  }
}

