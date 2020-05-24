package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品规格组记录表
 * 
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Data
@Entity
@Table(name = "yj_spec")
public class YjSpec implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 规格组id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer specId;
	/**
	 * 规格组名称
	 */
	private String specName;
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
	private List<YjSpecValue> specValueList;

}
