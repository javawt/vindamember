package com.songlanyun.jymall.modules.business.score.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.score.entity.YjCash;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface YjCashRepository extends BaseRepository<YjCash, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE YjCash a SET a.opterId= ?1,a.opterDate= ?2,a.status= ?3,a.remark= ?4 WHERE a.id= ?5")
    void updateCheckStatus(String opterId, Date opterDate, Integer status, String remark, Integer id);

}