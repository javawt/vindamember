package com.songlanyun.jymall.service;

import com.songlanyun.jymall.domain.ServerGoods;
import com.songlanyun.jymall.domain.ServerPrj;
import com.songlanyun.jymall.domain.ServerTime;

/**
* @author hupeng
* @date 2020-01-09
*/
public interface ServerPrjService {

    ServerPrj add(ServerPrj goodsDTO);

    ServerPrj add(ServerTime goodsDTO);

    /*** 删除某服务 **/
    void deleteById(Long id);


    ServerPrj update(ServerPrj goodsDTO);
}