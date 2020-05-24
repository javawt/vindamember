package com.songlanyun.jymall.modules.business.jysys.sysuser.service;

import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;

public interface SysUserLoginInfoService {

    /**
     * 根据用户id获取用户登录信息
    * @param userId
     * @return
     */
    public abstract SysUserLoginInfo findByUserId(String userId);

    /**
     * 新增保存用户登录信息
     */
    public abstract void save(SysUserLoginInfo sysUserLoginInfo);

}
