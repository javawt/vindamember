package com.songlanyun.jymall.modules.business.jysys.banner.controller;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.banner.entity.Banner;
import com.songlanyun.jymall.modules.business.jysys.banner.service.BannerService;
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
 * Created by zenghang on 2019/11/22.
 */
@RestController
@RequestMapping("/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> param) {
        /*PageUtils pageUtils = bannerService.queryPage(param);
        return R.ok().put("data", pageUtils);*/
        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));

            param.remove("page");
            param.remove("size");

            Sort sort = new Sort(Sort.Direction.DESC, "priority");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            Page<Banner> pageData = bannerService.findAll((Specification<Banner>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), size, 1);
            return R.ok().put("data", pageUtils);
        }
        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", bannerService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Banner banner) {
        bannerService.save(banner);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        bannerService.deleteById(id);
        return R.ok("删除成功");
    }
}
