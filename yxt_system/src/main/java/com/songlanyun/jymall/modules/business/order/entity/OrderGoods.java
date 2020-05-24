package com.songlanyun.jymall.modules.business.order.entity;

import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单商品记录表
 */
@Table(name = "yj_order_goods")
@Data
@Entity
public class OrderGoods {
  /**
   * 主键id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_goods_id", insertable = false, nullable = false)
  private Integer orderGoodsId;

  /**
   * 商品id
   */
  @Column(name = "goods_id", nullable = false)
  private Integer goodsId;

  /**
   * 商品名称
   */
  @Column(name = "goods_name", nullable = false)
  private String goodsName;

  /**
   * 商品封面图id
   */
  @Column(name = "image_id", nullable = false)
  private Integer imageId;

  /**
   * 商品封面图id
   */
  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  /**
   * 库存计算方式(10下单减库存 20付款减库存)
   */
  @Column(name = "deduct_stock_type", nullable = false)
  private Integer deductStockType = 20;

  /**
   * 规格类型(10单规格 20多规格)
   */
  @Column(name = "spec_type", nullable = false)
  private Integer specType = 10;

  /**
   * 商品sku标识
   */
  @Column(name = "spec_sku_id", nullable = false)
  private String specSkuId;

  /**
   * 商品规格id
   */
  @Column(name = "goods_sku_id", nullable = false)
  private Integer goodsSkuId;

  /**
   * 商品规格信息
   */
  @Column(name = "goods_attr", nullable = false)
  private String goodsAttr;

  /**
   * 商品详情
   */
  @Column(name = "content", nullable = false)
  private String content;

  /**
   * 商品编码
   */
  @Column(name = "goods_no", nullable = false)
  private String goodsNo;

  /**
   * 商品价格(单价)
   */
  @Column(name = "goods_price", nullable = false)
  private BigDecimal goodsPrice;

  /**
   * 商品划线价
   */
  @Column(name = "line_price", nullable = false)
  private BigDecimal linePrice;

  /**
   * 优惠券折扣金额
   */
  @Column(name = "coupon_money", nullable = false)
  private BigDecimal couponMoney;

  /**
   * 购买数量
   */
  @Column(name = "total_num", nullable = false)
  private Integer totalNum;

  /**
   * 商品总价(数量×单价)
   */
  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;

  /**
   * 实际付款价(折扣和优惠后)
   */
  @Column(name = "total_pay_price", nullable = false)
  private BigDecimal totalPayPrice;

  /**
   * 是否开启单独分销(0关闭 1开启)
   */
  @Column(name = "is_ind_dealer", nullable = false)
  private Boolean indDealer = false;

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

  private int ordinaryNum;

  private int consignNum;

  private BigDecimal easyMoney;

  /**
   * 创建时间
   */
  @Column(name = "create_time", nullable = false)
  private Date createTime = new Date();
  @Transient
  private String specValue;

  @Transient
  private YjGoodsSku yjGoodsSku;

  
}