package com.songlanyun.jymall.modules.business.jysys.bank.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.bank.entity.BankBind;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by zenghang on 2019/11/22.
 */
@Repository
public interface BankBindRepository extends BaseRepository<BankBind, Integer> {
    @Override
    @Modifying
    @Transactional
    @Query("update BankBind a set a.isDelete = 1 where a.id =?1")
    void delete(BankBind bankBind);

    @Override
    @Modifying
    @Transactional
    //@Query("update BankBind a set a.isDelete = 1 where a.id = ?1")
    @Query("DELETE FROM BankBind WHERE id= ?1")
    void deleteById(Integer id);

}
