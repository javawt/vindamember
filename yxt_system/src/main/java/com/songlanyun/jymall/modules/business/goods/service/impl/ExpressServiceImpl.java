package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.Express;
import com.songlanyun.jymall.modules.business.goods.repository.ExpressRepository;
import com.songlanyun.jymall.modules.business.goods.service.ExpressService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:35
 */
@Service
public class ExpressServiceImpl extends BaseServiceImpl<Express, Integer, ExpressRepository> implements ExpressService {
    public ExpressServiceImpl(ExpressRepository repository) {
        super(repository);
    }
}
