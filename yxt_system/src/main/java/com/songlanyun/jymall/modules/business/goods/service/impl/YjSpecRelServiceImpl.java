package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecRel;
import com.songlanyun.jymall.modules.business.goods.repository.YjSpecRelRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecRelService;
import org.springframework.stereotype.Service;


@Service
public class YjSpecRelServiceImpl extends BaseServiceImpl<YjSpecRel, Integer, YjSpecRelRepository> implements YjSpecRelService {

    public YjSpecRelServiceImpl(YjSpecRelRepository repository) {
        super(repository);
    }


    @Override
    public void deleteYhSpecRelByGoodsId(Integer goodsId) {
        repository.deleteYhSpecRelByGoodsId(goodsId);
    }
}
