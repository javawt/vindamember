package com.songlanyun.jymall.common.utils;

public enum OrderStatusEnum {
    //10待付款20待发货30已关闭40待使用50待收货60申请退款70退款完成80申请退货90退货完成100待评价110订单完成
    PAYMENT(10, "待付款"),
    SENDGOODS(20, "待发货"),
    CLOSED(30, "已关闭"),

    BEUSED(40, "待使用"),
    RECGOODS(50, "待收货"),
    APPLYMONEY(60, "申请退款"),
    APPLYMONEYCOMPLETE(70, "退款完成"),
    APPLYGOODS(80, "申请退货"),
    APPLYGOODSACCEPT(90, "退货已受理"),
    RETURNGOODSADDRESS(100, "退货地址回执"),
    APPLYGOODSCOMPLETE(110, "退货完成"),
    EVALUATE(120, "待评价"),
    ORDERFINISHED(130, "订单完成");

    private Integer status;
    private String msg;

    OrderStatusEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
