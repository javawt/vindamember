package com.songlanyun.jymall.modules.business.jysys.address.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.jysys.address.entity.Address;
import com.songlanyun.jymall.modules.business.jysys.address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/12/2
 * Time: 14:28
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService addressService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        if (!ShiroUtils.getUserId().equals("1")) {
            param.put("userId", ShiroUtils.getUserId());
        }

        PageUtils pageUtils = addressService.queryPage(param);
        return R.ok().put("data", pageUtils);
    }

    @GetMapping("/listNoPage")
    public R listNoPage(@RequestParam Map<String, Object> param) {
        if (!ShiroUtils.getUserId().equals("1")) {
            param.put("userId", ShiroUtils.getUserId());
        }
        return R.ok().put("data", addressService.findAll(param));
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", addressService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Address address) {
        if (address.getAddressId() == null) {
            address.setCreateTime(new Date());
            address.setUserId(ShiroUtils.getUserId());
            address.setUpdateTime(new Date());
        } else {
            address.setUpdateTime(new Date());
        }
        if (address.getIsDefault() == 1) {
            addressService.updateDefaultAddressBack(address);
            addressService.save(address);
        } else {
            addressService.save(address);
        }
        return R.ok("保存成功");
    }

    /**
     * 设置默认地址,将该用户其他地址设置为非默认
     */
    @PostMapping("/updateDefaultAddress")
    public R updateDefaultAddress(@RequestBody Address address) {
        address.setUserId(ShiroUtils.getUserId());
        addressService.updateDefaultAddress(address);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        addressService.deleteById(id);
        return R.ok("删除成功");
    }
}
