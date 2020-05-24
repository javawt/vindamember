package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品分类表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@Data
@Entity
@Table(name = "yj_category")
public class YjCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品分类id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 上级分类id,最上级parentId为0,默认新建的都是最上级,需手动设置
     */
    private Integer parentId = 0;
    /**
     * 分类图片url地址
     */
    private String imgUrl;
    /**
     * 排序方式(数字越小越靠前)
     */
    private Integer sort;
    /**
     * 小程序id
     */
    private Integer wxappId;
    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 是否删除
     */
    private Integer isDelete = 0;
    /**
     * 修改人id
     */
    private String updateId;

	/**
	 * 分类图片id
	 */
	private Integer imgId;
	/**
	 * 子级List
	 */
	@Transient
	private List<YjCategory> children;

	private Integer categoryStatus=10;//上架分类(10上架 20下架)

}
