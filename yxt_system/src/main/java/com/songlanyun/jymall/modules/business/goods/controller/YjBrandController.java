package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjBrand;
import com.songlanyun.jymall.modules.business.goods.service.YjBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 品牌管理
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("goods/yjbrand")
public class YjBrandController {
    @Autowired
    private YjBrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
     public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{categoryId}")
    //@RequiresPermissions("goods:YjBrand:info")
    public R info(@PathVariable("brandId") Integer categoryId) {
        YjBrand YjBrand = brandService.findById(categoryId);
        return R.ok().put("yjBrand", YjBrand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("goods:YjBrand:save")
    public R save(@RequestBody YjBrand YjBrand) {
        Map param=new HashMap();
        //判断品牌名称是否已存在 
        param.put( "name",YjBrand.getName());
        long nameCnt = brandService.count(param);
        if (nameCnt>0){
            return R.error(StatusMsgEnum.ADD_REPEAT);
        }
         brandService.save(YjBrand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("goods:YjBrand:update")
    public R update(@RequestBody YjBrand YjBrand) {
        ValidatorUtils.validateEntity(YjBrand);
        brandService.save(YjBrand);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    //@RequiresPermissions("goods:YjBrand:delete")
    public R delete(@PathVariable("id") Integer id) {
        brandService.deleteById(id);
        return R.ok();
    }

}
