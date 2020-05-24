package com.songlanyun.jymall.modules.business.sys.controller;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.entity.UserDetail;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ReturnsDetailed")
public class ReturnsDetailedController {

    @Resource
    private StatisticalService statisticalService;

    /**
     * yj_wholesa_log 寄售收益
     * yj_transfer  转账
     * yj_cash  提现
     * yj_money 分红
     * yj_money_team 团队收益
     *
     * 易货币 t_easy_money_recode
     * 支付记录表 yj_order_recharge
     */
    @RequestMapping("/userReturnDetai")
    public R userReturnDetai(UserDetail userDetail) {
        return statisticalService.userReturnDetai(userDetail);
    }


    /**
     * 转账明细
     */
    @RequestMapping("/userTransferDetai")
    public R userTransferDetai(UserDetail userDetail) {
        return statisticalService.userTransferDetai(userDetail);
    }

    /**
     * 提现明细
     */
    @RequestMapping("/userCashDetai")
    public R userCashDetai(UserDetail userDetail) {
        return statisticalService.userCashDetai(userDetail);
    }

}
