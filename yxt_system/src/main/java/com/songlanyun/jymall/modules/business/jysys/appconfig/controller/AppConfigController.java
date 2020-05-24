package com.songlanyun.jymall.modules.business.jysys.appconfig.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.appconfig.entity.AppConfig;
import com.songlanyun.jymall.modules.business.jysys.appconfig.service.AppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Created by tsh.
 * Date: 2019/12/2
 * Time: 14:28
 */
@RestController
@RequestMapping("/appConfig")
public class AppConfigController {
    @Autowired
    private AppConfigService appConfigService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
//        if (ShiroUtils.getUserId() != 1) {
//            param.put("userId", ShiroUtils.getUserId());
//        }
        PageUtils pageUtils = appConfigService.queryPage(param);
        return R.ok().put("data", pageUtils);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", appConfigService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody AppConfig appConfig) {
        if (appConfig.getId() == null) {
            appConfig.setCreateTime(new Date());
            appConfig.setUpdateTime(new Date());
        } else {
            appConfig.setUpdateTime(new Date());
        }
        appConfigService.save(appConfig);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        appConfigService.deleteById(id);
        return R.ok("删除成功");
    }
}
