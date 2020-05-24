package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.Activity;
import com.songlanyun.jymall.modules.business.goods.repository.ActivityRepository;
import com.songlanyun.jymall.modules.business.goods.service.ActivityService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:50
 */
@Service
public class ActivityServiceImpl extends BaseServiceImpl<Activity, Integer, ActivityRepository> implements ActivityService {
    public ActivityServiceImpl(ActivityRepository repository) {
        super(repository);
    }
}
