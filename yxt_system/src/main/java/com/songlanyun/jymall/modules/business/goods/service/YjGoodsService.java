package com.songlanyun.jymall.modules.business.goods.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;

import java.util.List;

/**
 * 商品记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface YjGoodsService  extends BaseService<YjGoods, Integer> {
    List<YjGoods> findAllByGoodsIdIn(Integer[] ids);

    void updateByActivityId(Integer activityId);
    /** 保存并返回 对象 **/
    YjGoods saveGoods(YjGoods goods);

    void updateGoodsType(Integer goodsId, String goodsType);

    PageUtils findActivityGoods(int activityId, int page, int size);

    void cleanActivityGoods();

    int getAllGoods();
}

