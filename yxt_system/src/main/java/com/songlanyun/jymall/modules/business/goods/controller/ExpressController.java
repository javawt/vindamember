package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.goods.entity.Express;
import com.songlanyun.jymall.modules.business.goods.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tsh.
 * Date: 2019/12/6
 * Time: 15:36
 */
@RestController
@RequestMapping("/express")
public class ExpressController {
    @Autowired
    private ExpressService expressService;

    /**
     * 物流公司列表信息
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));
            param.remove("page");
            param.remove("size");
            Sort sort = new Sort(Sort.Direction.ASC, "sort");
            Pageable pageable = PageRequest.of(page-1, size, sort);
            Page<Express> pages = expressService.findAll((Specification<Express>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);

            PageUtils pageUtils = new PageUtils(pages.getContent(), (int) pages.getTotalElements(), size, page);
            return R.ok().put("data", pageUtils);}
        throw new RRException("查询条件中未包含指定参数：page and size");

        /*PageUtils pageUtils = expressService.queryPage(param);
        return R.ok().put("data", pageUtils);*/
       /* R r = new R();
        r.put("data", expressService.findAll());
        return r;*/
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", expressService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Express express) {
        expressService.save(express);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        expressService.deleteById(id);
        return R.ok("删除成功");
    }
}
