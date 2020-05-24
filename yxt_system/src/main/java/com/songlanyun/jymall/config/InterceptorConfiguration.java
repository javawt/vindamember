package com.songlanyun.jymall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

   @Autowired
   AccessInterceptor accessInterceptor;

   public InterceptorConfiguration() {

   }

   @Override
   public void addInterceptors(InterceptorRegistry registry) {

      // registry.addInterceptor(accessInterceptor).addPathPatterns("/**").excludePathPatterns("/static/**","/webjars/**","/swagger-resources/**");
       InterceptorRegistration addInterceptor = registry.addInterceptor(accessInterceptor);
       // 排除配置
       addInterceptor.excludePathPatterns("/error");
       addInterceptor.excludePathPatterns("/static/**");//排除静态资源
       addInterceptor.excludePathPatterns("/webjars/*");
       addInterceptor.excludePathPatterns("/web/**");
       addInterceptor.excludePathPatterns("/swagger-resources/**");
       // 拦截配置
       addInterceptor.addPathPatterns("/**");
   }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**","/web/**").addResourceLocations("classpath:/static/","classpath:/web/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}