/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.config;

import com.songlanyun.jymall.modules.business.sys.entity.oauth2.OAuth2Filter;
import com.songlanyun.jymall.modules.business.sys.entity.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setRememberMeManager(null);

        //使用自定义session管理器
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/api/**", "anon");
        filterMap.put("/sys/oss/upload", "anon");
        filterMap.put("/statistical/orderDetailsExport", "anon");
        filterMap.put("/statistical/ConsignmentDetailsExport", "anon");
        filterMap.put("/statistical/serviceDetailsExport", "anon");
        filterMap.put("/yjCash/cashExport", "anon");
        filterMap.put("/order/wholesale/explist", "anon");
        filterMap.put("/statistical/UserResultExport", "anon");
        filterMap.put("/order/goods/expOrder", "anon");
        filterMap.put("/order/goods/expWhoOrder", "anon");
        filterMap.put("/sysAdmin/activitySelectList", "anon");
        filterMap.put("/sys/yjopterlog", "anon");
        filterMap.put("/sys/user/reset/password", "anon");
        filterMap.put("/appConfig/list", "anon");
        filterMap.put("/download/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/aaa.txt", "anon");
        filterMap.put("/file/**", "anon");

        filterMap.put("/goods/yjgoods/info/**", "anon");
        filterMap.put("/init/dict/getDictList/**", "anon");

        //支付宝回调地址
        filterMap.put("/order/alipay_callback", "anon");
        filterMap.put("/order/wxpay_callback", "anon");
        filterMap.put("/imgs/**", "anon");
        //图片地址过滤
        filterMap.put("/path/**", "anon");

        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        StatelessSessionManager sessionManager = new StatelessSessionManager();

        //设置session过期时间
        sessionManager.setGlobalSessionTimeout(3600 * 1000);
        return sessionManager;
    }

}
