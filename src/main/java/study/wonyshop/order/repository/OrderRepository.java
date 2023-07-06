package study.wonyshop.order.repository;


import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.wonyshop.order.entity.Order;
import study.wonyshop.user.entity.User;

public interface OrderRepository extends JpaRepository<Order,Long> {

  Optional<Order> findByIdAndUser(Long id, User user);


  Page<Order> findAllBySellerNickname(PageRequest of, String sellerNickname);

}
