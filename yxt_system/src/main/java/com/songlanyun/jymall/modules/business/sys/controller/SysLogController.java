/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import com.songlanyun.jymall.modules.business.sys.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;


/**
 * 系统日志
 *
 * @author Mark sunlightcs@gmail.com
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	@Resource
	private StatisticalService statisticalService;
	
	/**
	 * 列表
	 */
	/*@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);

		return R.ok().put("page", page);
	}*/

	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		return statisticalService.getLogList(params);
	}
	
}
