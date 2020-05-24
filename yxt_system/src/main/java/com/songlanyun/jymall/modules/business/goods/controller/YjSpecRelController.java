package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecRel;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 商品与规格值关系记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("goods/yjspecrel")
public class YjSpecRelController {
    @Autowired
    private YjSpecRelService yjSpecRelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("goods:yjspecrel:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = yjSpecRelService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("goods:yjspecrel:info")
    public R info(@PathVariable("id") Integer id){
        YjSpecRel yjSpecRel = yjSpecRelService.findById(id);

        return R.ok().put("yjSpecRel", yjSpecRel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("goods:yjspecrel:save")
    public R save(@RequestBody YjSpecRel yjSpecRel){
        yjSpecRelService.save(yjSpecRel);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("goods:yjspecrel:update")
    public R update(@RequestBody YjSpecRel yjSpecRel){
        ValidatorUtils.validateEntity(yjSpecRel);
        yjSpecRelService.save(yjSpecRel);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    //@RequiresPermissions("goods:yjspecrel:delete")
    public R delete(@RequestBody Integer[] ids){
        yjSpecRelService.deleteByIds(ids);

        return R.ok();
    }

}
