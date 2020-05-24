package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class YjSpecRelValue implements Serializable {
    private static final long serialVersionUID = 1L;
    private String specName;
    private String specValue;
    private Integer specId;
    /**
     * 小程序id
     */
    private Integer wxappId;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
