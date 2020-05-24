package com.songlanyun.jymall.modules.business.jysys.banner.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.banner.entity.Banner;
import com.songlanyun.jymall.modules.business.jysys.banner.repository.BannerRepository;
import com.songlanyun.jymall.modules.business.jysys.banner.service.BannerService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang on 2019/11/22.
 */
@Service
public class BannerServiceImpl extends BaseServiceImpl<Banner, Integer, BannerRepository> implements BannerService {
    public BannerServiceImpl(BannerRepository repository) {
        super(repository);
    }
}
