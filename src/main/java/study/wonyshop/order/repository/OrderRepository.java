package study.wonyshop.order.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import study.wonyshop.order.entity.Order;
import study.wonyshop.user.entity.User;

public interface OrderRepository extends JpaRepository<Order,Long> {

  Optional<Order> findByIdAndUser(Long id, User user);
}
