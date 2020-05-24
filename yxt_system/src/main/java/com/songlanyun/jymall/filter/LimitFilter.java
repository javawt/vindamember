package com.songlanyun.jymall.filter;

import com.alibaba.fastjson.JSON;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 限流
 * 防止流读取一次后就没有了, 所以需要将流继续写出去
 */
@Order(1)
@WebFilter(filterName = "limitFilter", urlPatterns = {"/*"})
public class LimitFilter implements Filter {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Value("${limit.time}")
    long time;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
         RequestReaderHttpServletRequestWrapper requestWrapper = new RequestReaderHttpServletRequestWrapper(request);

        String token = request.getHeader("auth");//header方式
        String uri = request.getRequestURI().toString();
        String redisKey = RedisKeys.LIMIT + token + ":" + uri;
        System.out.println(redisKey);
        String value = redisTemplate.opsForValue().get(redisKey);
        System.out.println("value="+value);
        String body = requestWrapper.getBodyString(requestWrapper);
        System.out.println("body="+body);
        if (body.equals(value)) {
            servletResponse.setContentType("application/json;charset=utf-8");
           // R responseBean = new R()
            R r = new R();
             r.put("code", -1);
            r.put("msg", "操作频繁");
            servletResponse.getWriter().write(JSON.toJSONString(r));

            return;
        }

        redisTemplate.opsForValue().set(redisKey, body, time, TimeUnit.MILLISECONDS);

        System.out.println("denglubu ");
        filterChain.doFilter(requestWrapper, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

}