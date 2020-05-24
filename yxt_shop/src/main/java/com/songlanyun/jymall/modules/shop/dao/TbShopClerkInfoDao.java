package com.songlanyun.jymall.modules.shop.dao;

import com.songlanyun.jymall.modules.shop.entity.TbShopClerkInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 店铺店员信息表(TbShopClerkInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-22 16:17:31
 */
@Mapper
public interface TbShopClerkInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbShopClerkInfo queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<TbShopClerkInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbShopClerkInfo 实例对象
     * @return 对象列表
     */
    List<TbShopClerkInfo> queryAll(TbShopClerkInfo tbShopClerkInfo);

    /**
     * 新增数据
     *
     * @param tbShopClerkInfo 实例对象
     * @return 影响行数
     */
    int insert(TbShopClerkInfo tbShopClerkInfo);

    /**
     * 修改数据
     *
     * @param tbShopClerkInfo 实例对象
     * @return 影响行数
     */
    int update(TbShopClerkInfo tbShopClerkInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    List<TbShopClerkInfo> select(TbShopClerkInfo TbShopClerkInfo);

}