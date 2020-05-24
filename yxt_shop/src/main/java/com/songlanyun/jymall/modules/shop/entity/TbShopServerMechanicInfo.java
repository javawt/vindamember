package com.songlanyun.jymall.modules.shop.entity;

import com.songlanyun.jymall.common.utils.PageInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺服务技工信息表(TbShopServerMechanicInfo)实体类
 *
 * @author makejava
 * @since 2020-05-22 10:19:35
 */
@Data
public class TbShopServerMechanicInfo extends PageInfo implements Serializable{
    private static final long serialVersionUID = -69084499424005155L;
    
    private Long id;
    
    private String mechanicName;
    /**
    * 从业时间
    */
    private String workTime;
    
    private String headUrl;
    /**
    * 优先级
    */
    private Integer priority;
    /**
    * 店铺id
    */
    private Long shopId;

    @Override
    public String toString() {
        return "TbShopServerMechanicInfo{" +
                "id=" + id +
                ", mechanicName='" + mechanicName + '\'' +
                ", workTime='" + workTime + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", priority=" + priority +
                ", shopId=" + shopId +
                '}';
    }
}