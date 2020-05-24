package com.songlanyun.jymall.modules.shop.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopServerMechanicInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopServerMechanicInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 店铺服务技工信息表(TbShopServerMechanicInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-22 10:19:35
 */
@RestController
@RequestMapping("tbShopServerMechanicInfo")
public class TbShopServerMechanicInfoController {
    /**
     * 服务对象
     */
    @Resource
    private TbShopServerMechanicInfoService tbShopServerMechanicInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public TbShopServerMechanicInfo selectOne(Long id) {
        return this.tbShopServerMechanicInfoService.queryById(id);
    }

    @PostMapping("/select")
    public R select(@RequestBody TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        return this.tbShopServerMechanicInfoService.select(tbShopServerMechanicInfo);
    }

    @PostMapping("save")
    public R save(@RequestBody TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        return this.tbShopServerMechanicInfoService.save(tbShopServerMechanicInfo);
    }

    @PostMapping("update")
    public R update(@RequestBody TbShopServerMechanicInfo tbShopServerMechanicInfo) {
        return this.tbShopServerMechanicInfoService.update(tbShopServerMechanicInfo);
    }

    @PostMapping("/delete/{id}")
    public R delete(@PathVariable("id") Long id){
        return this.tbShopServerMechanicInfoService.deleteById(id);
    }

}