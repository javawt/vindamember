package com.songlanyun.jymall.modules.shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.songlanyun.jymall.common.utils.PageInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺申请表(TbShopOwnerApplyInfo)实体类
 *
 * @author makejava
 * @since 2020-05-11 10:57:12
 */
@Data
public class TbShopOwnerApplyInfo extends PageInfo implements Serializable {
    private static final long serialVersionUID = -24007511377520277L;
    
    private Long id;

    private String userId;

    private String username;
    /**
    * 负责人名字
    */
    private String ownerName;
    private String ownerPhone;
    private String verCode;
    /**
    * 身份证号
    */
    private String idCard;
    
    private String idCardFront;
    
    private String idCardReverse;
    
    private String phone;
    /**
    * 营业执照
    */
    private String businessLicence;
    /**
    * 可提供员工数量
    */
    private Integer staffNum;
    /**
    * 客服电话
    */
    private String serverContact;
    
    private String shopName;
    
    private String shortInfo;
    
    private String headUrl;
    
    private String province;
    
    private String city;
    
    private String area;
    
    private String address;
    
    private Date applyTime;
    
    private String checkId;

    private String checkName;
    
    private int checkStatus; //1 未审核 2 通过 3 不通过
    
    private Date checkTime;
    
    private String checkReason;

}