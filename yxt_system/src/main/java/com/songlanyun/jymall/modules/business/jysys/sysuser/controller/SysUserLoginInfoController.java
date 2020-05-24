package com.songlanyun.jymall.modules.business.jysys.sysuser.controller;


import com.songlanyun.jymall.modules.business.jysys.sysuser.service.SysUserLoginInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统用户登录信息
 */
@RestController
@RequestMapping("/sysUserLoginInfo")
public class SysUserLoginInfoController {

    @Resource
    private SysUserLoginInfoService sysUserLoginInfoService;

    /**
     * 根据活动id 查询活动下面的商品
     */
    /*@PostMapping("/getGoodsByActId")
    public R getGoodsByActId(@RequestBody Statistical statistical) {
        return sysUserLoginInfoService.getGoodsByActId(statistical);
    }*/



}
