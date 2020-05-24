package com.songlanyun.jymall.modules.business.jysys.apply.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.apply.entity.Apply;
import com.songlanyun.jymall.modules.business.jysys.apply.repository.ApplyRepository;
import com.songlanyun.jymall.modules.business.jysys.apply.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:27
 */
@Service
public class ApplyServiceImpl extends BaseServiceImpl<Apply, Integer, ApplyRepository> implements ApplyService {
    public ApplyServiceImpl(ApplyRepository repository) {
        super(repository);
    }

    @Autowired
    private ApplyRepository applyRepository;

    @Override
    public Integer isExitIdCard(String applyAccount) {
        return applyRepository.countByApplyAccount(applyAccount);
    }
}
