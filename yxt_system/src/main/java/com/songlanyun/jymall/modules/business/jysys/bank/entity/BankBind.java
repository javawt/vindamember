package com.songlanyun.jymall.modules.business.jysys.bank.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang on 2019/11/22.
 * <p>
 * 银行卡绑定
 */
@Data
@Entity
@Table(name = "jy_bank_bind")
public class BankBind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 开户姓名
     */
    private String realName;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 银行卡号
     */
    private String icCard;

    /**
     * 银行预留手机号
     */
    private String tel;

    /**
     * 诚信认证状态：0，未审核；1，审核通过；2，审核未通过
     */
    private Integer status;

    /**
     * 提交审核时间
     */
    private String applyCheckTime;

    /**
     * 审核结果说明
     */
    private String checkRemark;

    /**
     * 注册时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    /**
     * 状态：0，正常；1，删除
     */
    private Boolean isDelete;

    private String bankAddress;

    private Long shopId;

    private int userType; // 1:用户2：店铺

}
