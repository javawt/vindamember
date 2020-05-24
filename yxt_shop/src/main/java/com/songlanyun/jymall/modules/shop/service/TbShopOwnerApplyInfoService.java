package com.songlanyun.jymall.modules.shop.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopOwnerApplyInfo;

/**
 * 店铺申请表(TbShopOwnerApplyInfo)表服务接口
 *
 * @author makejava
 * @since 2020-05-11 10:57:14
 */
public interface TbShopOwnerApplyInfoService {

    void save(TbShopOwnerApplyInfo tbShopOwnerApplyInfo);

    public abstract TbShopOwnerApplyInfo selectNewRecode();

    R checkApply(TbShopOwnerApplyInfo tbShopOwnerApplyInfo);

    R selectApplyList(TbShopOwnerApplyInfo tbShopOwnerApplyInfo);

}