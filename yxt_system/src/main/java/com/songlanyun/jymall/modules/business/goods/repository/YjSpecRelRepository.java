package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecRel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品与规格值关系记录表
 * 
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Repository
public interface YjSpecRelRepository extends BaseRepository<YjSpecRel, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM YjSpecRel a WHERE a.goodsId= ?1")
    void deleteYhSpecRelByGoodsId(Integer goodsId);
}
