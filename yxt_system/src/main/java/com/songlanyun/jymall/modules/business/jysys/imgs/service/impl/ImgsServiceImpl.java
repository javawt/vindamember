package com.songlanyun.jymall.modules.business.jysys.imgs.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.imgs.entity.Imgs;
import com.songlanyun.jymall.modules.business.jysys.imgs.repository.ImgsRepository;
import com.songlanyun.jymall.modules.business.jysys.imgs.service.ImgsService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang on 2019/11/21.
 */
@Service
public class ImgsServiceImpl extends BaseServiceImpl<Imgs, Integer, ImgsRepository> implements ImgsService {

    public ImgsServiceImpl(ImgsRepository repository) {
        super(repository);
    }
}
