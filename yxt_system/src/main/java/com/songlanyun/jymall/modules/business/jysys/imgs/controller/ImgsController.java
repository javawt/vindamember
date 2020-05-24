package com.songlanyun.jymall.modules.business.jysys.imgs.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.imgs.entity.Imgs;
import com.songlanyun.jymall.modules.business.jysys.imgs.service.ImgsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zenghang on 2019/11/21.
 */
@RestController
@RequestMapping("imgs")
public class ImgsController {
    @Autowired
    private ImgsService imgsService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = imgsService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", imgsService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Imgs imgs) {
        imgsService.save(imgs);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        imgsService.deleteById(id);
        return R.ok("删除成功");
    }
}
