/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.common.utils;

/**
 * Redis所有Keys
 *
 * @author Mark sunlightcs@gmail.com
 */
public class RedisKeys {
    //token
    public final static String TOKEN = "sly:jyapp:token:";
    //限流
    public final static String LIMIT = "sly::jyapp:limit:";

    //存放登陆用户所在token
    public final static String USER_INFO_REDIS = "sly::jyapp:user:";

    /** 电话号码对应的验证码  **/
    public final static String VERFITY_CODE = "sly:jyapp:vercode:";

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
