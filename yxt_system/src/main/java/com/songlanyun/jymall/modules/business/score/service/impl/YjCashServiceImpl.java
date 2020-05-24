package com.songlanyun.jymall.modules.business.score.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.score.entity.YjCash;
import com.songlanyun.jymall.modules.business.score.repository.YjCashRepository;
import com.songlanyun.jymall.modules.business.score.service.YjCashService;
import org.springframework.stereotype.Service;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:27
 */
@Service
public class YjCashServiceImpl extends BaseServiceImpl<YjCash, Integer, YjCashRepository> implements YjCashService {
    public YjCashServiceImpl(YjCashRepository repository) {
        super(repository);
    }

    @Override
    public void updateCheckStatus(YjCash yjCash) {
        //Integer opterId, Date opterDate, Integer status, String remark, Integer id
        this.repository.updateCheckStatus(yjCash.getOpterId(),yjCash.getOpterDate(),yjCash.getStatus(),yjCash.getRemark(),yjCash.getId());
    }
}
