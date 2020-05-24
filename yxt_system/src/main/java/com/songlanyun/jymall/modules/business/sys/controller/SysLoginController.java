/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.controller;

import com.songlanyun.jymall.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class SysLoginController extends AbstractController {
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    @Value("${redis.loginTm}")
    private int loginTm;  //验证码过期时间


    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public R logout(ServletRequest request) {
        //sysUserTokenService.logout(getUserId());
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);
        redisTemplate.delete(token);
        return R.ok();
    }

    private String getRequestToken(HttpServletRequest httpRequest){
        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = httpRequest.getParameter("token");
        }

        return token;
    }

}
