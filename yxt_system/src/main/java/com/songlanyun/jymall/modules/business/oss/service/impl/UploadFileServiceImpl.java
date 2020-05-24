package com.songlanyun.jymall.modules.business.oss.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadFile;
import com.songlanyun.jymall.modules.business.oss.repository.YjUploadFileRepository;
import com.songlanyun.jymall.modules.business.oss.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */

@Service
public class UploadFileServiceImpl extends BaseServiceImpl<YjUploadFile, Integer, YjUploadFileRepository> implements UploadFileService {
    public UploadFileServiceImpl(YjUploadFileRepository repository) {
        super(repository);
    }
    @Autowired
    YjUploadFileRepository yjUploadFileRepository;
    @Override
    @Transactional
    public void deleteByGrpId(Integer grpId) {
        yjUploadFileRepository.deleteByGroupId(grpId);
    }

    @Override
    public  List<YjUploadFile> getImgList(int grpId){
        return  yjUploadFileRepository.findAllByGroupId(grpId);
    }

    @Transactional
    public void deleteByIds(Integer grpId) {
        yjUploadFileRepository.deleteByGroupId(grpId);
    }

    public void moveGrpByIds(int grpId,List<Integer> ids){
        yjUploadFileRepository.moveGrpByIds(grpId,ids);
    }


}

