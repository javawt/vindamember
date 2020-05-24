package com.songlanyun.jymall.modules.business.order.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.order.entity.OrderAddress;
import com.songlanyun.jymall.modules.business.order.repository.OrderAddressRepository;
import com.songlanyun.jymall.modules.business.order.service.OrderAddressService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:12
 */
@Service
public class OrderAddressServiceImpl extends BaseServiceImpl<OrderAddress, Integer, OrderAddressRepository> implements OrderAddressService {
    public OrderAddressServiceImpl(OrderAddressRepository repository) {
        super(repository);
    }
}
