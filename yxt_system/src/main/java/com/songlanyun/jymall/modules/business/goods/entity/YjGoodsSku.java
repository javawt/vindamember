package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品规格表
 * 
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Data
@Entity
@Table(name = "yj_goods_sku")
public class YjGoodsSku implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品规格id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer goodsSkuId;
	/**
	 * 商品id
	 */
	private Integer goodsId;
	/**
	 * 商品sku记录索引 (由规格id组成)
	 */
	private String specSkuId;
	/**
	 * 规格图片id
	 */
	private Integer imageId;
	/**
	 * 商品编码
	 */
	private String goodsNo;
	/**
	 * 商品价格
	 */
	private BigDecimal goodsPrice;
	/**
	 * 商品划线价
	 */
	private BigDecimal linePrice;
	/**
	 * 当前库存数量
	 */
	private Integer stockNum;
	/**
	 * 商品销量
	 */
	private Integer goodsSales;
	/**
	 * 商品重量(Kg)
	 */
	private Double goodsWeight;
	/**
	 * 小程序id
	 */
	private Integer wxappId;
	/**
	 * 创建时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime = new Date();
	/**
	 * 更新时间
	 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	/**
	 * 零售单价
	 */
	private BigDecimal retailPrice;
	/**
	 * 零售送券
	 */
	private BigDecimal retailCoupon;
	/**
	 * 批发--套餐名 wholesale
	 */
	@Column(name = "ws_name")
	private String wsName;
	/**
	 * 批发价格
	 */
	@Column(name = "ws_price")
	private BigDecimal wsPrice;
	/**
	 * 批发--需要的批发券
	 */
	@Column(name = "ws_needcoupon")
	private BigDecimal wsNeedcoupon;
	/**
	 * 限购数量
	 */
	@Column(name = "ws_limitnum")
	private Integer wsLimitnum;
	/**
	 * 批发--库存
	 */
	@Column(name = "ws_stock")
	private Integer wsStock;
	/**
	 * 运费
	 */
	private BigDecimal freight;
	/**
	 * 寄售费用 默认20%
	 */
	private Double consig;
	@Column(name = "type")
	private Integer type;

	@Transient
	private String specValue;

	@Column(name = "img_urls")
	private String imgUrls;

	private BigDecimal barterPrice; //易货价格

	@Transient
	private String specName;

}
