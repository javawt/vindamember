package com.songlanyun.jymall.modules.business.jysys.appconfig.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.appconfig.entity.AppConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:26
 */
@Repository
public interface AppConfigRepository extends BaseRepository<AppConfig, Integer> {

}
