package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.YjBrand;
import com.songlanyun.jymall.modules.business.goods.repository.YjBrandRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjBrandService;
import org.springframework.stereotype.Service;


@Service
public class YjBrandServiceImpl extends BaseServiceImpl<YjBrand, Integer, YjBrandRepository>
        implements YjBrandService {
    public YjBrandServiceImpl(YjBrandRepository repository) {
        super(repository);
    }

    }
