package com.songlanyun.server.service.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 平台和商家的服务
 */
@Data

public class ServerGoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务成本价
     */
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
     * 服务id
     */
    @Id
    @Column(name = "id", insertable = false, nullable = false)
    private Long id;

    /**
     * 服务图片地址
     */
    private String imgs;



    /**
     * 服务价格
     */

    private BigDecimal price = BigDecimal.ZERO;

    /**
     * 店铺id
     */
    private Long shopId;


    private String srvDesc;

    /**
     * 此服务时间段内服务最大人数
     */
    private Integer srvMax;


    /**
     * 服务项目类型id
     */
    private Long srvPid;
    /**
     * 服务标签
     */
    private String srvTag;

    /**
     * 10--商家的服务  20--平台的服务
     */
    private Integer srvType;

    /**
     * 此服务id--结束时间
     */
    private Timestamp startEdn;

    /**
     * 此服务id-开始时间
     */
    private Timestamp startTime;

    /**
     *   状态(10--待审核  20---审核通过 30--审核不通过 40上架  50下架)
     * 服务就这几种状态，是吧

     */
    private Integer status;


    
}