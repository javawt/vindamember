package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecValue;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 商品规格值记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("goods/yjspecvalue")
public class YjSpecValueController {
    @Autowired
    private YjSpecValueService yjSpecValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("goods:yjspecvalue:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = yjSpecValueService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{specValueId}")
    //@RequiresPermissions("goods:yjspecvalue:info")
    public R info(@PathVariable("specValueId") Integer specValueId){
        YjSpecValue yjSpecValue = yjSpecValueService.findById(specValueId);

        return R.ok().put("yjSpecValue", yjSpecValue);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("goods:yjspecvalue:save")
    public R save(@RequestBody YjSpecValue yjSpecValue){
        yjSpecValueService.save(yjSpecValue);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("goods:yjspecvalue:update")
    public R update(@RequestBody YjSpecValue yjSpecValue){
        ValidatorUtils.validateEntity(yjSpecValue);
        yjSpecValueService.save(yjSpecValue);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    //@RequiresPermissions("goods:yjspecvalue:delete")
    public R delete(@RequestBody Integer[] specValueIds){
        yjSpecValueService.deleteByIds(specValueIds);

        return R.ok();
    }

}
