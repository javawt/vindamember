package com.songlanyun.jymall.modules.api.sys.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.banner.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by zenghang on 2019/11/22.
 */
@RestController
@RequestMapping("/api/banner")
public class BannerApiController {
    @Autowired
    private BannerService bannerService;

    /**
     * 列表
     */
    @GetMapping("/lists")
    public R list(@RequestParam Map<String, Object> param) {
        param.put("isDelete", 0);
        return R.ok().put("data", bannerService.findAll(param));
    }

}
