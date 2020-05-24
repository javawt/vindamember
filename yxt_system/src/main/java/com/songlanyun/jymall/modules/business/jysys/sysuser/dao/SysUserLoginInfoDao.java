package com.songlanyun.jymall.modules.business.jysys.sysuser.dao;

import com.songlanyun.jymall.common.utils.OrderStatusEnum;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户登录
 * 接口中所有属性都是pubic static final修饰的，方法都是public abstract修饰的
 * abstract 不共存final、private、static、native、synchronized
 */
@Mapper
public interface SysUserLoginInfoDao {

    public abstract SysUserLoginInfo findByUserId(String userId);

    public abstract void save(SysUserLoginInfo sysUserLoginInfo);

}
