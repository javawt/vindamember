/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.songlanyun.jymall.modules.business.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.songlanyun.jymall.common.validator.group.AddGroup;
import com.songlanyun.jymall.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.persistence.Transient;
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
public class SysUserEntityRed implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;

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
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	/**
	 * 状态  0：系统用户   1：App用户
	 */
	private Integer type;

	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	private Long createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	//@Column(name = "card" )
	private String card;

	/** 支付密码  **/
	//@Column(name = "pay_psw" )
	private String payPsw;

	/** 推荐人id  **/
	//@Column(name = "referee" )
	private Long referee;

	/**
	 * 性别
	 */
	//@Column(name = "gender" )
	private Integer gender = 0;

	/**
	 * 国家
	 */
	//@Column(name = "country" )
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
	 * 默认收货地址
	 */
	//@Column(name = "address_id" )
	private Integer addressId = 0;

	/**
	 * 用户可用余额
	 */
	//@Column(name = "balance" )
	private BigDecimal balance = BigDecimal.ZERO;

	/**
	 * 用户总支付的金额
	 */
	//@Column(name = "pay_money" )
	private BigDecimal payMoney = BigDecimal.ZERO;

	/**
	 * 实际消费的金额(不含退款)
	 */
	//@Column(name = "expend_money" )
	private BigDecimal expendMoney = BigDecimal.ZERO;

	/**
	 * 会员等级id  会员等级id  10---普通 20--初级经销商 30--中级经销商 40--高级经销商
	 */
	private Integer gradeId = 10;

	/**
	 * 是否删除
	 */
	//@Column(name = "is_delete" )
	private Boolean isDelete = Boolean.FALSE;

	/**
	 * 小程序id
	 */
	//@Column(name = "wxapp_id")
	private Integer wxappId = 0;

	/**
	 * 更新时间
	 */
	//@Column(name = "update_time")
	private Integer updateTime = 0;

	/**
	 * 批发券数量
	 */
	private BigDecimal coupon;



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
	//@Column(name = "Invitation_code")
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

	private BigDecimal easyMoney;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date birthDay;

	private boolean proxyType=false;//是否缴费升级

	@Transient
	private String orderNo;

	@Transient
	private Double money;
}
