package com.songlanyun.jymall.modules.shop.entity;

import com.songlanyun.jymall.common.utils.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺店员信息表(TbShopClerkInfo)实体类
 *
 * @author makejava
 * @since 2020-05-22 16:17:31
 */
@Data
public class TbShopClerkInfo extends PageInfo implements Serializable {
    private static final long serialVersionUID = 651810593659840463L;
    
    private Long id;
    
    private Long shopId;
    
    private String clerkName;
    
    private String phone;
    
    private String shortInfo;
    /**
    * 是否开启管理特权
    */
    private Integer openPrivilege;
    
    private String userId;

}