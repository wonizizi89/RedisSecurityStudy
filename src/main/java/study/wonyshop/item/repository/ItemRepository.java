package study.wonyshop.item.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import study.wonyshop.item.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

  Optional<Item> findBySellerNicknameIsAndId(String sellerNickname,Long id);



  Page<Item> findAllBySellerNickname(PageRequest of, String sellerNickname);

}
