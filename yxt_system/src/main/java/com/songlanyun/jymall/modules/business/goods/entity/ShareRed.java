package com.songlanyun.jymall.modules.business.goods.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 领取红包参数模型
 */
@Data
public class ShareRed implements Serializable {

    private String orderNo;

    private Double money;
}
