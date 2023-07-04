package study.wonyshop;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import study.wonyshop.delivery.Address;
import study.wonyshop.item.entity.Album;
import study.wonyshop.item.entity.Book;
import study.wonyshop.item.entity.Item;
import study.wonyshop.item.entity.Movie;
import study.wonyshop.item.repository.ItemRepository;
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
  private final ItemRepository itemRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    User customer = new User("customer123@naver.com", "customer",
        passwordEncoder.encode("customer1234"), new Address("대전", "계룡로", "123"), "default.png",
        UserRoleEnum.MEMBER, "01022223333", 0);
    User customer1 = new User("customer1123@naver.com", "customer1",
        passwordEncoder.encode("customer11234"), new Address("대전", "계룡로", "123"), "default.png",
        UserRoleEnum.MEMBER, "01011113333");
    User seller = new User("rupi1234@naver.com", "뽀로로", passwordEncoder.encode("rupi1234"),
        new Address("대전", "계룡로", "123"), "default.png",
        UserRoleEnum.SELLER, "01033334444");
    User seller1 = new User("rupi12345@naver.com", "크롱", passwordEncoder.encode("rupi1234"),
        new Address("대전", "계룡로", "123"), "default.png",
        UserRoleEnum.SELLER, "01034554444");
    User admin = new User("pororo12@naver.com", "pororo", passwordEncoder.encode("pororo1234"),
        new Address("대전", "계룡로", "123"), "default.png",
        UserRoleEnum.MEMBER, "01044445555");
    userRepository.save(customer);
    userRepository.save(customer1);
    userRepository.save(seller);
    userRepository.save(seller1);
    userRepository.save(admin);
    for (int i = 1; i < 6; i++) {
      Album album = new Album("ALBUM", i * 1000, i * 10, "가을편지", "default" + i + ".png", "뽀로로",
          "아이유", "갠소장각");
      itemRepository.save(album);
      Book book = new Book("좋은책", i * 1000, i * 10, "좋은책이야", "default" + i + ".png", "크롱", "좋은사람",
          "isbn"+i*4);
      itemRepository.save(book);
      Movie movie = new Movie("무비무비" + i, i * 1000, 10 * i, "영화엔 팝콘각", "default" + i + ".png", "크롱",
          "감독" + i, "배우" + i);
      itemRepository.save(movie);
    }

  }
}