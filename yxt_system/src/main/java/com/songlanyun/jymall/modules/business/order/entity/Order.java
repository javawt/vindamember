package com.songlanyun.jymall.modules.business.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单表
 */
@Data
@Entity
@Table(name = "yj_order")
public class Order {
    /**
     * 订单号
     */
    @Id
    private String orderNo;

    /**
     * 商品总金额(不含优惠折扣)
     */
    private BigDecimal totalPrice;

    /**
     * 订单金额(含优惠折扣)
     */
    private BigDecimal orderPrice;

    /**
     * 优惠券id
     */
    private Integer couponId;

    /**
     * 优惠券抵扣金额
     */
    private BigDecimal couponMoney;

    /**
     * 实际付款金额(包含运费)
     */
    private BigDecimal payPrice;

    /**
     * 买家留言
     */
    private String buyerRemark;

    /**
     * 支付方式(10余额支付 20微信支付 30 -- 支付宝支付)
     */
    private Integer payType;

    /**
     * 付款状态(10未付款 20已付款)
     */
    private Integer payStatus = 10;

    /**
     * 付款时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 配送方式(10快递配送 20上门自提)
     */
    private Integer deliveryType = 10;

    /**
     * 经销商id
     */
    private String dealersId ;

    /**
     * 运费金额
     */
    private BigDecimal expressPrice;

    /**
     * 物流公司id
     */
    private Integer expressId;

    /**
     * 物流公司
     */
    private String expressCompany;

    /**
     * 物流单号
     */
    private String expressNo;


    /**
     * 发货状态(10未发货 20已发货)
     */
    private Integer deliveryStatus = 10;

    /**
     * 发货时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    /**
     * 收货状态(10未收货 20已收货)
     */
    private Integer receiptStatus = 10;

    /**
     * 收货时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date receiptTime;

    /**
     * 订单状态(10：待付款  20：待发货  30：待收货   40： 退货  50:完成  60：取消  70: 付款失败  80：过期)
     */
    private Integer orderStatus = OrderStatus.PAYMENT;

    /**
     * 是否已评价(0否 1是)
     */
    private Integer isComment = 0;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 标识：累积用户实际消费金额
     */
    private Integer isUserExpend;

    /**
     * 是否删除
     */
    private Integer isDelete = 0;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime = new Date();

    /**
     * 订单类型 0--精品区购买的订单 1--批发的订单
     */
    private Integer orderType;

    private String isComplete;

    private String boutiqueBonus;

    private String retailBonus;

    private Integer redPacket=0;//红包数量

    /**
     * 订单商品信息
     */
    @Transient
    private OrderGoods orderGoodsTH;

    @Transient
    private List<OrderGoods> orderGoods;

    /**
     * 订单地址信息
     */
    @Transient
    private OrderAddress orderAddress;

    @Transient
    private YjGoodsSku yjGoodsSku;

    private BigDecimal easyMoney;

    private String callbackNo;//支付回调订单号

    private Integer activityId;

    //(10：待付款  20：待发货  30：待收货   40： 退货  50:完成  60：取消  70: 付款失败  80：过期
    // 90: 申请退款 100:已结算收益 110支付中 120退货 130拒绝退货 140退货受理 150退货成功 160退货中 170 退款失败

    public interface OrderStatus {
        Integer PAYMENT = 10;
        Integer SHIPPED = 20;
        Integer RECEIVED = 30;
        //        Integer RETURNED = 40;
        Integer COMPLETE = 50;
        Integer CANCEL = 60;
        Integer PAYMENT_FAILED = 70;
        Integer EXPIRE = 80;
        Integer REFUND = 90;
        Integer SETTLED = 100;
        Integer IN_PAYMENT = 110; //支付中
        Integer RETURN_GOODS = 120; //退货
        Integer REJECTION_RETURNS = 130; //拒绝退货
        Integer ACCEPTANCE_RETURNS = 140; //退货受理
        Integer RETURN_GOODS_SUCCESS = 150; //退货成功，订单关闭
        Integer IN_REFUND = 160;//退货中
        Integer REJECTION_REFUND = 170; //退款失败
    }

    /**
     * 订单状态汇总--50/80/130
     * 10、待付款20、已取消30、待提货40、待寄售50、兑换完成
     * 60、待发货70、待收货80、确认收货90、寄售中100、已寄售
     * 110、寄售失败120、申请退款130、退款完成
     */
    public interface xlgOrderStatus {
        Integer PAYMENT = 10;//待付款
        Integer SHIPPED = 20;//已取消
        Integer RECEIVED = 30;//待提货
        Integer RETURNED = 40;//待寄售
        Integer COMPLETE = 50;//兑换完成
        Integer CANCEL = 60;//待发货
        Integer PAYMENT_FAILED = 70;//待收货
        Integer EXPIRE = 80;//确认收货
        Integer REFUND = 90;//寄售中
        Integer SETTLED = 100;//已寄售
        Integer IN_PAYMENT = 110; //寄售失败
        Integer RETURN_GOODS = 120; //申请退款
        Integer REJECTION_RETURNS = 130; //退款完成
    }
}
