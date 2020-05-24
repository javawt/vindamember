package com.songlanyun.jymall.modules.business.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUserAdvice implements Serializable {
    private Long adviceId;

    private String adviceType;

    private String contact;

    private String createBy;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String adviceContent;

    private int page;

    private int size;

}
