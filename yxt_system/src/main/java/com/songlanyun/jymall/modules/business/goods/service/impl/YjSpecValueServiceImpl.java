package com.songlanyun.jymall.modules.business.goods.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecValue;
import com.songlanyun.jymall.modules.business.goods.repository.YjSpecValueRepository;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class YjSpecValueServiceImpl extends BaseServiceImpl<YjSpecValue, Integer, YjSpecValueRepository> implements YjSpecValueService {

    public YjSpecValueServiceImpl(YjSpecValueRepository repository) {
        super(repository);
    }

    @Autowired
    private YjSpecValueRepository yjSpecValueRepository;
    @Override
    public void deleteYjSpecValueBySpecId(Integer specId) {
        yjSpecValueRepository.deleteYjSpecValueBySpecIds(specId);
    }
}
