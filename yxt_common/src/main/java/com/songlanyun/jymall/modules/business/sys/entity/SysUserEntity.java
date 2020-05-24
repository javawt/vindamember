/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.songlanyun.jymall.common.validator.group.AddGroup;
import com.songlanyun.jymall.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	private String userId;


	/**
	 * 邮箱
	 */
	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;




	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Integer> roleIdList;

	/**
	 * 创建者ID
	 */
	private String createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;


	/** 推荐人id  **/
	private String referee;

	/**
	 * 性别
	 */
	private Integer gender = 0;

	/**
	 * 国家
	 */
	private String country = "";

	/**
	 * 省份
	 */
	//@Column(name = "province" )
	private String province = "";

	/**
	 * 城市
	 */
	//@Column(name = "city" )
	private String city = "";


	/**
	 * 用户可用余额
	 */
	//@Column(name = "balance" )
	private BigDecimal balance = BigDecimal.ZERO;



	/**
	 * 是否删除
	 */
	//@Column(name = "is_delete" )
	private Boolean isDelete = Boolean.FALSE;

	/**
	 * 更新时间
	 */
	//@Column(name = "update_time")
	private Date updateTime;




	/** 临时用，不写入数据库  **/
//	@Transient
	@TableField(exist = false)
	private String token;

	/** 注册时的验证码  **/
//	@Transient
	@TableField(exist = false)
	private String verCode;
	/**
	 * 邀请码
	 */
	@Column(name = "Invitation_code")
	private String invitationCode;

	/**
	 * 头像地址
	 */
	//@Column(name = "avatar_url")
	private String avatarUrl;

	/**
	 * 用户昵称
	 */
	//@Column(name = "nick_name")
	private String nickName;

	private String shortInfo;//个性签名



	@TableField(exist = false)
	private String reName;

}
