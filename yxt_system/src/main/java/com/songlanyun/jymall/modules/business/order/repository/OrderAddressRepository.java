package com.songlanyun.jymall.modules.business.order.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.order.entity.OrderAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAddressRepository extends BaseRepository<OrderAddress, Integer> {

}