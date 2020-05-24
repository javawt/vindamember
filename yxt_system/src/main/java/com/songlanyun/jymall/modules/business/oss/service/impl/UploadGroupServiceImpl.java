package com.songlanyun.jymall.modules.business.oss.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadGroup;
import com.songlanyun.jymall.modules.business.oss.repository.YjUploadGroupRepository;
import com.songlanyun.jymall.modules.business.oss.service.UploadGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Service

public class UploadGroupServiceImpl extends BaseServiceImpl<YjUploadGroup, Integer, YjUploadGroupRepository> implements UploadGroupService {
   @Autowired
   YjUploadGroupRepository yjUploadGroupRepository;

    public UploadGroupServiceImpl(YjUploadGroupRepository repository) {
        super(repository);
    }

    /** 删除某分组  **/
    @Override
    @Transactional
    public void deleteByGroupId(int grpid){
        yjUploadGroupRepository.deleteByGroupId(grpid);
    }

    @Override
    public YjUploadGroup findByGroupName(String grpName){
       YjUploadGroup uploadVo= yjUploadGroupRepository.findByGroupName(grpName);
       return uploadVo;
    }



}
