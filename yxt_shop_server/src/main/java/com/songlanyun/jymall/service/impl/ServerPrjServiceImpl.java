package com.songlanyun.jymall.service.impl;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;
import com.songlanyun.jymall.domain.ServerGoods;
import com.songlanyun.jymall.domain.ServerPrj;
import com.songlanyun.jymall.domain.ServerTime;
import com.songlanyun.jymall.repository.ServerGoodsRepository;
import com.songlanyun.jymall.repository.ServerPrjRepository;
import com.songlanyun.jymall.service.ServerInfoService;
import com.songlanyun.jymall.service.ServerPrjService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author ww
 * @date 2019-12-13
 */
@Service
public class ServerPrjServiceImpl implements ServerPrjService {

    @Autowired
    private ServerPrjRepository serverPrjRepository;

    @Override
    public ServerPrj add(ServerPrj goodsDTO) {
        ServerPrj newSrvPrj = serverPrjRepository.save(goodsDTO);
        return newSrvPrj;
    }

    @Override
    public ServerPrj add(ServerTime goodsDTO) {
        return null;
    }

    /**
     * 删除某服务项目
     * @param id
     */
    @Override
    public void deleteById(Long id){
        serverPrjRepository.deleteById(id);
    }


    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");
        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));
            Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "createTime");
            Page<ServerPrj> pageList = serverPrjRepository.findAll(new PageRequest(page, size));
            return R.ok(StatusMsgEnum.QUERY_SUCCESS).put("data",pageList);
        }
        else{
            return R.error("需要分页参数");
        }

    }

    @Override
    public ServerPrj update(ServerPrj goodsDTO) {
        ServerPrj newSrvPrj = serverPrjRepository.save(goodsDTO);
        return newSrvPrj;
    }


}