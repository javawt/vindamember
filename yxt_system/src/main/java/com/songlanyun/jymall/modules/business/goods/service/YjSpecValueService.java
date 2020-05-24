package com.songlanyun.jymall.modules.business.goods.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecValue;

/**
 * 商品规格值记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface YjSpecValueService extends BaseService<YjSpecValue, Integer> {
    void deleteYjSpecValueBySpecId(Integer specId);
}

