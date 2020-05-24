package com.songlanyun.jymall.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
//@PropertySource(value = "classpath:wxPay.properties")
@Data
public class WxPayConfig {

    /**
     * APP 应用 id
     */
    private String appId="wxe9969fe57dae7784";

    /**
     * 公众号应用 id
     */
    private String publicAppId;

    /**
     * 小程序应用 id
     */
    private String miniAppId;

    /**
     * 商户号 mch id
     */
    private String mchId="1575397031";

    /**
     * 商户号支付密钥(key)
     */
    private String key="mNuRF8vudcYrEQSFL6fgvCvCs3JdNnzA";

    /**
     * 商品描述
     */
    private String body="喜来购商城";

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * H5 支付所需网址
     */
    private String wapUrl;
    /**
     * H5 交易类型
     */
    //@Value("${wxPay.pay.tradeType.h5}")
    private String tradeTypeH5="MWEB";

    /**
     * NATIVE 交易类型
     */
    //@Value("${wxPay.pay.tradeType.native}")
    private String tradeTypeNative="NATIVE";

    /**
     * JSAPI 交易类型
     */
    //@Value("${wxPay.pay.tradeType.jsAPI}")
    private String TradeTypeJsApi="JSAPI";

    /**
     * APP 交易类型
     */
    //@Value("${wxPay.pay.tradeType.app}")
    private String tradeTypeApp="APP";
     /**
      * 微信接口返回结果成功状态值(responseSuccess)
      */
     //@Value("${wxPay.pay.response.success}")
     private String responseSuccess="SUCCESS";

     /**
      * 微信接口返回结果失败状态值(responseFail)
      */
     //@Value("${wxPay.pay.response.fail}")
     private String responseFail="FAIL";

     /**
      * 微信签名字段名(sign)
      */
     //@Value("${wxPay.pay.field.sign}")
     private String fieldSign="sign";

     /**
      * 微信支付签名方式
      */
     //@Value("${wxPay.pay.signType}")
     private String signType="MD5";

     /**
      * 微信 APP 支付扩展字段( APP 支付 package: Sign=WXPay)
      */
     //@Value("${wxPay.pay.packageApp}")
     private String packageApp="Sign=WXPay";

     /**
      * 微信「统一下单」接口地址
      */
    // @Value("${wxPay.pay.api.unifiedOrderUrl}")
     private String unifiedOrderUrl="https://api.mch.weixin.qq.com/pay/unifiedorder";

     /**
      * 微信查询订单信息接口地址
      */
    // @Value("${wxPay.pay.api.orderQueryUrl}")
     private String orderQueryUrl="https://api.mch.weixin.qq.com/pay/orderquery";


}
