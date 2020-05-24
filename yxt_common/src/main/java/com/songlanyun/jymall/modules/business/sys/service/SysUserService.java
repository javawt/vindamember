/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntity;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserLoginInfo;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);

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

	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user);

    /**
     * 保存APP用户
	 * @param user
	 */
	void saveAPPUser(SysUserEntity user);

	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);
	
	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param newPassword  新密码
	 */
	void updatePassword(String userId, String newPassword);

	int checkVerCode(String phone, String vcode);

	void updateCoupon(Map map);

	Double getCoupon(String userId);

	String getUserName(String userId);

	SysUserEntityDTO getUserById(String userId);

	void updateUserBalance(SysUserEntity sysUserEntity);

	void updateUserGrade(SysUserEntity sysUserEntity);

	Integer getUserCount();

	Integer getTodayAddCount();

	R getUserBalance(String userId);

	SysUserEntity getUserByMobile(String mobile);

	SysUserEntityDTO updateNickName(Map<String, Object> parMap);

	SysUserEntityDTO updateGender(Map<String, Object> parMap);

	SysUserEntityDTO updateShortInfo(Map<String, Object> parMap);

	Integer isExistInvition(String iCode);
}
