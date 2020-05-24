package com.songlanyun.jymall.modules.business.jysys.bannertext.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.bannertext.entity.BannerText;
import com.songlanyun.jymall.modules.business.jysys.bannertext.repository.BannerTextRepository;
import com.songlanyun.jymall.modules.business.jysys.bannertext.service.BannerTextService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang on 2019/11/22.
 */
@Service
public class BannerTextServiceImpl extends BaseServiceImpl<BannerText, Integer, BannerTextRepository> implements BannerTextService {
    public BannerTextServiceImpl(BannerTextRepository repository) {
        super(repository);
    }
}
