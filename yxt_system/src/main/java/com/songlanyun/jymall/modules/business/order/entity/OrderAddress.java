package com.songlanyun.jymall.modules.business.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 订单收货地址记录表
 */
@Table(name = "yj_order_address")
@Entity
@Data
public class OrderAddress {
    /**
     * 地址id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "order_address_id", nullable = false)
    private Integer orderAddressId;

    /**
     * 收货人姓名
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 联系电话
     */
    @Column(name = "phone", nullable = false)
    private String phone;

    /**
     * 所在省份id
     */
    @Column(name = "province_name", nullable = false)
    private String provinceName;

    /**
     * 所在城市id
     */
    @Column(name = "city_name", nullable = false)
    private String cityName;

    /**
     * 所在区id
     */
    @Column(name = "region_name", nullable = false)
    private String regionName;

    /**
     * 详细地址
     */
    @Column(name = "detail", nullable = false)
    private String detail;

    /**
     * 订单id
     */
    @Column(name = "order_no", nullable = false)
    private String orderNo;

    /**
     * 用户id
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();
}