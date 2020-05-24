package com.songlanyun.jymall.modules.shop.controller;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.shop.entity.TbShopBannerInfo;
import com.songlanyun.jymall.modules.shop.service.TbShopBannerInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 店铺轮播图表(TbShopBannerInfo)表控制层
 *
 * @author makejava
 * @since 2020-05-13 14:05:44
 */
@RestController
@RequestMapping("/tbShopBannerInfo")
public class TbShopBannerInfoController {
    /**
     * 服务对象
     */
    @Resource
    private TbShopBannerInfoService tbShopBannerInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("/selectOne")
    public TbShopBannerInfo selectOne(@RequestBody Long id) {
        return this.tbShopBannerInfoService.queryById(id);
    }

    /**
     * 通过条件查询多条数据
     *
     * @param tbShopBannerInfo
     * @return 多条数据
     */
    @PostMapping("/selectList")
    public R selectList(@RequestBody TbShopBannerInfo tbShopBannerInfo) {
        return this.tbShopBannerInfoService.selectList(tbShopBannerInfo);
    }

    @PostMapping("/save")
    public R save(@RequestBody TbShopBannerInfo tbShopBannerInfo) {
        return this.tbShopBannerInfoService.insert(tbShopBannerInfo);
    }

    @PostMapping("/update")
    public R update(@RequestBody TbShopBannerInfo tbShopBannerInfo) {
        return this.tbShopBannerInfoService.update(tbShopBannerInfo);
    }

    @PostMapping("/delete/{id}")
    public R update(@PathVariable Long id) {
        return this.tbShopBannerInfoService.deleteById(id);
    }


}