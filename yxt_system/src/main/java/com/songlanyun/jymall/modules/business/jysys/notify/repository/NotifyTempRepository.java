package com.songlanyun.jymall.modules.business.jysys.notify.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import org.springframework.stereotype.Repository;

/**
 * Created by zenghang.
 * Date: 2019/12/17
 * Time: 10:54
 */
@Repository
public interface NotifyTempRepository extends BaseRepository<NotifyTemp, Integer> {
}
