package com.songlanyun.jymall.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.domain.ServerGoods;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.List;

/**
* @author hupeng
* @date 2020-01-09
*/
public interface ServerInfoService {

    ServerGoods addServer(ServerGoods goodsDTO);

    /*** 删除某服务 **/
    void deleteById(Long id);


    @RequestMapping("/list")
    R list(@RequestParam Map<String, Object> params);
}