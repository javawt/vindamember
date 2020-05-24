package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品规格表
 * 
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Repository
public interface YjGoodsSkuRepository  extends BaseRepository<YjGoodsSku, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM YjGoodsSku a WHERE a.goodsId= ?1")
    void deleteYjGoodsSkuByGoodsId(Integer goodsId);

    @Modifying
    @Transactional
    @Query("select a FROM YjGoodsSku a WHERE a.goodsId= ?1")
    List<YjGoodsSku> selectYjGoodsSkuByGoodsId(Integer goodsId);

    List<YjGoodsSku> findAllByGoodsSkuIdIn(List<Integer> goodsIds);

}
