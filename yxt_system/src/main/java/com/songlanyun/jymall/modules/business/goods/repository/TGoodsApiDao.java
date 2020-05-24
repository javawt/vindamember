package com.songlanyun.jymall.modules.business.goods.repository;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * app dao
 *
 */
@Mapper
public interface TGoodsApiDao {

    List<Map<String ,Object>> getBarrage();
}
