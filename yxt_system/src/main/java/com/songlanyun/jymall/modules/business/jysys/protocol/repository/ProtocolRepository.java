package com.songlanyun.jymall.modules.business.jysys.protocol.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.protocol.entity.Protocol;
import org.springframework.stereotype.Repository;

/**
 * Created by zenghang on 2019/11/22.
 */
@Repository
public interface ProtocolRepository extends BaseRepository<Protocol, Integer> {
}
