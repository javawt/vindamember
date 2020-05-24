package com.songlanyun.jymall.modules.business.goods.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.goods.entity.YjCategory;

import java.util.List;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface YjCategoryService  extends BaseService<YjCategory, Integer> {
    List<YjCategory> findAllByParentId(Integer parentId);

}

