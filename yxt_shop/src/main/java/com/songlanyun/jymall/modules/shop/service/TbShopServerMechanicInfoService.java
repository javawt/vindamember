package com.songlanyun.jymall.modules.shop.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopServerMechanicInfo;
import java.util.List;

/**
 * 店铺服务技工信息表(TbShopServerMechanicInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-22 10:19:35
 */
public interface TbShopServerMechanicInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbShopServerMechanicInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TbShopServerMechanicInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tbShopServerMechanicInfo 实例对象
     * @return 实例对象
     */
    TbShopServerMechanicInfo insert(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    public abstract R select(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    public abstract R save(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    public abstract R update(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    R deleteById(Long id);

}