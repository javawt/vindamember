package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang.
 * Date: 2019/11/27
 * Time: 10:35
 *
 * 购物车
 */
@Data
@Entity
@Table(name = "yj_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    /**
     * 商品skuID
     */
    private Integer goodsSkuId;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 商品实体数据
     */
    @Transient
    private YjGoods goods;

    private String goodsName;

}
