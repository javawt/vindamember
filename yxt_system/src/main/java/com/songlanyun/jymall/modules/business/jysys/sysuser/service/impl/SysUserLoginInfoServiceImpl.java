package com.songlanyun.jymall.modules.business.jysys.sysuser.service.impl;

import com.songlanyun.jymall.modules.business.jysys.sysuser.dao.SysUserLoginInfoDao;
import com.songlanyun.jymall.modules.business.jysys.sysuser.service.SysUserLoginInfoService;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class SysUserLoginInfoServiceImpl implements SysUserLoginInfoService {

    @Resource
    private SysUserLoginInfoDao sysUserLoginInfoDao;


    @Override
    public SysUserLoginInfo findByUserId(String userId) {
        return sysUserLoginInfoDao.findByUserId(userId);
    }

    @Override
    public void save(SysUserLoginInfo sysUserLoginInfo) {
        sysUserLoginInfoDao.save(sysUserLoginInfo);
    }
}
