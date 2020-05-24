package com.songlanyun.jymall.modules.business.jysys.protocol.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.protocol.entity.Protocol;
import com.songlanyun.jymall.modules.business.jysys.protocol.repository.ProtocolRepository;
import com.songlanyun.jymall.modules.business.jysys.protocol.service.ProtocolService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang on 2019/11/22.
 */
@Service
public class ProtocolServiceImpl extends BaseServiceImpl<Protocol, Integer, ProtocolRepository> implements ProtocolService {
    public ProtocolServiceImpl(ProtocolRepository repository) {
        super(repository);
    }
}
