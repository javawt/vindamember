/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntity;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(String userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(String userId);
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserLoginInfo queryByUserName(String username);

	void updateCoupon(Map map);

	Double getCoupon(String userId);

	String getUserName(String userId);

	SysUserEntityDTO getUserById(String userId);

	void updateUserAvatarurl(SysUserEntityDTO sysUserEntityDTO);
	void updateUserBalance(SysUserEntity sysUserEntity);

	void updateUserPassword(Map parMap);

	void updateUserGrade(SysUserEntity sysUserEntity);

	Integer getUserCount();

	Integer getTodayAddCount();

	Double getUserBalance(String userId);

	Double getUserCashMoney(String userId);

	SysUserEntity getUserByMobile(String mobile);

	void updateNickName(Map<String, Object> parMap);

	void updateGender(Map<String, Object> parMap);

	void updateShortInfo(Map<String, Object> parMap);

	Integer isExistInvition(String iCode);

	/**
     * 保存用户基本信息
	 * @param sysUserEntity
	 */
	public abstract void saveUser(SysUserEntity sysUserEntity);
}
