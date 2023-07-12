package co.yixiang.modules.shop.repository;

import co.yixiang.modules.shop.domain.YxProductTemplateItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author yushen
* @date 2023-05-11
*/
public interface YxProductTemplateItemRepository extends JpaRepository<YxProductTemplateItem, Integer>, JpaSpecificationExecutor<YxProductTemplateItem> {
}