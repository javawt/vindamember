package com.songlanyun.jymall.modules.shop.service.impl;

import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.shop.dao.TbShopInfoDao;
import com.songlanyun.jymall.modules.shop.entity.TbShopInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 店铺信息表(TbShopInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-05-12 11:21:26
 */
@Service("tbShopInfoService")
public class TbShopInfoServiceImpl implements TbShopInfoService {
    @Resource
    private TbShopInfoDao tbShopInfoDao;

    @Override
    public void saveInfo(TbShopInfo tbShopInfo) {
        if(StringUtils.isBlank(tbShopInfo.getUserId())){
            tbShopInfo.setUserId(ShiroUtils.getUserId());
        }
        tbShopInfoDao.saveInfo(tbShopInfo);
    }

    @Override
    public int selectCount(String userId) {
        return tbShopInfoDao.selectCount(userId);
    }
}