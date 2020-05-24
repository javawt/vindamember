/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.oss.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadFile;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadGroup;
import com.songlanyun.jymall.modules.business.oss.service.UploadFileService;
import com.songlanyun.jymall.modules.business.oss.service.UploadGroupService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/upload")
public class OssUploadController {
    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private UploadGroupService uploadGroupService;

    /**
     * 列表
     */
    @GetMapping("/grpList")
    public R grpList() {
        List<YjUploadGroup> page = uploadGroupService.findAll(new Sort(Sort.Direction.ASC, "sort"));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/getImgList", method = {RequestMethod.POST})
    public R getImgList(@RequestBody Integer grpId) {
        List<YjUploadFile> page = uploadFileService.getImgList(grpId);
        return R.ok().put("data", page);
    }

    /**
     * 删除某分组及下面的图片
     */
    @RequestMapping(value = "/grpDel", method = {RequestMethod.POST})
    public R grpDel(@RequestBody int grpId) {
        try {
            uploadGroupService.deleteByGroupId(grpId);
            uploadFileService.deleteByGrpId(grpId);
            return R.ok(StatusMsgEnum.SUCCESS);
        } catch (Exception e) {
            return R.error(StatusMsgEnum.FAIL);
        }
    }

    /**
     * 新增或保存 uploadGroup
     **/
    @RequestMapping(value = "/grpUpdate", method = {RequestMethod.POST})
    public R grpUpdate(@RequestBody YjUploadGroup _uploadVo) {
        //组名不能为空
        if (StringUtils.isBlank(_uploadVo.getGroupName())) {
            return R.error(StatusMsgEnum.OSS_GROUP_NAME_NEED);
        }
        //是新增，则判断是否重名
        if (_uploadVo.getGroupId() == null) {
            YjUploadGroup uploadVo = uploadGroupService.findByGroupName(_uploadVo.getGroupName());
            if (uploadVo != null)
                return R.error(StatusMsgEnum.OSS_GROUP_EXISTS);
        }
        try {
            uploadGroupService.save(_uploadVo);
            return R.ok(StatusMsgEnum.SUCCESS);
        } catch (Exception e) {
            return R.error(StatusMsgEnum.FAIL);
        }
    }

    /*** 移动 ids 到另一个 groupid **/
    @RequestMapping(value = "/moveGrpIds", method = {RequestMethod.POST})
    public R moveGrpIds(@RequestBody Map<String,Object> param) {
        String ids=param.get("ids").toString();
        int grpId=Integer.parseInt(param.get("grpId").toString());
        String[] stuList = ids.split(",");
        List<Integer> LString = new ArrayList<Integer>();
        for (String str : stuList) {
            LString.add(new Integer(str));
        }
        List<Integer> idsArr = new ArrayList<Integer>(LString);
        try {
            uploadFileService.moveGrpByIds(grpId,idsArr);
            return R.ok(StatusMsgEnum.SUCCESS);
        } catch (Exception e) {
            return R.error(StatusMsgEnum.FAIL);
        }

    }

     /*** 根据文件 id 批量删除文件 **/
    @RequestMapping(value = "/delFileByIds", method = {RequestMethod.POST})
    public R delFileByIds(@RequestBody String ids) {
        String[] stuList = ids.split(",");
        // 将字符串数组转为List<Intger> 类型
        List<Integer> LString = new ArrayList<Integer>();
        for (String str : stuList) {
            LString.add(new Integer(str));
        }
        Integer[] arrString = LString.toArray(new Integer[LString.size()]);
        try {
            uploadFileService.deleteByIds(arrString);
            return R.ok(StatusMsgEnum.SUCCESS);
        } catch (Exception e) {
            return R.error(StatusMsgEnum.FAIL);
        }
    }


}
