package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.YjCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Repository
public interface YjCategoryRepository  extends BaseRepository<YjCategory, Integer> {
    @Query(value = "select a from YjCategory a where a.name like ?1",
            countQuery = "select count(a) from YjCategory")
    Page<YjCategory> pageableDemo(String name, Pageable pageable);

    List<YjCategory> findAllByParentId(Integer parentId);
}
