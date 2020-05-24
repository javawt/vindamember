package com.songlanyun.jymall.service;

import com.songlanyun.jymall.domain.ServerPrj;
import com.songlanyun.jymall.domain.ServerTime;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
* @author hupeng
* @date 2020-01-09
*/
public interface ServerTimeService {


    List<ServerTime> add(List<ServerTime> goodsDTO);

    @Transactional
    void batchInsert(List list);

    @Transactional
    void batchUpdate(List list);

    /*** 删除某服务下的所有服务时间 **/
    void deleteById(Long id);


    ServerTime update(ServerTime goodsDTO);

    List getSrvTime(Map<String, Object> params);
}