package com.songlanyun.jymall.modules.shop.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.shop.entity.TbShopConfigInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopConfigInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 店铺配置信息表(TbShopConfigInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-13 10:16:05
 */
@RestController
@RequestMapping("/tbShopConfigInfo")
public class TbShopConfigInfoController {
    /**
     * 服务对象
     */
    @Resource
    private TbShopConfigInfoService tbShopConfigInfoService;


    @PostMapping("save")
    public R save(@RequestBody TbShopConfigInfo tbShopConfigInfo) {
        return this.tbShopConfigInfoService.save(tbShopConfigInfo);
    }

    @PostMapping("update")
    public R update(@RequestBody TbShopConfigInfo tbShopConfigInfo) {
        return this.tbShopConfigInfoService.update(tbShopConfigInfo);
    }

    @PostMapping("/select")
    public R select(@RequestBody TbShopConfigInfo tbShopConfigInfo) {
        return this.tbShopConfigInfoService.select(tbShopConfigInfo);
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id){
        return this.tbShopConfigInfoService.deleteById(id);
    }
}