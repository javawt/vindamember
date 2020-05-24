package com.songlanyun.jymall.repository;

import com.songlanyun.jymall.domain.ServerTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ServerTimeRepository extends JpaRepository<ServerTime, Long>, JpaSpecificationExecutor<ServerTime> {
   List<ServerTime> findAllByShopIdAndSrvIdOrderByStartTime(long shopId,long srvId);
}