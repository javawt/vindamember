package com.songlanyun.jymall.modules.business.score.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderEntityBean {

    /**
     * 推荐人
     **/
    private int userid;

    /**
     * 订单金额
     **/
    private BigDecimal money=BigDecimal.ZERO;

}
