/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.api.sys.controller;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.controller.AbstractController;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import com.songlanyun.jymall.modules.business.sys.entity.oauth2.TokenGenerator;
import com.songlanyun.jymall.modules.business.sys.form.SysLoginForm;
import com.songlanyun.jymall.modules.business.sys.service.SysCaptchaService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/api")
public class SysLoginApiController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysCaptchaService sysCaptchaService;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    @Value("${redis.loginTm}")
    private int loginTm;  //验证码过期时间

    /**
     * 验证码
     */
    @GetMapping("captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 登录
     */
    @PostMapping("/sys/login")
    public Map<String, Object> login(@RequestBody SysLoginForm form) throws IOException {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return R.error("验证码不正确");
        }
        //用户信息
        SysUserLoginInfo user = sysUserService.queryByUserName(form.getUsername());
        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }
        //根据用户id查询该用户的角色集
        /*List<Integer> roleIList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if(null!=roleIList && roleIList.size()>0){
            //系统用户
            if(!roleIList.contains(2)){
                return R.error("非系统用户登录");
            }
        }else{
            return R.error("非系统用户登录");
        }*/
        if (user.getUserType() != 1)
            return R.error("非app用户登录");
        //账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }
        //生成一个token
        String token = TokenGenerator.generateValue();
        redisTemplate.opsForValue().set(token,user,loginTm,TimeUnit.SECONDS);
        //生成token，并保存到数据库
        //R r = sysUserTokenService.createToken(user.getUserId());
        return R.ok().put("token", token);
    }

    @PostMapping("/sys/appLogin")
    public Map<String, Object> appLogin(@RequestBody SysLoginForm form) throws IOException {

        //用户信息
        SysUserLoginInfo user = sysUserService.queryByUserName(form.getUsername());

        if (user == null) {
            return R.error("用户不存在");
        }
        //根据用户id查询该用户的角色集
        /*List<Integer> roleIList = sysUserRoleService.queryRoleIdList(user.getUserId());
        if(null!=roleIList && roleIList.size()>0){
            //APP角色用户id
            if(!roleIList.contains(3)){
                return R.error("非APP用户登录");
            }
        }else{
            return R.error("非APP用户登录");
        }*/
        if (user.getUserType() != 0)
            return R.error("非app用户登录");
        //账号不存在、密码错误
        if (!user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }

        //生成一个token
        String token = TokenGenerator.generateValue();
        redisTemplate.opsForValue().set(token,user,loginTm,TimeUnit.SECONDS);
        //生成token，并保存到数据库
        //R r = sysUserTokenService.createToken(user.getUserId());
        return R.ok().put("token", token);
    }

}
