package com.songlanyun.jymall.modules.shop.dao;

import com.songlanyun.jymall.modules.shop.entity.TbShopInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺信息表(TbShopInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-12 11:21:26
 */
@Mapper
public interface TbShopInfoDao {

    void saveInfo(TbShopInfo tbShopInfo);

    int selectCount(String userId);

}