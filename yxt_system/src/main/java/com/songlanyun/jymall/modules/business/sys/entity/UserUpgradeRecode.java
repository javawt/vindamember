package com.songlanyun.jymall.modules.business.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserUpgradeRecode implements Serializable {
    private Long id;

    private Long userId;

    private Integer beforeGrade;

    private Integer afterGrade;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer upgradeType;
}
