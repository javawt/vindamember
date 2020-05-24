package com.songlanyun.jymall.modules.business.goods.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpec;
import org.springframework.stereotype.Repository;

/**
 * 商品规格组记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Repository
public interface YjSpecRepository  extends BaseRepository<YjSpec, Integer> {

}
