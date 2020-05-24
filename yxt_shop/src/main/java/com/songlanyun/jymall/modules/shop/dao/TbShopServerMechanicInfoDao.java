package com.songlanyun.jymall.modules.shop.dao;

import com.songlanyun.jymall.modules.shop.entity.TbShopServerMechanicInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 店铺服务技工信息表(TbShopServerMechanicInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-22 10:19:35
 */
@Mapper
public interface TbShopServerMechanicInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbShopServerMechanicInfo queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TbShopServerMechanicInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbShopServerMechanicInfo 实例对象
     * @return 对象列表
     */
    List<TbShopServerMechanicInfo> queryAll(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    List<TbShopServerMechanicInfo> select(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    /**
     * 新增数据
     *
     * @param tbShopServerMechanicInfo 实例对象
     * @return 影响行数
     */
    int insert(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    /**
     * 修改数据
     *
     * @param tbShopServerMechanicInfo 实例对象
     * @return 影响行数
     */
    int update(TbShopServerMechanicInfo tbShopServerMechanicInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);





}