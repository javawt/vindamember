package com.songlanyun.jymall.modules.business.order.model;

import lombok.Data;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/12/6
 * Time: 15:35
 * 用户付款模型
 */
@Data
public class PaymentModel {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付方式
     */
    private Integer payType;//10余额支付 20微信支付 30 支付宝支付 40易货币

    /**
     * 支付密码
     */
    private String password;

    /**
     * 订单号
     */
    private String orderNo;

    private List<String> orderList;

    private String callbackNo;
}
