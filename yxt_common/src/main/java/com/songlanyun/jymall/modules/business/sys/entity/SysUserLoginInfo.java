/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.songlanyun.jymall.common.validator.group.AddGroup;
import com.songlanyun.jymall.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 系统用户登录信息表
 *
 */
@Data
@Entity
@TableName("sys_user_login_info")
public class SysUserLoginInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private String userId;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	/** payPassword  **/
	private String payPsw;

	private Integer userType=0; //0:app 1:administrator

	private Integer isMember=0; //0:isNotMember 1:member

	private Integer isInfluencer=0; //0:isNotInfluencer 1:influencer

	private Integer isExpert=0;

}
