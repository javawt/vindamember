package com.songlanyun.jymall.modules.business.jysys.uploadfile.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.entity.UploadFile;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:27
 */
public interface UploadFilesService extends BaseService<UploadFile, Integer> {

    public  String getImg(int imgId);

}
