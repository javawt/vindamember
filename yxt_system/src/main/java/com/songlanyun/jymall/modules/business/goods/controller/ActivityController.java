package com.songlanyun.jymall.modules.business.goods.controller;

import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.goods.entity.Activity;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import com.songlanyun.jymall.modules.business.goods.service.ActivityService;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsService;
import com.songlanyun.jymall.modules.business.sys.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:51
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private YjGoodsService goodsService;
    @Resource
    private SysAdminService sysAdminService;


    /**
     * 列表
     */
    @GetMapping("/list")
    @Cacheable( value="sly:xlgshop:activityList",keyGenerator = "keyGenerator")
    public R list(@RequestParam Map<String, Object> param) {
        if (param.containsKey("page") && param.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(param.get("page")));
            int size = Integer.parseInt(String.valueOf(param.get("size")));
            param.remove("page");
            param.remove("size");
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Pageable pageable = PageRequest.of(page-1, size, sort);
            Page<Activity> pages = activityService.findAll((Specification<Activity>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);

            PageUtils pageUtils = new PageUtils(pages.getContent(), (int) pages.getTotalElements(), size, page);
            return R.ok().put("data", pageUtils);}
        throw new RRException("查询条件中未包含指定参数：page and size");

        /*PageUtils pageUtils = activityService.queryPage(param);
        return R.ok().put("data", pageUtils);*/
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", activityService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @CacheEvict(value="sly:xlgshop:activityList",allEntries=true)
    @Transactional
    public R save(@RequestBody Activity activity) {
        //判断是否设置未推荐首页
        if(activity.getHasRecommend()){
            if(null==activity.getId()){
                sysAdminService.updateActivityRecommends();
            }else{
                sysAdminService.updateActivityRecommend(activity.getId());
            }

        }
        activityService.save(activity);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @CacheEvict(value="sly:xlgshop:activityList",allEntries=true)
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        activityService.deleteById(id);
        return R.ok("删除成功");
    }

    /**
     * 添加删除活动商品
     *
     * @param param goodsIds   商品ids
     *              activityId 活动id
     */
    @PostMapping("/save/goods")
    @Caching(evict = {@CacheEvict(value="sly:xlgshop:activityList",allEntries=true),@CacheEvict(value="sly:xlgshop:yjgoodsList",allEntries=true)})
    public R saveGoods(@RequestBody Map<String, Object> param) {
        Integer activityId = (Integer) param.get("activityId");
        Integer activetyType=(Integer) param.get("activityType");
        List<Integer> goodsIds = (List<Integer>) param.get("goodsIds");
        //清除该活动的所有商品
        //goodsService.updateByActivityId(activityId);

        //重设所有活动商品
        Integer[] goodsId = new Integer[goodsIds.size()];
        for (int i = 0; i < goodsId.length; i++) {
            goodsId[i] = goodsIds.get(i);
        }
        /*List<YjGoods> list = goodsService.findAllByGoodsIdIn(goodsId);
        list.forEach(l -> {
            *//*l.setHasActivity(true);
            l.setActivityId(activityId);
            l.setActivityType(activetyType);// 0---普通  1---零售活动 2--批发活动*//*
        });*/
        //goodsService.saveAll(list);

        //清除缓存
        goodsService.cleanActivityGoods();

        return R.ok("保存成功");
    }


    /**
     * 点击结束活动商品   /activity/endActivity 参数 直接为 activityId
     */
    @PostMapping("/endActivity")
    @Transactional
    @CacheEvict(value="sly:xlgshop:activityList",allEntries=true)
    public R endActivity(@RequestBody  Integer activityId) {
      //得到此活动的所有商品

        Activity activity = activityService.findById(activityId);
        activity.setHasRecommend(false);
        activityService.save(activity);

        HashMap param=new HashMap();
        param.put("activityId",activityId);
        List<YjGoods> list = goodsService.findAll(param);
        list.forEach(l -> {
           /* l.setHasActivity(false);
            l.setActivityId(0);
            l.setActivityType(0);// 0---普通  1---零售活动 2--批发活动*/
        });
        goodsService.saveAll(list);
        return  R.ok();
    }

}
