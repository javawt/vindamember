package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.SpecialSymbols;
import com.songlanyun.jymall.common.utils.StatusMsgEnum;
import com.songlanyun.jymall.common.validator.ValidatorUtils;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsService;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsSkuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 商品记录表
 *
 * @author wange
 * @email wangwan@songlanyun.com
 * @date 2019-11-20 16:38:40
 */
@RestController
@RequestMapping("/goods/yjgoods")
public class YjGoodsController {
    @Autowired
    private YjGoodsService yjGoodsService;

    @Autowired
    private YjGoodsSkuService yjGoodsSkuService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @Cacheable( value="sly:xlgshop:yjgoodsList",keyGenerator = "keyGenerator")
    public R list(@RequestParam Map<String, Object> params) {
        //PageUtils page = yjGoodsService.queryPage(params);
        //return R.ok().put("page", page);
        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");
            Sort sort = new Sort(Sort.Direction.DESC, "goodsSort");
            Pageable pageable = PageRequest.of(page - 1, size, sort);
            try {
                Page<YjGoods> pageData = yjGoodsService.findAll((Specification<YjGoods>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (null != params.get("categoryId")) {
                        if (StringUtils.isNotBlank(params.get("categoryId").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("categoryId"), params.get("categoryId")));
                        }
                    }
                    if (null != params.get("goodsStatus")) {
                        if (StringUtils.isNotBlank(params.get("goodsStatus").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("goodsStatus"), params.get("goodsStatus")));
                        }
                    }
                    if (null != params.get("goodsAttribute")) {
                        if (StringUtils.isNotBlank(params.get("goodsAttribute").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("goodsAttribute"), params.get("goodsAttribute")));
                        }
                    }
                    if (null != params.get("goodsName")) {
                        if (StringUtils.isNotBlank(params.get("goodsName").toString())) {
                            predicates.add(criteriaBuilder.like(root.get("goodsName"), "%"+ SpecialSymbols.queryString(params.get("goodsName").toString())+"%"));
                        }
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                //将内容设置为空
                PageUtils pageUtils = new PageUtils(pageData.getContent().stream().peek(l -> l.setContent("")).collect(
                        Collectors.toList()), (int) pageData.getTotalElements(), size, page);



                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {

                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }
    /**
     * 列表
     */
    @RequestMapping("/lists")
    @Cacheable( value="sly:xlgshop:yjgoodsList",keyGenerator = "keyGenerator")
    public R lists(@RequestParam Map<String, Object> params) {
        //PageUtils page = yjGoodsService.queryPage(params);
        //return R.ok().put("page", page);
        //params.put("specType",10);
        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");

            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<YjGoods> pageData = yjGoodsService.findAll((Specification<YjGoods>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    //predicates.add(criteriaBuilder.equal(root.get("specType"), params.get("specType")));
                    if (null != params.get("categoryId")) {
                        if (StringUtils.isNotBlank(params.get("categoryId").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("categoryId"), params.get("categoryId")));
                        }
                    }
                    if (null != params.get("goodsStatus")) {
                        if (StringUtils.isNotBlank(params.get("goodsStatus").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("goodsStatus"), params.get("goodsStatus")));
                        }
                    }
                    if(null != params.get("activityId")){
                        if (StringUtils.isNotBlank(params.get("activityId").toString())) {
                            predicates.add(criteriaBuilder.notEqual(root.get("activityId"), params.get("activityId")));
                        }
                    }
                    Path<Object> paths = root.get("goodsAttribute");
                    CriteriaBuilder.In<Object> ins = criteriaBuilder.in(paths);
                    ins.value("0");
                    ins.value("3");
                    predicates.add(criteriaBuilder.and(ins));//查组合区
                    if (null != params.get("goodsName")) {
                        if (StringUtils.isNotBlank(params.get("goodsName").toString())) {
                            predicates.add(criteriaBuilder.like(root.get("goodsName"), "%"+ SpecialSymbols.queryString(params.get("goodsName").toString())+"%"));
                        }
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);
                PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), size, page);
                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {

                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{goodsId}")
    //@RequiresPermissions("goods:yjgoods:info")
    public R info(@PathVariable("goodsId") Integer goodsId) {
        YjGoods yjGoods = yjGoodsService.findById(goodsId);
        return R.ok().put("yjGoods", yjGoods);
    }

    @RequestMapping("/getGoodsSku/{goodsId}")
    //@RequiresPermissions("goods:yjgoods:info")
    public R getGoodsSku( @PathVariable("goodsId") Integer goodsId) {
        //YjGoods yjGoods = yjGoodsService.findById(goodsId);
        List<YjGoodsSku> skuList=new ArrayList<>();

        //单规格
        //    if (yjGoods.getSpecType()==10){
        Map<String,Object> param=new HashMap<>();
        param.put("goodsId",goodsId);
        param.put("asc","goodsSkuId");
        skuList=yjGoodsSkuService.findAll(param);
        // }

        return R.ok().put("data", skuList);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true)
    public R save(@RequestBody YjGoods yjGoods) {
        if (yjGoods.getGoodsId() == null) {
            yjGoods.setCreateTime(new Date());
            yjGoods.setUpdateTime(new Date());
        }
        if(yjGoods.getActivityId()==null)
            yjGoods.setActivityId(0);//活动id默认给0
        try {
            YjGoods goods= yjGoodsService.saveGoods(yjGoods);
            return R.ok().put("data",goods);
        }catch (RRException err){
            return R.error(StatusMsgEnum.FAIL);
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true)
    public R update(@RequestBody YjGoods yjGoods) {
        ValidatorUtils.validateEntity(yjGoods);
        yjGoodsService.save(yjGoods);

        return R.ok();
    }
    /**
     * 修改
     */
    @RequestMapping("/updateGoodsType")
    @CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true)
    public R updateGoodsType(@RequestBody YjGoods yjGoods) {
        ValidatorUtils.validateEntity(yjGoods);
        yjGoodsService.updateGoodsType(yjGoods.getGoodsId(),yjGoods.getGoodsType());
        return R.ok();
    }

    /**
     * 批量删除删除
     */
    @DeleteMapping("/delete")
    @CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true)
    public R delete(@RequestBody Integer[] goodsIds) {
        yjGoodsService.deleteByIds(goodsIds);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    @CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true)
    public R delete(@PathVariable("id") Integer goodsIds) {
        yjGoodsService.deleteById(goodsIds);

        return R.ok();
    }

}
