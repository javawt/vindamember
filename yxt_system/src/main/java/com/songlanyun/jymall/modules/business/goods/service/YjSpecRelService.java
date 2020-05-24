package com.songlanyun.jymall.modules.business.goods.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecRel;

/**
 * 商品与规格值关系记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface YjSpecRelService extends BaseService<YjSpecRel, Integer> {
    void deleteYhSpecRelByGoodsId(Integer goodsId);
}

