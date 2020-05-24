package com.songlanyun.jymall.modules.business.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class Statistical implements Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    private int page;

    private int size;

    private int activityId;

    private String orderNo;

    private String expressNo;//退货物流单号

    private String userId;

    private String uid;

    private Integer dtype;
}
