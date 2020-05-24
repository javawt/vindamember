package com.songlanyun.jymall.modules.business.order.service;

import com.alipay.api.AlipayApiException;
import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.order.entity.Order;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import com.songlanyun.jymall.modules.business.order.model.OrderModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:34
 */
public interface OrderService extends BaseService<Order, String> {

    void confirmEarnings(String orderId);//结算收益

    int getCompleOrderCount();

    int getCompleOrderCounts();

    Order retail(String id, String AgentId, YjGoodsSku goodsSku, OrderModel orderModel, int type);

    void saveOrderAddress(Integer addressId, String id);

    void saveOrderGoods(OrderGoods orderGoods);

    R pay(Map<String, Object> params, Order order) throws Exception;

    R pay(Integer payType, BigDecimal payPrice, String orderPayNo, String password, int orderType) throws Exception;

    R cartpay(Map<String, Object> params, List<Order> orderList) throws Exception;

    R cartpay(Integer payType, String password, List<Order> orderList) throws Exception;

    void check(Map<String, String> params) throws AlipayApiException;

    //R refundOrder(RefundModel refundModel);

}
