package com.songlanyun.jymall.modules.business.goods.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class YjSkuRel implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer goodsId;
	private List<YjSkuRelSon> dataList;


}
