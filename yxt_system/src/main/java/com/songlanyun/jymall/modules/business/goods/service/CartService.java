package com.songlanyun.jymall.modules.business.goods.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.goods.entity.Cart;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/11/27
 * Time: 10:43
 */
public interface CartService extends BaseService<Cart, Integer> {
    List<Cart> findAllByCartIdIn(List<Integer> cartIds);
}
