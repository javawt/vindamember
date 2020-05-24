package com.songlanyun.jymall.config;

import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 */
@Configuration
//@Data
public class WxUtil {
    //微信app id
    //@Value("${wechat.appid}")
    private String appId = "wxe9969fe57dae7784";
    //微信应用秘钥
    //@Value("${wechat.appsecret}")
    private  String appSecret = "a705c88619cdc861830405a543310403";
    //商户id
    //@Value("${wechat.mchID}")
    private  String mchID = "1575397031";
    //微信支付key
    //@Value("${wechat.payKey}")
    private  String payKey = "mNuRF8vudcYrEQSFL6fgvCvCs3JdNnzA";
    //微信证书
    //@Value("${wechat.certUrl}")
    private  String certUrl = "statics/wxpay/apiclient_cert.p12";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMchID() {
        return mchID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    public String getCertUrl() {
        return certUrl;
    }

    public void setCertUrl(String certUrl) {
        this.certUrl = certUrl;
    }
}
