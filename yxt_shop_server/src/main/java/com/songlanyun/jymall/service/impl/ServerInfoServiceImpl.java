package com.songlanyun.jymall.service.impl;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.domain.ServerGoods;
import com.songlanyun.jymall.repository.ServerGoodsRepository;
import com.songlanyun.jymall.service.ServerInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @author ww
 * @date 2019-12-13
 */
@Service
public class ServerInfoServiceImpl implements ServerInfoService {

    @Autowired
    private ServerGoodsRepository serverGoodsRepository;

    @Override
    public ServerGoods addServer(ServerGoods goodsDTO) {
        ServerGoods newSrvGoods = serverGoodsRepository.save(goodsDTO);
        return newSrvGoods;
    }

    /**
     * 删除某服务
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        serverGoodsRepository.deleteById(id);
    }


    @Override
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");

        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));
            Sort sort = new Sort(Sort.Direction.DESC, "createTime");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            String shop_name = String.valueOf(params.get("shop_name"));
            String prj_name = String.valueOf(params.get("prj_name"));
            Long shop_id = Long.parseLong(String.valueOf(params.get("shop_id")));
            Long srv_id = Long.parseLong(String.valueOf(params.get("srv_id")));
            Object objList = serverGoodsRepository.getSrvList(shop_name, prj_name, shop_id, srv_id, pageable);


        }

        return null;

    }


}