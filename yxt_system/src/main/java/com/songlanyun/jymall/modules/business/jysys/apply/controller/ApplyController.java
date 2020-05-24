package com.songlanyun.jymall.modules.business.jysys.apply.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.common.utils.SpecialSymbols;
import com.songlanyun.jymall.modules.business.jysys.apply.entity.Apply;
import com.songlanyun.jymall.modules.business.jysys.apply.service.ApplyService;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        if (!ShiroUtils.getUserId().equals("1")) {
            param.put("userId", ShiroUtils.getUserId());
        }

        PageUtils pageUtils = applyService.queryPage(param);
        return R.ok().put("data", pageUtils);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", applyService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Apply apply) {
        if (null != apply) {
            //参数校验
            if (StringUtils.isBlank(apply.getUserName())) {
                return R.error("用户名不能为空");
            }
            if (StringUtils.isNotBlank(apply.getApplyAccount())) {
                if (!SpecialSymbols.isMobile(apply.getApplyAccount())) {
                    return R.error("支付宝账号格式不正确");
                }
            } else {
                return R.error("支付宝账号不能为空");
            }
            Integer applyInfo=applyService.isExitIdCard(apply.getApplyAccount());
            System.out.println(apply.getApplyAccount()+"----"+applyInfo);
            if(applyInfo>0){
                return R.error("该支付宝账户已被绑定");
            }else{
                if (apply.getId() == null) {
                    apply.setUserId(ShiroUtils.getUserId());
                    apply.setUpdateTime(new Date());
                } else {
                    apply.setUpdateTime(new Date());
                }
                applyService.save(apply);
                return R.ok("保存成功");
            }

        } else {
            return R.error("参数不能为空");
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        applyService.deleteById(id);
        return R.ok("删除成功");
    }
}
