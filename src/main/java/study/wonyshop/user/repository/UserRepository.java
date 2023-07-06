package study.wonyshop.user.repository;

import java.util.List;
import java.util.Optional;
import javax.validation.constraints.Email;
import org.apache.tomcat.websocket.PojoHolder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.wonyshop.order.dto.OrderResponse;
import study.wonyshop.redis.CacheNames;
import study.wonyshop.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {


  Optional<User> findByNickname(String nickname);

  @Cacheable(cacheNames = CacheNames.USERBYEMAIL, key = "'login'+#p0", unless = "#result==null")
  Optional<User> findByEmail(String email);

  Optional<User> findByPhoneNumber(String phoneNumber);

}
///**
//* 1:N 관계인 orderItems 조회
//*/
//    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
//        return em.createQuery(
//                "select new
//jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name,
//oi.orderPrice, oi.count)" +
//                        " from OrderItem oi" +
//                        " join oi.item i" +
//                        " where oi.order.id = : orderId",
//OrderItemQueryDto.class)
//                .setParameter("orderId", orderId)
//.getResultList();

