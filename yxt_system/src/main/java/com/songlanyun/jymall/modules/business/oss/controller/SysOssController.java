/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.oss.controller;

import com.google.gson.Gson;
import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.common.validator.group.AliyunGroup;
import com.songlanyun.jymall.common.validator.group.QcloudGroup;
import com.songlanyun.jymall.common.validator.group.QiniuGroup;
import com.songlanyun.jymall.modules.business.oss.cloud.CloudStorageConfig;
import com.songlanyun.jymall.modules.business.oss.cloud.OSSFactory;
import com.songlanyun.jymall.modules.business.oss.entity.SysOssEntity;
import com.songlanyun.jymall.modules.business.oss.entity.YjUploadFile;
import com.songlanyun.jymall.modules.business.oss.service.SysOssService;
import com.songlanyun.jymall.modules.business.oss.service.UploadFileService;
import com.songlanyun.jymall.modules.business.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;

	@Autowired
	private UploadFileService uploadFileService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
//	@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return R.ok().put("page", page);
	}


    /**
     * 云存储配置信息
     */
    @GetMapping("/config")
//    @RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


	/**
	 * 保存云存储配置信息
	 */
	@PostMapping("/saveConfig")
//	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}
	

	/**
	 * 上传文件
	 */
	@PostMapping("/upload")
//	@RequiresPermissions("sys:oss:all")
	public R upload(@RequestParam("file") MultipartFile file,@RequestParam("paramValue") String paramvalue) throws Exception {

		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		if (paramvalue==null){
		   return R.error(StatusMsgEnum.UPLOAD_PARAM_NEED);
		}

		int grpId=Integer.parseInt(paramvalue);
		//上传文件
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
		String uploadFile=url.substring(url.lastIndexOf("/")+1);
		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);
		//保存到上传文件列表中，做图片集
		YjUploadFile fileVo=new YjUploadFile();
		 fileVo.setCreateTime(new Date());
		fileVo.setFileName(uploadFile);
		fileVo.setFileUrl(url);
		fileVo.setFileSize(file.getSize());
		fileVo.setExtension(suffix);
		fileVo.setGroupId(grpId);
		try {
			YjUploadFile uploadEdVo=uploadFileService.save(fileVo);

			return R.ok().put("fileVo",uploadEdVo);
		}catch (RRException exp){
			return R.error(StatusMsgEnum.FAIL);
		}

	}




	/**
	 * 删除
	 */
	@PostMapping("/delete")
//	@RequiresPermissions("sys:oss:all")
	public R delete(@RequestBody Long[] ids){
		sysOssService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
