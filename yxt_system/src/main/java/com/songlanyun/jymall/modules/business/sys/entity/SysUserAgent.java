package com.songlanyun.jymall.modules.business.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class SysUserAgent implements Serializable {
    private Long id;

    private Long userId;

    private String agentName;

    private String idCard;

    private String idPositive;

    private String idReverse;

    private String payType;//支付类型  (10余额支付 20微信支付 30 -- 支付宝支付

    private  String payStatu; //10 未支付 20 已支付

    private  String orderNo;

    private BigDecimal payPrice;

    private  String cardPwd;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private int page;

    private int size;

    private String status;//0未审核 1审核通过 2审核拒绝

    private  String remark;//拒绝理由

    private Long reviewId;

    @Transient
    private BigDecimal consumSum;

    @Transient
    private SysUserEntity sysUserEntity;

}
