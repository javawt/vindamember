package com.songlanyun.jymall.modules.business.sys.controller;


import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import com.songlanyun.jymall.modules.business.sys.service.SysAdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 后台新增接口类
 */
@RestController
@RequestMapping("/sysAdmin")
public class SysAdminController {

    @Resource
    private SysAdminService sysAdminService;

    /**
     * 根据活动id 查询活动下面的商品
     */
    @PostMapping("/getGoodsByActId")
    public R getGoodsByActId(@RequestBody Statistical statistical) {
        return sysAdminService.getGoodsByActId(statistical);
    }

    /**
     * 根据活动id 查询活动下面的商品
     */
    @PostMapping("/deleteActGoods")
    public R deleteActGoods(@RequestBody Integer[] goods) {
        return sysAdminService.deleteActGoods(goods);
    }

    /**
     * app退货地址保存
     */
    @PostMapping("/appReturnGoods")
    public R appReturnGoods(@RequestBody Statistical statistical){
        return sysAdminService.appReturnGoods(statistical);
    }

    /**
     * 活动下拉选
     */
    @PostMapping("/activitySelectList")
    public R activitySelectList(){
        return sysAdminService.activitySelectList();
    }

    /**
     * 个人升降级记录列表
     */
    @PostMapping("/personUpgrade")
    public R personUpgrade(@RequestBody Statistical statistical){
        return sysAdminService.personUpgrade(statistical.getUid());
    }

}
