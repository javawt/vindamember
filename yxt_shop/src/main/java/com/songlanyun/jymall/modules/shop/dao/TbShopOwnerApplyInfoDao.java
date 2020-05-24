package com.songlanyun.jymall.modules.shop.dao;

import com.songlanyun.jymall.modules.shop.entity.TbShopOwnerApplyInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 店铺申请表(TbShopOwnerApplyInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-11 10:57:13
 */
@Mapper
public interface TbShopOwnerApplyInfoDao {

    void save(TbShopOwnerApplyInfo tbShopOwnerApplyInfo);

    TbShopOwnerApplyInfo selectNewRecode(String userId);

    void checkApply(TbShopOwnerApplyInfo tbShopOwnerApplyInfo);

    TbShopOwnerApplyInfo getShopOwnerApplyInfo(Long id);

    List<TbShopOwnerApplyInfo> selectApplyList(TbShopOwnerApplyInfo tbShopOwnerApplyInfo);

}