package com.songlanyun.jymall.modules.business.jysys.appconfig.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.appconfig.entity.AppConfig;
import com.songlanyun.jymall.modules.business.jysys.appconfig.repository.AppConfigRepository;
import com.songlanyun.jymall.modules.business.jysys.appconfig.service.AppConfigService;
import org.springframework.stereotype.Service;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:27
 */
@Service
public class AppConfigServiceImpl extends BaseServiceImpl<AppConfig, Integer, AppConfigRepository> implements AppConfigService {
    public AppConfigServiceImpl(AppConfigRepository repository) {
        super(repository);
    }

}
