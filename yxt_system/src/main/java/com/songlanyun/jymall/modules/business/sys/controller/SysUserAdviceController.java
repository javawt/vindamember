package com.songlanyun.jymall.modules.business.sys.controller;


import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserAdvice;
import com.songlanyun.jymall.modules.business.sys.service.SysUserAdviceService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户意见反馈
 */
@RestController
@RequestMapping("/sysUserAdvice")
public class SysUserAdviceController {

    @Resource
    private SysUserAdviceService sysUserAdviceService;


    @RequestMapping("/addUserAdvice")
    public R addUserAdvice(@RequestBody SysUserAdvice sysUserAdvice) {
        try {
            sysUserAdviceService.addUserAdvice(sysUserAdvice);
        }catch (Exception e){
            R.error("保存失败");
        }
        return R.ok();
    }

    @RequestMapping("/updateUserAdvice")
    public R updateUserAdvice(@RequestBody SysUserAdvice sysUserAdvice) {
        try {
            sysUserAdviceService.updateUserAdvice(sysUserAdvice);
        }catch (Exception e){
            R.error("修改失败");
        }
        return R.ok();
    }


    @RequestMapping("/selectUserAdvice")
    public R selectUserAdvice(@RequestBody SysUserAdvice sysUserAdvice) {
        return sysUserAdviceService.selectUserAdvice(sysUserAdvice);
    }

}
