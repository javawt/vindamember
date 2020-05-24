package com.songlanyun.jymall.modules.business.order.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:26
 */
public interface OrderGoodsService extends BaseService<OrderGoods, Integer> {
    List<OrderGoods> findAllByOrderNo(String orderNo);
}
