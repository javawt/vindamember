package com.songlanyun.jymall.modules.business.oss.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadFile;

import java.util.List;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
public interface UploadFileService extends BaseService<YjUploadFile, Integer> {
   /** 删除分组图片 **/
    void deleteByGrpId(Integer integer);
    /** 按分组搜索其下图片  **/
    List<YjUploadFile> getImgList(int grpId);
    /** 将ids 的图片移到另一个分组  **/
      void moveGrpByIds(int grpId, List<Integer> ids);
}

