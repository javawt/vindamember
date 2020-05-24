package com.songlanyun.jymall.modules.business.score.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CashStatistical implements Serializable {
    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String startDate;

    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String endDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDates;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDates;

    private int page;

    private int size;

    private Long userId;

    private String accountName;

    private String did;//供应商id

    private String ostatus;//订单状态

    private String userName;

    private String orderType;

    private String orderNo;

    private int offset;

    private String status;//提现状态

}
