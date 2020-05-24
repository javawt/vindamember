package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品与规格值关系记录表
 * 
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Data
@Entity
@Table(name = "yj_spec_rel")
public class YjSpecRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	/**
	 * 商品id
	 */
	private Integer goodsId;
	/**
	 * 规格组id
	 */
	private Integer specId;
	/**
	 * 规格值id
	 */
	private Integer specValueId;
	/**
	 * 小程序id
	 */
	private Integer wxappId;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

}
