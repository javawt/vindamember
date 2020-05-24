package com.songlanyun.jymall.modules.shop.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopConfigInfo;

/**
 * 店铺配置信息表(TbShopConfigInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-13 10:16:05
 */
public interface TbShopConfigInfoService {

    /**
     * 修改数据
     *
     * @param tbShopConfigInfo 实例对象
     * @return 实例对象
     */
    R update(TbShopConfigInfo tbShopConfigInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    R deleteById(Long id);

    R save(TbShopConfigInfo tbShopConfigInfo);

    R select(TbShopConfigInfo tbShopConfigInfo);

}