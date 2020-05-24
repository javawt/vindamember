package com.songlanyun.jymall.modules.shop.service;

import com.songlanyun.jymall.modules.shop.entity.TbShopInfo;

/**
 * 店铺信息表(TbShopInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-12 11:21:26
 */
public interface TbShopInfoService {

    void saveInfo(TbShopInfo tbShopInfo);

    int selectCount(String userId);

}