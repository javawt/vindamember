package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 商品记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Repository
public interface YjGoodsRepository extends BaseRepository<YjGoods, Integer> {

    List<YjGoods> findAllByGoodsIdIn(Integer[] ids);

    @Modifying
    @Transactional
    @Query("update YjGoods a set a.activityId = null ,a.hasActivity = false where a.activityId = ?1")
    void updateByActivityId(Integer activityId);

    @Modifying
    @Transactional
    @Query("update YjGoods a set a.goodsType = ?2 where a.goodsId = ?1")
    void updateGoodsType(Integer goodsId, String goodsType);

    @Query("select count(a.goodsId) from YjGoods a")
    int getAllGoods();

}
