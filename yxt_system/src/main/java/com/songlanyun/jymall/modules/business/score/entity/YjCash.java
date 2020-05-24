package com.songlanyun.jymall.modules.business.score.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现明细表
 */
@Table(name = "yj_cash")
@Entity
@Data
public class YjCash implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;


    /**
     * 申请提现金额
     */
    @Column(name = "money")
    private BigDecimal money;

    private String opterId; //审核人id


    /**
     * 申请时间
     */
    @Column(name = "create_date")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date opterDate;

    private Integer status; //0---提交申请 1---审核通过 2---审核拒绝

    private String remark; //拒绝理由

    private String idCard; //卡号

    private String cardPwd; //密码

    private String cashType; //提现方式 0:支付宝 1：银行卡

    @Transient
    private String username;//申请人名称
    @Transient
    private String optername;//操作人名称

    private String tax;

    private BigDecimal taxMoney;

}