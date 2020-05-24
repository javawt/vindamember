package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.Express;
import org.springframework.stereotype.Repository;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:33
 */
@Repository
public interface ExpressRepository extends BaseRepository<Express, Integer> {
}
