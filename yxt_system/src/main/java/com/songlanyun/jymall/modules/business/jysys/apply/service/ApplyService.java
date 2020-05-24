package com.songlanyun.jymall.modules.business.jysys.apply.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.jysys.apply.entity.Apply;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:27
 */
public interface ApplyService extends BaseService<Apply, Integer> {
    Integer isExitIdCard(String applyAccount);
}
