package com.songlanyun.jymall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.songlanyun.jymall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {
    /**
     * 拦截 ----- 只针对 app 用户登陆，因为只取头部  auth ---非后台管理系统 auth 解码
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Value("${redis.login}")
    private String loginRedis = "";

     @Value("${ignoreMethod}")
    private String ignoreChkMethod = ""; //哪些方法不需要传递auth

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //得到方法名
        String method = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);

        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        String auth = request.getHeader("auth");//header方式
        String token= request.getHeader("token");
        ObjectMapper mapper = new ObjectMapper();
        R err=R.error( -2, method + "调用需要访问令牌");// customised pojo for error json message
        response.setCharacterEncoding("UTF-8"); //设置编码格式
        response.setContentType("application/json");

        //如果此接口在忽略auth数组中，则直接执行 不要写成 update,list 之类，因为 xxupdate contains update
        if (ignoreChkMethod.contains(method) || token!=null) {
            // 正常执行调用其他服务接口...
            System.out.println(" ----方法:" + method + " -------");
            return true;
        }
        //1、请求头无auth传入,不响应请求
        if (auth == null) {
            response.getWriter().write(mapper.writeValueAsString(err));
            return false;
        }
        // 2、判断redis中是否有此auth 无报错
        Object _uid = redisTemplate.opsForValue().get(loginRedis+auth);
        if (_uid==null){
            R r =   R.error(-2, "访问令牌已过期，请重新登录");
            response.getWriter().write(mapper.writeValueAsString(r));
            return false;
        }
        String uid = _uid.toString();
        if (uid == null) {
            R r =   R.error(-2, "访问令牌不正确");
             response.getWriter().write(mapper.writeValueAsString(r));
            return false;
        }
        // String dcode = Des.decrypt(encode, SECRET);

        request.setAttribute("uid", uid);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        long startTime = (Long) request.getAttribute("requestStartTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        // 打印方法执行时间
        if (executeTime > 1000) {
            System.out.println("[" + method.getDeclaringClass().getName() + "." + method.getName() + "] 执行耗时 : "
                    + executeTime + "ms");
        } else {
            System.out.println("[" + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "] 执行耗时 : "
                    + executeTime + "ms");
        }
    }
}