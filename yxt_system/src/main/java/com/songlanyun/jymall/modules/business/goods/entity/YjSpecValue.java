package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品规格值记录表
 * 
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Data
@Entity
@Table(name = "yj_spec_value")
public class YjSpecValue implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 规格值id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer specValueId;
	/**
	 * 规格值
	 */
	private String specValue;
	/**
	 * 规格组id
	 */
	private Integer specId;
	/**
	 * 小程序id
	 */
	private Integer wxappId;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Transient
	private String specName;

}
