package com.songlanyun.jymall.modules.shop.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopBannerInfo;

/**
 * 店铺轮播图表(TbShopBannerInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-13 14:05:44
 */
public interface TbShopBannerInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbShopBannerInfo queryById(Long id);

    R selectList(TbShopBannerInfo tbShopBannerInfo);

    /**
     * 新增数据
     *
     * @param tbShopBannerInfo 实例对象
     * @return 实例对象
     */
    R insert(TbShopBannerInfo tbShopBannerInfo);

    /**
     * 修改数据
     *
     * @param tbShopBannerInfo 实例对象
     * @return 实例对象
     */
    R update(TbShopBannerInfo tbShopBannerInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    R deleteById(Long id);

}