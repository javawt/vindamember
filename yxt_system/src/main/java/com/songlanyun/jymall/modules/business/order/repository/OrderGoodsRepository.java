package com.songlanyun.jymall.modules.business.order.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGoodsRepository extends BaseRepository<OrderGoods, Integer> {

    List<OrderGoods> findAllByOrderNo(String orderNo);

}