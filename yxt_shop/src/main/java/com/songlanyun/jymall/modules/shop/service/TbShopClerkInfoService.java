package com.songlanyun.jymall.modules.shop.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopClerkInfo;
import java.util.List;

/**
 * 店铺店员信息表(TbShopClerkInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-22 16:17:31
 */
public interface TbShopClerkInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbShopClerkInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TbShopClerkInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tbShopClerkInfo 实例对象
     * @return 实例对象
     */
    TbShopClerkInfo insert(TbShopClerkInfo tbShopClerkInfo);

    R select(TbShopClerkInfo TbShopClerkInfo);

    R save(TbShopClerkInfo TbShopClerkInfo);

    R update(TbShopClerkInfo TbShopClerkInfo);

    R deleteById(Long id);

}