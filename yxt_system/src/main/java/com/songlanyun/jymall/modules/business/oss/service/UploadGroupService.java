package com.songlanyun.jymall.modules.business.oss.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadGroup;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface UploadGroupService extends BaseService<YjUploadGroup, Integer> {
    YjUploadGroup findByGroupName(String grpName);
    void deleteByGroupId(int grpid);
}

