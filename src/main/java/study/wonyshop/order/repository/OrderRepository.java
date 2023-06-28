package study.wonyshop.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.wonyshop.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
