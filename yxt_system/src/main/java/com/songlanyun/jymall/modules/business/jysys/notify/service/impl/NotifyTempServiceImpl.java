package com.songlanyun.jymall.modules.business.jysys.notify.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import com.songlanyun.jymall.modules.business.jysys.notify.repository.NotifyTempRepository;
import com.songlanyun.jymall.modules.business.jysys.notify.service.NotifyTempService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2019/12/17
 * Time: 10:55
 */
@Service
public class NotifyTempServiceImpl extends BaseServiceImpl<NotifyTemp, Integer, NotifyTempRepository> implements NotifyTempService {
    public NotifyTempServiceImpl(NotifyTempRepository repository) {
        super(repository);
    }
}
