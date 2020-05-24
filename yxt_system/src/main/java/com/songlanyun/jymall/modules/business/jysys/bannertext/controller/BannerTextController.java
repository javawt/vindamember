package com.songlanyun.jymall.modules.business.jysys.bannertext.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.bannertext.entity.BannerText;
import com.songlanyun.jymall.modules.business.jysys.bannertext.service.BannerTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by zenghang on 2019/11/22.
 */
@RestController
@RequestMapping("/bannertext")
public class BannerTextController {
    @Autowired
    private BannerTextService bannerTextService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        //PageUtils pageUtils = bannerTextService.queryPage(param);
        return R.ok().put("data", bannerTextService.findAll());
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", bannerTextService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody BannerText bannerText) {
        List<BannerText> bannerTextList= bannerTextService.findAll();
        if(null!=bannerTextList && bannerTextList.size()>0){
            //数据库存在记录
            if(bannerTextList.get(0).getId()==bannerText.getId()){
                bannerTextService.save(bannerText);
            }else{
                return R.error("记录已存在，无法新增");
            }
        }else{
            bannerTextService.save(bannerText);
        }
        return R.ok();
    }

    /**
     * 删除
     */
    /*@DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        bannerTextService.deleteById(id);
        return R.ok("删除成功");
    }*/
}
