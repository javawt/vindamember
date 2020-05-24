package com.songlanyun.jymall.modules.business.jysys.address.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang.
 * Date: 2019/12/2
 * Time: 14:18
 * <p>
 * 地址表
 */
@Data
@Entity
@Table(name = "yj_user_address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    /**
     * 收货人姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 联系电话
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 所在省份id
     */
    @Column(name = "province_name")
    private String provinceName;

    /**
     * 所在城市id
     */
    @Column(name = "city_name")
    private String cityName;

    /**
     * 所在区id
     */
    @Column(name = "region_name")
    private String regionName;

    /**
     * 新市辖区(该字段用于记录region表中没有的市辖区)
     */
    @Column(name = "district")
    private String district;

    /**
     * 详细地址
     */
    @Column(name = "detail")
    private String detail;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 小程序id
     */
    @Column(name = "postal_code")
    private Integer postalCode;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否默认地址 1为默认地址
     */
    @Column(name = "is_default")
    private Integer isDefault;
}
