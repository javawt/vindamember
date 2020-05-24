package com.songlanyun.jymall.modules.business.score.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.score.entity.YjCash;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:21
 */
public interface YjCashService extends BaseService<YjCash, Integer>  {
    void updateCheckStatus(YjCash yjCash);
}
