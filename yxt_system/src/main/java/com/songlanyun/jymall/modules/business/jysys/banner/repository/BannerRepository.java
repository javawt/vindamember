package com.songlanyun.jymall.modules.business.jysys.banner.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.banner.entity.Banner;
import org.springframework.stereotype.Repository;

/**
 * Created by zenghang on 2019/11/22.
 */
@Repository
public interface BannerRepository extends BaseRepository<Banner, Integer> {
}
