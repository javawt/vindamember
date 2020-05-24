package com.songlanyun.jymall.modules.business.oss.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface YjUploadFileRepository extends BaseRepository<YjUploadFile, Integer> {
    void deleteByGroupId(Integer integer);

    List<YjUploadFile> findAllByGroupId(int grpId);

    /**
     * 将图片批量移到另一个分类中
     **/
    @Modifying
    @Transactional
    @Query(value="update yj_upload_file sc set sc.group_id = :targetGrpId where sc.file_id in :ids",nativeQuery = true)
   public void moveGrpByIds(@Param(value = "targetGrpId") int targetGrpId, @Param(value = "ids") List<Integer> ids);
}