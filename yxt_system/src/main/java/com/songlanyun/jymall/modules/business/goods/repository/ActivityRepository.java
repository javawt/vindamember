package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.Activity;
import org.springframework.stereotype.Repository;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:49
 */
@Repository
public interface ActivityRepository extends BaseRepository<Activity, Integer> {
}
