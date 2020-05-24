package com.songlanyun.jymall.modules.business.jysys.bank.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.bank.entity.BankBind;
import com.songlanyun.jymall.modules.business.jysys.bank.repository.BankBindRepository;
import com.songlanyun.jymall.modules.business.jysys.bank.service.BankBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang on 2019/11/22.
 */
@Service
public class BankBindServiceImpl extends BaseServiceImpl<BankBind, Integer, BankBindRepository> implements BankBindService {
    public BankBindServiceImpl(BankBindRepository repository) {
        super(repository);
    }

    @Autowired
    private BankBindRepository bankBindRepository;

//    @Override
//    public BankBind isExitIdCard(String idCard) {
//        return bankBindRepository.isExitIdCard(idCard);
//    }
}
