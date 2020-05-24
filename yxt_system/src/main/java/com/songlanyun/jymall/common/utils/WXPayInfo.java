package com.songlanyun.jymall.common.utils;

import lombok.Data;

@Data
public class WXPayInfo {
    // 支付回调地址
    private  String notifyUrl="http://115.29.195.218:10020/xlg/order/wxpay_callback";
    //private  String notifyUrl="http://115.29.195.218:10020/xlg/order/wxpay_callback";
    /*訂單金額*/
    private  String money="";
    /*交易号*/
    private  String orderNo="";
    /*openID*/
    private  String openId="";
    /*订单超时时间单位M*/
    private  String outTime="30m";
}
