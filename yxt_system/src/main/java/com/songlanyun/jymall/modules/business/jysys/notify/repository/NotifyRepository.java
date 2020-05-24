package com.songlanyun.jymall.modules.business.jysys.notify.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.Notify;
import org.springframework.stereotype.Repository;

/**
 * Created by zenghang.
 * Date: 2019/11/29
 * Time: 9:54
 */
@Repository
public interface NotifyRepository extends BaseRepository<Notify, Integer> {
}
