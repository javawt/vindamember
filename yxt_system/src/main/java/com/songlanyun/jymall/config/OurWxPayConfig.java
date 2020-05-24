package com.songlanyun.jymall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
public class OurWxPayConfig implements com.github.wxpay.sdk.WXPayConfig {
    @Autowired
    private WxUtil wxUtil;

    private byte[] certData;
	/*  //读取证书
	  public OurWxPayConfig()throws Exception{
	  	InputStream certStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(WxUtil.certUrl);
	    this.certData = IOUtils.toByteArray(certStream);
	    certStream.close();
	  }*/

    public String getAppID() {
        return wxUtil.getAppId();
    }

    public String getMchID() {
        return wxUtil.getMchID();
    }

    public String getKey() {
        return wxUtil.getPayKey();
    }

    public InputStream getCertStream() {
        return new ByteArrayInputStream(this.certData);
    }

    public int getHttpConnectTimeoutMs() {
        return 0;
    }

    public int getHttpReadTimeoutMs() {
        return 0;
    }
}
