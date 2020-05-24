package com.songlanyun.jymall.modules.business.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PaymentSummary implements Serializable {
    private Long id;

    private Long userId;

    private Long relationId;

    private int paymentType;

    private String orderNo;

    private int operaType;

    private BigDecimal money;

    private BigDecimal beforeMoney;

    private BigDecimal afterMoney;

    private int payType;

    @JsonFormat(pattern = "yy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    private String remark;
}
