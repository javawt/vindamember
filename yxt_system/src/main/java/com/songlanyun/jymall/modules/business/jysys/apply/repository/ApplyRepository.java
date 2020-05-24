package com.songlanyun.jymall.modules.business.jysys.apply.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.apply.entity.Apply;
import org.springframework.stereotype.Repository;


/**
 * Created by tsh.
 * Date: 2019/12/7
 * Time: 14:26
 */
@Repository
public interface ApplyRepository extends BaseRepository<Apply, Integer> {

    int countByApplyAccount(String applyAccount);

}
