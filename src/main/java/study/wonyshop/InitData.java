package study.wonyshop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.wonyshop.user.entity.User;
import study.wonyshop.user.entity.UserRoleEnum;
import study.wonyshop.user.repository.UserRepository;

/**
 * 스프링 구동 시점에 run() 되어 데이터베이스에 저장됨
 */
@Component
@RequiredArgsConstructor
public class InitData implements ApplicationRunner {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {



    User customer = new User("customer123@naver.com", "customer", passwordEncoder.encode("customer1234"), "대전", "default.png",
        UserRoleEnum.MEMBER, "01022223333");
    User customer1 = new User("customer1123@naver.com", "customer1", passwordEncoder.encode("customer11234"), "대전", "default.png",
        UserRoleEnum.MEMBER, "01011113333");
    User seller = new User("rupi1234@naver.com", "rupi", passwordEncoder.encode("rupi1234"), "대구", "default.png",
        UserRoleEnum.SELLER, "01033334444");
    User admin = new User("pororo12@naver.com", "pororo", passwordEncoder.encode("pororo1234"), "부산", "default.png",
        UserRoleEnum.MEMBER, "01044445555");
    userRepository.save(customer);
    userRepository.save(customer1);
    userRepository.save(seller);
    userRepository.save(admin);
  }

}