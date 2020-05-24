package com.songlanyun.jymall.modules.business.jysys.dict.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商城字典表
 * 
 * @author zhaoyong
 * @email zy765203718@gmail.com
 * @date 2019-11-14 10:26:30
 */
@Data
@TableName("yj_dict")
@ApiModel(value="字典表实体")
public class YjDictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty("ID")
	private Integer id;
	/**
	 * 
	 */
	@ApiModelProperty("扩展属性ID")
	private String ext;
	/**
	 * 
	 */
	@ApiModelProperty("字典明细ID")
	private Integer dkey;
	/**
	 * 
	 */
	@ApiModelProperty("字典数据类型")
	private Integer dtype;
	/**
	 * 
	 */
	@ApiModelProperty("字典数据值")
	private String dvalue;

}
