package com.songlanyun.jymall.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.*;

import lombok.Data;

/**
 * 平台和商家的服务
 */
@Data
@Table(name = "yj_server_goods")
@Entity
public class ServerGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @Id
    @Column(name = "id",  nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    /**
     * 审核不通过描述
     */
    @Column(name = "chk_desc")
    private String chkDesc;

    @Column(name = "srv_title")
    private String srvTitle;



    /**
     * 审核时间
     */
    @Column(name = "chk_time")
    private Timestamp chkTime;

    /**
     * 审核人
     */
    @Column(name = "chk_uid")
    private Long chkUid;

    /**
     * 服务成本价
     */
    @Column(name = "cost")
    private BigDecimal cost = BigDecimal.ZERO;

    /**
     * 上架时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 上架人id
     */
    @Column(name = "create_userid")
    private Long createUserid;


    /**
     * 服务图片地址
     */
    @Column(name = "imgs")
    private String imgs;

    /**
     * 服务划线价
     */
    @Column(name = "off_price")
    private BigDecimal offPrice = BigDecimal.ZERO;

    /**
     * 服务价格
     */
    @Column(name = "price")
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * 店铺id  -1表示是平台
     */
    @Column(name = "shop_id")
    private Long shopId = -1L;

    /**
     * 服务描述
     */
    @Column(name = "srv_desc")
    private String srvDesc;

    /**
     * 此服务时间段内服务最大人数
     */
    @Column(name = "srv_max")
    private Integer srvMax;

    /**
     * 服务排序号---可能由后台设置
     */
    @Column(name = "srv_no")
    private Integer srvNo = 100;

    /**
     * 服务项目类型id
     */
    @Column(name = "srv_pid")
    private Long srvPid;

    /**
     * 服务项目名称
     */
    @Column(name = "srv_pname")
    private String srvPname;

    /**
     * 服务标签
     */
    @Column(name = "srv_tag")
    private String srvTag;

    /**
     * 10--商家的服务  20--平台的服务
     */
    @Column(name = "srv_type")
    private Integer srvType;


    /**
     * 状态 商品状态(10上架 20下架)

     */
    @Column(name = "status")
    private Integer status;

    /**
     * 累计销售数量
     */
    @Column(name = "sum_count")
    private BigDecimal sumCount;

    /**
     * 累计销售金额
     */
    @Column(name = "sum_money")
    private BigDecimal sumMoney;

    /**
     * 下架理由
     */
    @Column(name = "ud_desc")
    private String udDesc;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Timestamp updateTime;

    
}