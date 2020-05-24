package com.songlanyun.jymall.modules.business.order.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zenghang.
 * Date: 2019/12/6
 * Time: 16:07
 * 发货的模型
 */
@Data
public class DeliverModel {
    /**
     * 订单ID
     */
    private String orderNo;

    /**
     * 物流ID
     */
    private Integer expressId;

    /**
     * 物流单号
     */
    private String expressNo;

    /**
     * 运费金额
     */
    private BigDecimal expressPrice;


}
