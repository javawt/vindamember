package com.songlanyun.jymall.modules.business.jysys.uploadfile.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.entity.UploadFile;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.repository.UploadFileRepository;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.service.UploadFilesService;
import org.springframework.stereotype.Service;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:27
 */
@Service
public class UploadFilesServiceImpl extends BaseServiceImpl<UploadFile, Integer, UploadFileRepository> implements UploadFilesService {
    public UploadFilesServiceImpl(UploadFileRepository repository) {
        super(repository);
    }
    @Override
    public  String getImg(int imgId){

        return  "";
    }
}
