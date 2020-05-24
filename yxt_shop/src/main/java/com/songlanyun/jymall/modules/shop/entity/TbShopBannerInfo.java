package com.songlanyun.jymall.modules.shop.entity;

import com.songlanyun.jymall.common.utils.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺轮播图表(TbShopBannerInfo)实体类
 *
 * @author makejava
 * @since 2020-05-13 14:05:44
 */
@Data
public class TbShopBannerInfo extends PageInfo implements Serializable {
    private static final long serialVersionUID = -24075615328681611L;
    
    private Long id;
    
    private Long shopId;

    private String shopName;
    
    private String imageUrl;

}