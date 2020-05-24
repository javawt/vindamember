package com.songlanyun.jymall.modules.shop.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopClerkInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopClerkInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 店铺店员信息表(TbShopClerkInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-22 16:17:31
 */
@RestController
@RequestMapping("/tbShopClerkInfo")
public class TbShopClerkInfoController {
    /**
     * 服务对象
     */
    @Resource
    private TbShopClerkInfoService tbShopClerkInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public TbShopClerkInfo selectOne(Long id) {
        return this.tbShopClerkInfoService.queryById(id);
    }

    @PostMapping("/select")
    public R select(@RequestBody TbShopClerkInfo TbShopClerkInfo) {
        return this.tbShopClerkInfoService.select(TbShopClerkInfo);
    }

    @PostMapping("save")
    public R save(@RequestBody TbShopClerkInfo TbShopClerkInfo) {
        return this.tbShopClerkInfoService.save(TbShopClerkInfo);
    }

    @PostMapping("update")
    public R update(@RequestBody TbShopClerkInfo TbShopClerkInfo) {
        return this.tbShopClerkInfoService.update(TbShopClerkInfo);
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id){
        return this.tbShopClerkInfoService.deleteById(id);
    }

}