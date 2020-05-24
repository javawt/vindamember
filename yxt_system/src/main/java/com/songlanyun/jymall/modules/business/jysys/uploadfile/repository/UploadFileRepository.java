package com.songlanyun.jymall.modules.business.jysys.uploadfile.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.entity.UploadFile;
import org.springframework.stereotype.Repository;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:26
 */
@Repository
public interface UploadFileRepository extends BaseRepository<UploadFile, Integer> {

}
