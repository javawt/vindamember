package com.songlanyun.jymall.modules.shop.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 店铺信息表(TbShopInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-12 11:21:27
 */
@RestController
@RequestMapping("/tbShopInfo")
public class TbShopInfoController {
    /**
     * 服务对象
     */
    @Resource
    private TbShopInfoService tbShopInfoService;

    /**
     * 保存店铺信息
     */
    @PostMapping("/save")
    public R save(@RequestBody TbShopInfo tbShopInfo) {
        R r=new R();
        this.tbShopInfoService.saveInfo(tbShopInfo);
        return r;
    }

}