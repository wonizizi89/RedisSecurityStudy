package study.wonyshop.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import study.wonyshop.item.dto.ItemSearchCond;

public interface SearchItemCustom {
  Page searchItemByDynamicCond(PageRequest page, ItemSearchCond itemSearchCond);
}
