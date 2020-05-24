package com.songlanyun.jymall.common.utils;

public enum StatusMsgEnum {
    SUCCESS(200,"操作成功！"),
    FAIL(300,"操作失败！"),
    PARAM_NULL(101,"参数为空！"),
    ADD_SUCCESS(200,"添加成功！"),
    ADD_REPEAT(201,"重复添加(违反唯一约束)"),
    QUERY_SUCCESS(200,"查询成功！"),
    QUERY_FALSE(101,"查询失败！"),
    DELETE_SUCCESS(200,"删除成功！"),
    UPDATE_SUCCESS(200,"更新成功！"),
    LOGIN_SUCCESS(200,"登录成功！"),
    LOGIN_FALSE(101,"登录失败！"),
    RESET_PASSWORD_SUCCESS(200,"更新密码成功！"),
    ADD_ROLE_SUCCESS(200,"添加用户成功！"),
    DELETE_ROLE_SUCCESS(200,"删除用户成功！"),
    DISABLED_SUCCESS(200,"禁用成功！"),
    ACTIVE_SUCCESS(200,"启用成功！"),
    LOGIN_NAME_PSW_ERROR(201,"用户或密码错误"),
    TOKEN_ERR(202,"token请求异常"),
    USER_IS_EXISTS(203,"用户名已经存在"),
    USER_IS_EXISTS1(2031,"手机号已注册"),
    OSS_GROUP_EXISTS(210,"文件上传组名已经存在"),
    OSS_GROUP_NAME_NEED(210,"文件上传分组名不能为空"),
    UPLOAD_PARAM_NEED(213,"文件参数不能为空"),
    CAN_REGISTER(204,"此用户名可以注册"),
    NOCAN_REGISTER(204,"用户注册失败"),
    NEED_LOGIN_NAME_PSW(201,"用户或密码不能为空"),
    SMSCODESEND_SUCESS(205,"验证码发送成功"),
    SMSCODE_SENDED_FAIL(207,"验证码再次请求时间未到"),
    SMSCODESEND_TIMEOUT(208,"验证码已过期"),
    SMSCODES_NOMATCH(209,"验证码不匹配"),
    INVICTCODE_ERR(210,"邀请码不存在"),
    SMSCODESEND_FAIL(207,"验证码发送失败"),

    ORDER_PAY_NOSUPPORT(300,"暂不支持此支付方式"),
    ORDER_PAY_SUCESS(310,"支付成功"),
    ORDER_PAY_FAIL(320,"支付失败"),
    ORDER_PAY_WAITING(310,"支付等待"),
    ORDER_NOEXIST(300,"订单信息不存在"),

    /**
     * 10、待付款  20、待发货 30、待收货
     * 40、退货 50、完成 60、取消 70、付款失败
     * 80、过期 90、申请退款 100、待寄售
     * 110、部分寄售 120、寄售成功 130、已操作退款 140、申请提货
     */
    WHOLESALE_10(10,"待付款"),
    WHOLESALE_20(20,"待发货"),
    WHOLESALE_30(30,"待收货"),
    WHOLESALE_40(40,"退货"),
    WHOLESALE_50(50,"完成"),
    WHOLESALE_60(60,"取消"),
    WHOLESALE_70(70,"付款失败"),
    WHOLESALE_80(80,"过期"),
    WHOLESALE_90(90,"申请退款"),
    WHOLESALE_100(100,"待寄售"),
    WHOLESALE_110(110,"部分寄售"),
    WHOLESALE_120(120,"寄售成功"),
    WHOLESALE_130(130,"已操作退款"),
    WHOLESALE_140(140,"申请提货");

    private Integer status;

    private String msg;

    StatusMsgEnum(Integer status, String msg){
        this.status = status;
        this.msg =msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
