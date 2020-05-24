package com.songlanyun.jymall.modules.business.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 业务交互层(*≧︶≦))(￣▽￣* )ゞ
 */
@Data
public class SysUserEntityDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	private String userId;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空")
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空")
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	@NotBlank(message="邮箱不能为空")
	@Email(message="邮箱格式不正确")
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
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	private String createUserId;

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
	private String referee;

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



	@TableField(exist = false)
	private String reName;
	private String shortInfo;//个性签名

	private Integer userType; //0:app 1:administrator

	private Integer isMember; //0:isNotMember 1:member

	private Integer isInfluencer; //0:isNotInfluencer 1:influencer

	private Integer isExpert=0;

}
