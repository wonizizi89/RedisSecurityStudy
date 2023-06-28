package study.wonyshop.user.repository;

import java.util.Optional;
import javax.validation.constraints.Email;
import org.apache.tomcat.websocket.PojoHolder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import study.wonyshop.redis.CacheNames;
import study.wonyshop.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {


  Optional<User> findByNickname(String nickname);
  //@Cacheable(cacheNames = CacheNames.USERBYEMAIL, key = "'login'+#p0", unless = "#result==null")
  Optional<User> findByEmail(String email);
  Optional<User> findByPhoneNumber(String phoneNumber);

}
