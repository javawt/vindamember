package com.songlanyun.jymall.modules.business.order.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import com.songlanyun.jymall.modules.business.order.repository.OrderGoodsRepository;
import com.songlanyun.jymall.modules.business.order.service.OrderGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:26
 */
@Service
public class OrderGoodsServiceImpl extends BaseServiceImpl<OrderGoods, Integer, OrderGoodsRepository> implements OrderGoodsService {
    public OrderGoodsServiceImpl(OrderGoodsRepository repository) {
        super(repository);
    }

    @Override
    public List<OrderGoods> findAllByOrderNo(String orderNo) {
        return this.repository.findAllByOrderNo(orderNo);
    }
}
