package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/11/27
 * Time: 10:42
 */
@Repository
public interface CartRepository extends BaseRepository<Cart, Integer> {

    List<Cart> findAllByCartIdIn(List<Integer> cartIds);
}
