package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.Cart;
import com.songlanyun.jymall.modules.business.goods.repository.CartRepository;
import com.songlanyun.jymall.modules.business.goods.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/11/27
 * Time: 10:44
 */
@Service
public class CartServiceImpl extends BaseServiceImpl<Cart, Integer, CartRepository> implements CartService {
    public CartServiceImpl(CartRepository repository) {
        super(repository);
    }

    @Override
    public List<Cart> findAllByCartIdIn(List<Integer> cartIds){
        return this.repository.findAllByCartIdIn(cartIds);
    }
}
