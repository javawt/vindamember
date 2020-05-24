package com.songlanyun.jymall.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Data
@Component
public class Send {
   @Value("${sms.appkey}")
    private String appKey;

   @Value("${sms.url}")
    private String smsUrl="";


    // 手机号
    private String mobile = "";


    boolean falg = true;

    @Autowired
    RestTemplate restTemplate;

    public Send() {
    }

    public boolean send(String mobile, String content) throws Exception {
        String result = null;

        String url= smsUrl+"?mobile="+mobile+"&content="+content+"&appkey="+appKey;
        try {
            result = restTemplate.getForObject(url, String.class);
            JSONObject json = JSONObject.parseObject(result);
            if (json.getInteger("status") != 0) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}