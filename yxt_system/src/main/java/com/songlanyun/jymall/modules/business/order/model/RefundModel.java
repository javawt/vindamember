package com.songlanyun.jymall.modules.business.order.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zenghang.
 * Date: 2019/12/19
 * Time: 13:58
 * <p>
 * 退款模型
 */
@Data
public class RefundModel {

    /**
     * 支付类型
     */
    private Integer payType;

    /**
     * 订单号
     */
    private String tradeNo;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款描述
     */
    private String refundReason = "商品退款";

    /**
     * 订单ID
     */
    private String orderNo;
}
