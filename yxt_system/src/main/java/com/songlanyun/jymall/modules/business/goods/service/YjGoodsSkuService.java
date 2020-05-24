package com.songlanyun.jymall.modules.business.goods.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;

import java.util.List;

/**
 * 商品规格表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface YjGoodsSkuService  extends BaseService<YjGoodsSku, Integer> {

    void deleteYjGoodsSkuByGoodsId(Integer goodsId);

    List<YjGoodsSku> selectYjGoodsSkuByGoodsId(Integer goodsId);

    List<YjGoodsSku> findAllByGoodsSkuIdIn(List<Integer> goodsIds);
}

