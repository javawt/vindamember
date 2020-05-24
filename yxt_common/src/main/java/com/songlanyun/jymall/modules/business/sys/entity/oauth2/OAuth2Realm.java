/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.entity.oauth2;

import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import com.songlanyun.jymall.modules.business.sys.service.ShiroService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private SysUserService sysUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntityDTO user = (SysUserEntityDTO)principals.getPrimaryPrincipal();
        String userId = user.getUserId();

        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息SysUserLoginInfo
        //SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
        SysUserLoginInfo tokenEntity =  (SysUserLoginInfo) redisTemplate.opsForValue().get(accessToken);
        //token失效
        if(null==tokenEntity){
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        //SysUserLoginInfo sysUserLoginInfo = sysUserLoginInfoService.findByUserId(tokenEntity.getUserId());
        SysUserEntityDTO sysUserEntityDTO = sysUserService.getUserById(tokenEntity.getUserId());
        //账号锁定
        if(sysUserEntityDTO.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUserEntityDTO, accessToken, getName());
        return info;
    }
}
