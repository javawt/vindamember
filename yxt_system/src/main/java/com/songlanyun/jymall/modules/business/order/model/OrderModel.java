package com.songlanyun.jymall.modules.business.order.model;

import lombok.Data;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/12/6
 * Time: 11:30
 *
 * 生成订单模板
 */
@Data
public class OrderModel {
    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 商品skuID
     */
    private Integer goodsSkuId;

    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 地址ID
     */
    private Integer addressId;

    /**
     * 买家留言
     */
    private String buyerRemark;

    /**
     * 订单ID
     */
    private String orderNo;

    /**
     * 针对购物车商品ids
     */
    private List<Integer> cartIds;

    private int activityType;//0 组合 1 精选 2 易货

}
