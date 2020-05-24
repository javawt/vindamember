package com.songlanyun.jymall.modules.shop.entity;

import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺信息表(TbShopInfo)实体类
 *
 * @author makejava
 * @since 2020-05-12 11:21:26
 */
@Data
public class TbShopInfo implements Serializable {
    private static final long serialVersionUID = -78944047970757412L;
    
    private Long id;
    
    private String userId;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 店铺简介
    */
    private String shortInfo;
    /**
    * 店铺logo
    */
    private String shopAvatar;
    /**
    * 省
    */
    private String province;
    /**
    * 市
    */
    private String city;
    /**
    * 区
    */
    private String area;
    /**
    * 详细地址
    */
    private String address;
    /**
    * 经度
    */
    private Double longitude;
    /**
    * 纬度
    */
    private Double latitude;
    
    private Date createTime;
    /**
    * 店铺钱包
    */
    private Decimal shopBalance;
    /**
    * 星级评分
    */
    private String starGrade;
    /**
    * 服务类别
    */
    private String serverType;
    /**
    * 效果分
    */
    private String effectsScore;
    /**
    * 环境分
    */
    private String environmentScore;
    /**
    * 服务分
    */
    private String serverScore;
    /**
    * 电话
    */
    private String contact;
    /**
    * 作息时间
    */
    private String workTime;
    /**
    * 店铺申请成功记录id
    */
    private Long applyInfoId;
    /**
    * 货号前缀(唯一)
    */
    private String preItemno;

}