package com.songlanyun.jymall.modules.business.jysys.bank.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.jysys.bank.entity.BankBind;
import com.songlanyun.jymall.modules.business.jysys.bank.service.BankBindService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang on 2019/11/22.
 */
@RestController
@RequestMapping("/bank/bind")
public class BankBindController {
    @Autowired
    private BankBindService bankBindService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        /*if (ShiroUtils.getUserId() != 1) {
            param.put("userId", ShiroUtils.getUserId());
        }
        PageUtils pageUtils = bankBindService.queryPage(param);
        return R.ok().put("data", pageUtils);*/


        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));
            param.remove("page");
            param.remove("size");
            Sort sort = new Sort(Sort.Direction.DESC, "ctime");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<BankBind> pageData = bankBindService.findAll((Specification<BankBind>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if(null!=param.get("icCard")){
                    if(StringUtils.isNotBlank(param.get("icCard").toString())){
                        predicates.add(criteriaBuilder.like(root.get("icCard"), "%" + param.get("icCard") + "%"));
                    }
                }
                /*if (ShiroUtils.getUserId() != 1) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                }*/
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), page, size);
            return R.ok().put("data", pageUtils);
        } else {
            return R.error("查询条件中未包含指定参数：page and size");
        }
    }

    @GetMapping("/applist")
    public R applist(@RequestParam Map<String, Object> param) {
        /*if (ShiroUtils.getUserId() != 1) {
            param.put("userId", ShiroUtils.getUserId());
        }
        PageUtils pageUtils = bankBindService.queryPage(param);
        return R.ok().put("data", pageUtils);*/


        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));
            param.remove("page");
            param.remove("size");
            Sort sort = new Sort(Sort.Direction.DESC, "ctime");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<BankBind> pageData = bankBindService.findAll((Specification<BankBind>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                if(null!=param.get("icCard")){
                    if(StringUtils.isNotBlank(param.get("icCard").toString())){
                        predicates.add(criteriaBuilder.like(root.get("icCard"), "%" + param.get("icCard") + "%"));
                    }
                }
                if (!ShiroUtils.getUserId().equals("1")) {
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), page, size);
            return R.ok().put("data", pageUtils);
        } else {
            return R.error("查询条件中未包含指定参数：page and size");
        }
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", bankBindService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody BankBind bankBind) {
        if(StringUtils.isBlank(bankBind.getIcCard())){
            return R.error("卡号不能为空");
        }
        bankBind.setUserId(ShiroUtils.getUserId());
        //判断数据重复录入，卡号不可重复
        Map<String,Object> parMap=new HashMap<>();
        parMap.put("icCard",bankBind.getIcCard());
        Long idCardNum=bankBindService.count(parMap);
        if(idCardNum>0){
            return R.error("该卡已被绑定");
        }else{
            bankBindService.save(bankBind);
            return R.ok("保存成功");
        }
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        bankBindService.deleteById(id);

        return R.ok("删除成功");
    }
}
