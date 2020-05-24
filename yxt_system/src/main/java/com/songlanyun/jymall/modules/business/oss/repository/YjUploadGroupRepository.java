package com.songlanyun.jymall.modules.business.oss.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadGroup;

public interface YjUploadGroupRepository  extends BaseRepository<YjUploadGroup, Integer> {
   YjUploadGroup findByGroupName(String grpName);


   void deleteByGroupId(Integer grpId);
}