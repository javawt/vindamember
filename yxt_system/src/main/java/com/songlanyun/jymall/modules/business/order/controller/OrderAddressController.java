package com.songlanyun.jymall.modules.business.order.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.order.entity.OrderAddress;
import com.songlanyun.jymall.modules.business.order.service.OrderAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:13
 */
@RestController
@RequestMapping("/order/address")
public class OrderAddressController {
    @Autowired
    private OrderAddressService orderAddressService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderAddressService.queryPage(params);
        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", orderAddressService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderAddress orderAddress) {
        orderAddressService.save(orderAddress);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        orderAddressService.deleteById(id);
        return R.ok("删除成功");
    }

}
