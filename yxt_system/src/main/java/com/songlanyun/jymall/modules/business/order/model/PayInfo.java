package com.songlanyun.jymall.modules.business.order.model;

import lombok.Data;

@Data
public class PayInfo {
    // 异步回调地址
    private  String notifyUrl="http://pm.pinganchejia.com/mxc/api/notifyWeiXinPay";
    // 同步回调地址
    public static String returnUrl="http://jymall.free.idcfengye.com/jyshop/order/alipay_callback";
    /*訂單金額*/
    private  String money="";
    /*交易号*/
    private  String orderNo="";
    /*openID*/
    private  String openId="";
    /*订单超时时间单位M*/
    private  String outTime="30m";

    /**
     * 用户钱包支付密码
     */
    private String password;
}
