package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpec;
import com.songlanyun.jymall.modules.business.goods.repository.YjSpecRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecService;
import org.springframework.stereotype.Service;


@Service
public class YjSpecServiceImpl extends BaseServiceImpl<YjSpec, Integer, YjSpecRepository> implements YjSpecService {

    public YjSpecServiceImpl(YjSpecRepository repository) {
        super(repository);
    }
}
