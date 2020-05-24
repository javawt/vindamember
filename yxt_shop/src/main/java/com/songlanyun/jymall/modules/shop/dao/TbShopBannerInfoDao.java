package com.songlanyun.jymall.modules.shop.dao;

import com.songlanyun.jymall.modules.shop.entity.TbShopBannerInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 店铺轮播图表(TbShopBannerInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-13 14:05:44
 */
@Mapper
public interface TbShopBannerInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbShopBannerInfo queryById(Long id);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbShopBannerInfo 实例对象
     * @return 对象列表
     */
    List<TbShopBannerInfo> queryAll(TbShopBannerInfo tbShopBannerInfo);

    List<TbShopBannerInfo> selectList(TbShopBannerInfo tbShopBannerInfo);

    /**
     * 新增数据
     *
     * @param tbShopBannerInfo 实例对象
     * @return 影响行数
     */
    int insert(TbShopBannerInfo tbShopBannerInfo);

    /**
     * 修改数据
     *
     * @param tbShopBannerInfo 实例对象
     * @return 影响行数
     */
    int update(TbShopBannerInfo tbShopBannerInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}