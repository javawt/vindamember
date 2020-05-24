package com.songlanyun.jymall.modules.shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.songlanyun.jymall.common.utils.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺配置信息表(TbShopConfigInfo)实体类
 *
 * @author makejava
 * @since 2020-05-13 10:16:05
 */
@Data
public class TbShopConfigInfo extends PageInfo implements Serializable {
    private static final long serialVersionUID = -24091113763946115L;
    
    private Long id;
    
    private Long shopId;

    private String shopName;
    /**
    * 是否开启通知
    */
    private int notifyMsg;
    /**
    * 门店提货返利
    */
    private Double deliveryRebate;
    /**
    * 是否开启会员功能
    */
    private int openMember;
    /**
    * 消费升级金额
    */
    private Double expenseMoney;
    /**
    * 会员折扣
    */
    private Double memberDiscount;
    /**
    * 权益信息
    */
    private String equityInfo;
    /**
    * 会员返利
    */
    private Double memberRebate;
    /**
    * 是否开启贵宾功能
    */
    private int openVip;
    /**
    * 贵宾分成
    */
    private Double vipRebate;


}