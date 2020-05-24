package com.songlanyun.jymall.modules.shop.dao;

import com.songlanyun.jymall.modules.shop.entity.TbShopConfigInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 店铺配置信息表(TbShopConfigInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-13 10:16:05
 */
@Mapper
public interface TbShopConfigInfoDao {


    /**
     * 修改数据
     *
     * @param tbShopConfigInfo 实例对象
     * @return 影响行数
     */
    int update(TbShopConfigInfo tbShopConfigInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

    void save(TbShopConfigInfo tbShopConfigInfo);

    List<TbShopConfigInfo> select(TbShopConfigInfo tbShopConfigInfo);

    TbShopConfigInfo getOne(Long id);

}