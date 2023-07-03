package study.wonyshop.delivery.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.wonyshop.delivery.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {

}
