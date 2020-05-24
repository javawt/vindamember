package com.songlanyun.jymall.modules.api.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.mchange.v2.collection.MapEntry;
import com.songlanyun.jymall.common.JpaUtils.SpecificationUtil;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.modules.business.goods.entity.*;
import com.songlanyun.jymall.modules.business.goods.service.*;
import com.songlanyun.jymall.modules.business.jysys.appconfig.entity.AppConfig;
import com.songlanyun.jymall.modules.business.jysys.appconfig.service.AppConfigService;
import com.songlanyun.jymall.modules.business.jysys.banner.entity.Banner;
import com.songlanyun.jymall.modules.business.jysys.banner.service.BannerService;
import com.songlanyun.jymall.modules.business.jysys.dict.service.YjDictService;
import com.songlanyun.jymall.modules.business.jysys.protocol.entity.Protocol;
import com.songlanyun.jymall.modules.business.jysys.protocol.service.ProtocolService;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.entity.UploadFile;
import com.songlanyun.jymall.modules.business.jysys.uploadfile.service.UploadFilesService;
import com.songlanyun.jymall.modules.business.order.entity.Order;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zenghang on 2019/11/21.
 */
@RestController
@RequestMapping("/api")
public class GoodsApiController {
    @Autowired
    private BannerService bannerService;
    @Autowired
    private YjCategoryService categoryService;
    @Autowired
    private YjGoodsService goodsService;
    @Autowired
    private ProtocolService protocolService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private UploadFilesService uploadFileService;

    @Autowired
    private YjGoodsSkuService yjGoodsSkuService;
    @Autowired
    private KDUtil kdUtil;
    @Resource
    private SysAdminDao sysAdminDao;

    @Autowired
    private YjGoodsService yjGoodsService;

    @Autowired
    private YjDictService dictService;
    @Autowired
    private SysUserService sysUserService;


    // 短信模板
    @Value("${sms.template}")
    private String template = "";

    //第一个用户的邀请码
    @Value("${baseInvitation_code}")
    private String fristInvCode;

    @Value("${redis.verCodeTm}")
    private int verCodeTm = 60;
    /*
     * TODO 首页API开始
     */

    /**
     * 获取首页首次加载的所有数据
     */
    @GetMapping("/home")
    public R home() {
        R r = new R();
        //banner图
        Map<String, Object> param = new HashMap<>();
        param.put("page", 1);
        param.put("size", 8);
        r.put("banner", bannerList(param).get("data"));

        //一级分类
        List<YjCategory> categories = (List<YjCategory>) categoryListFirst(new HashMap<>()).get("data");
        r.put("categoryList", categories);

        //普通商品列表
        param.put("page", 1);
        param.put("size", 10);
        //APP默认展示第一个分类下的十件商品信息
        if (null != categories && categories.size() > 0)
            param.put("categoryId", categories.get(0).getCategoryId());

        r.put("goodsList", goodsList(param).get("data"));
        return r;
    }

    /**
     * 获取活动商品
     *
     * @return
     */
    @GetMapping("/activities")
    public R activities() {
        Map<String, Object> param = new HashMap<>();
        param.put("hasRecommend", true);
        param.put("asc", "orderNo");
        List<Activity> activitiesTemp = activityService.findAll(param);
        List<Activity> activities = new ArrayList<>();
        if (null != activitiesTemp && activitiesTemp.size() > 0) {
            activities.add(activitiesTemp.get(0));
        }
//        Pageable pageable = PageRequest.of(0, 4);
        activities.forEach(activity -> {
            Map<String, Object> findActivityGoods = new HashMap<>();
            findActivityGoods.put("hasActivity", true);
            findActivityGoods.put("activityId", activity.getId());
            findActivityGoods.put("desc", "goodsSort");
            findActivityGoods.put("goodsStatus", 10);
            //findActivityGoods.put("page", 1);
            //findActivityGoods.put("size", 4);

            List<YjGoods> goods = goodsService.findAll(new SpecificationUtil<YjGoods>().getSpecification(findActivityGoods),
                    PageRequest.of(0, 100)).getContent();

            activity.setGoodsList(goods);


        });
        return R.ok().put("activities", activities);
    }

    /**
     * 广告轮播图列表
     *
     * @param param page => 页码
     *              size => 数量，不给默认4条
     */
    @GetMapping("/banner/list")
    public R bannerList(@RequestParam Map<String, Object> param) {
        if (!param.containsKey("size") && param.get("size") == null) {
            param.put("size", 4);
        }
        param.put("isDelete", 0);
        Sort sort = new Sort(Sort.Direction.DESC, "priority");
        Pageable pageable = PageRequest.of(0, Integer.parseInt(param.get("size").toString()), sort);
        Page<Banner> pageData = bannerService.findAll((Specification<Banner>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), Integer.parseInt(param.get("size").toString()), 1);
        return R.ok().put("data", pageUtils);
        /*PageUtils pageUtils = bannerService.queryPage(param);
        return R.ok().put("data", pageUtils);*/
    }



    /**
     * 商品分类列表,最顶级
     */
    @GetMapping("/category/list/first")
    public R categoryListFirst(@RequestParam Map<String, Object> param) {
        param.put("isDelete", 0);
        param.put("asc", "sort");
        param.put("parentId", 0);
        return R.ok().put("data", categoryService.findAll(new SpecificationUtil<YjCategory>().getSpecification(param)));
    }

    /**
     * 根据商品分类查询具体商品信息
     *
     * @param param categoryId => 商品分类ID
     *              page => 页码
     *              size => 数量
     */
    @GetMapping("/goods/list")
    public R goodsList(@RequestParam Map<String, Object> param) {
        if (param.get("categoryId") == null) {
            return R.error("缺少分类ID");
        }
        param.put("asc", "goodsSort");
        if (param.containsKey("page") && param.containsKey("size")) {
            List<YjCategory> categories;
            if (StringUtils.isNotBlank(String.valueOf(param.get("categoryId")))) {
                categories = categoryService.findAllByParentId(Integer.parseInt(String.valueOf(param.get("categoryId"))));
            } else {
                categories = new ArrayList<>();
            }
            Pageable pageable = SpecificationUtil.getPageable(param);
            Page<YjGoods> pageData = goodsService.findAll((Specification<YjGoods>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                //in查询，找到所有二级分类下的商品
                if (categories.size() > 0) {
                    Path<Object> path = root.get("categoryId");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    categories.forEach(l -> in.value(l.getCategoryId()));
                    predicates.add(criteriaBuilder.and(in));
                    //tsh  不参与活动商品    12/20
                    predicates.add(criteriaBuilder.equal(root.get("hasActivity"), false));
                    if (null != param.get("goodsName")) {
                        if (StringUtils.isNotBlank(param.get("goodsName").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("goodsName"), param.get("goodsName")));
                        }
                    }
                    predicates.add(criteriaBuilder.equal(root.get("goodsStatus"), 10));

                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);

            PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), pageable.getPageSize(), pageable.getPageNumber());
            return R.ok().put("data", pageUtils);
        }

        return R.error("查询条件中未包含指定参数：page and size");
    }

    /**
     * 根据活动获取活动的商品
     *
     * @param param activityId => 活动TYPE 必传
     *              page => 页码
     *              size => 数量
     */
    @GetMapping("/activity/goods")
    public R activityGoods(@RequestParam Map<String, Object> param) {
        if (null != param) {
            if (null != param.get("type")) {
                Map<String, Object> parMap = new HashMap<>();
                parMap.put("type", param.get("type"));
                PageUtils pageUtils = activityService.queryPage(param);
                List<Activity> actList = (List<Activity>) pageUtils.getList();
                if (null != actList && actList.size() > 0) {
                    for (Activity activity : actList) {
                        Map<String, Object> parMaps = new HashMap<>();
                        parMaps.put("hasActivity", true);
                        parMaps.put("activityId", activity.getId());
                        parMaps.put("goodsStatus", 10);
                        List<YjGoods> goodsList = goodsService.findAll(parMaps);
                        activity.setGoodsList(goodsList);
                        //tsh 2019.12.24 增加批发价
                        if (null != goodsList && goodsList.size() > 0) {
                            for (YjGoods yjGoods : goodsList) {
                                /*List<YjGoodsSku> skuList = sysAdminDao.getGoodsSkuByGoodsId(yjGoods.getGoodsId());
                                if (null != skuList && skuList.size() > 0)
                                    yjGoods.setGoodsSku(skuList.get(0));*/
                            }
                        }

                    }
                }
                return R.ok().put("data", pageUtils);
            } else {
                return R.error("缺少type参数");
            }
        } else {
            return R.error("缺少type参数");
        }
        /*param.put("hasActivity", true);
        PageUtils pageUtils = goodsService.queryPage(param);
        return R.ok().put("data", pageUtils);*/
    }

    /**
     * 热门商品
     *
     * @param param page => 页码
     *              size => 数量
     *              精品
     * @return
     */
    @GetMapping("/popular")
    public R popular(@RequestParam Map<String, Object> param) {
        /*param.put("goodsType", 2);
        param.put("goodsStatus", 10);
        PageUtils pageUtils = goodsService.queryPage(param);*/
        if (param.containsKey("page") && param.containsKey("size")) {
            Pageable pageable = SpecificationUtil.getPageable(param);
            Page<YjGoods> pageData = goodsService.findAll((Specification<YjGoods>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("goodsStatus"), 10));
                predicates.add(criteriaBuilder.like(root.get("goodsType"), "%" + 2 + "%"));
                Path<Object> paths = root.get("goodsAttribute");
                CriteriaBuilder.In<Object> ins = criteriaBuilder.in(paths);
                ins.value("1");
                ins.value("3");
                ins.value("4");
                predicates.add(criteriaBuilder.and(ins));//查组合区
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            List<YjGoods> list = pageData.getContent();
            if (null != list && list.size() > 0) {
                for (YjGoods yjGoods : list) {
                    /*List<YjGoodsSku> skuList = sysAdminDao.getGoodsSkuByGoodsId(yjGoods.getGoodsId());
                    if (null != skuList && skuList.size() > 0)
                        yjGoods.setGoodsSku(skuList.get(0));*/
                }
            }
            PageUtils pageUtils = new PageUtils(list, pageData.getTotalPages(), pageable.getPageSize(), pageable.getPageNumber());
            return R.ok().put("data", pageUtils);
        }

        return R.error("查询条件中未包含指定参数：page and size");
    }

    /**
     * 推荐精品
     *
     * @param param page => 页码
     *              size => 数量
     * @return
     */
    @GetMapping("/new/product")
    public R newProduct(@RequestParam Map<String, Object> param) {
        /*param.put("goodsType", 1);
        param.put("goodsStatus", 10);
        PageUtils pageUtils = goodsService.queryPage(param);
        return R.ok().put("data", pageUtils);*/

        if (param.containsKey("page") && param.containsKey("size")) {
            Pageable pageable = SpecificationUtil.getPageable(param);
            Page<YjGoods> pageData = goodsService.findAll((Specification<YjGoods>) (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("goodsStatus"), 10));
                //predicates.add(criteriaBuilder.equal(root.get("goodsType"), 1));
                predicates.add(criteriaBuilder.like(root.get("goodsType"), "%" + 1 + "%"));
                Path<Object> paths = root.get("goodsAttribute");
                CriteriaBuilder.In<Object> ins = criteriaBuilder.in(paths);
                ins.value("1");
                ins.value("3");
                ins.value("4");
                predicates.add(criteriaBuilder.and(ins));//查组合区
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            //List<YjGoods> list = pageData.getContent().stream().peek(l -> l.setContent("")).collect(Collectors.toList());
            List<YjGoods> list = pageData.getContent();
            if (null != list && list.size() > 0) {
                for (YjGoods yjGoods : list) {
                   /* List<YjGoodsSku> skuList = sysAdminDao.getGoodsSkuByGoodsId(yjGoods.getGoodsId());
                    if (null != skuList && skuList.size() > 0)
                        yjGoods.setGoodsSku(skuList.get(0));*/
                }
            }
            PageUtils pageUtils = new PageUtils(list, pageData.getTotalPages(), pageable.getPageSize(), pageable.getPageNumber());
            return R.ok().put("data", pageUtils);
        }

        return R.error("查询条件中未包含指定参数：page and size");
    }

    /*
     * TODO 首页API结束
     *
     * TODO 分类API开始
     */

    /**
     * 商品分类列表,二级
     *
     * @param param parentId => 上级ID
     */
    @GetMapping("/category/list/second")
    public R categoryListSecond(@RequestParam Map<String, Object> param) {
        if (param.get("parentId") == null) {
            return R.error("缺少上级ID");
        }

        param.put("isDelete", 0);
        param.put("asc", "sort");
        param.put("parentId", param.get("parentId"));
        List<YjCategory> second = categoryService.findAll(new SpecificationUtil<YjCategory>().getSpecification(param));

        List<YjCategory> listAll = categoryService.findAll();
        second.forEach(category -> category.setChildren(
                listAll.stream().filter(l -> l.getParentId().equals(category.getCategoryId())).collect(Collectors.toList())
        ));

        return R.ok().put("data", second);
    }

    /**
     * 商品搜索 只针对组合区商品
     *
     * @param param page => 页码
     *              size => 数量
     *              <p>
     *              可选值
     *              goodsName => 商品名称
     *              categoryId => 商品分类ID
     */
    @GetMapping("/goods/list/search")
    public R goodsListSearch(@RequestParam Map<String, Object> param) {
        if (param.containsKey("page") && param.containsKey("size")) {

            List<YjCategory> categories;
            if (param.get("categoryId") != null && StringUtils.isNotBlank(param.get("categoryId").toString())) {
                categories = categoryService.findAllByParentId(Integer.parseInt(String.valueOf(param.get("categoryId"))));
                if (categories.size() == 0) {
                    YjCategory yjCategory = new YjCategory();
                    yjCategory.setCategoryId(Integer.parseInt(String.valueOf(param.get("categoryId"))));
                    categories.add(yjCategory);
                }
            } else {
                categories = new ArrayList<>();
            }

            param.remove("categoryId");
            Pageable pageable = SpecificationUtil.getPageable(param);


            Page<YjGoods> pageData = goodsService.findAll((Specification<YjGoods>) (root, query, criteriaBuilder) -> {

                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("goodsStatus"), 10));
                //predicates.add(criteriaBuilder.equal(root.get("hasActivity"), false));
                //predicates.add(criteriaBuilder.isNull(root.get("activityId")));
                if (param.containsKey("activityType") && StringUtils.isNotBlank(String.valueOf(param.get("activityType")))) {
                    //0 组合 1 精选 2 易货     0组合1精品2易货3组合+精品
                    Path<Object> paths = root.get("goodsAttribute");
                    CriteriaBuilder.In<Object> ins = criteriaBuilder.in(paths);
                    if (param.get("activityType").equals("1")) {
                        ins.value("1");
                        ins.value("3");
                        ins.value("4");
                        predicates.add(criteriaBuilder.and(ins));//查组合区
                    } else if (param.get("activityType").equals("2")) {
                        ins.value("2");
                        ins.value("4");
                        predicates.add(criteriaBuilder.and(ins));//查组合区
                    } else {
                        ins.value("0");
                        ins.value("3");
                        predicates.add(criteriaBuilder.and(ins));//查组合区
                    }
                    param.remove("goodsAttribute");
                }
                if (param.containsKey("goodsName") && StringUtils.isNotBlank(String.valueOf(param.get("goodsName")))) {
                    predicates.add(criteriaBuilder.like(root.get("goodsName"), "%" + String.valueOf(param.get("goodsName")) + "%"));
                    param.remove("goodsName");
                }

                //in查询，找到所有二级分类下的商品
                if (categories.size() > 0) {
                    Path<Object> path = root.get("categoryId");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    categories.forEach(l -> in.value(l.getCategoryId()));
                    predicates.add(criteriaBuilder.and(in));
                }

                //商品价格区间
                if (param.containsKey("startPrice") && param.containsKey("endPrice")) {
                    if (StringUtils.isNotBlank(param.get("startPrice").toString()) && StringUtils.isNotBlank(param.get("endPrice").toString()))
                        SpecificationUtil.between(predicates, "price", root, criteriaBuilder, Double.valueOf(String.valueOf(param.get("startPrice"))), Double.valueOf(String.valueOf(param.get("endPrice"))));
                }
                param.remove("startPrice");
                param.remove("endPrice");
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    Map.Entry<String, Object> par=new MapEntry("goodsSort","goodsSort");
                    SpecificationUtil.setDesc(par, root, query, criteriaBuilder);
                    if ("asc".equals(entry.getKey())) {
                        SpecificationUtil.setAsc(entry, root, query, criteriaBuilder);
                        continue;
                    }

                    if ("desc".equals(entry.getKey())) {
                        SpecificationUtil.setDesc(entry, root, query, criteriaBuilder);
                        continue;
                    }
                    /*if (StringUtils.isNotBlank(String.valueOf(entry.getValue()))) {
                        predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                    }*/


                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }, pageable);
            List<YjGoods> yjGoodsList = pageData.getContent();
            if (null != yjGoodsList && yjGoodsList.size() > 0) {
                for (YjGoods yjGoods : yjGoodsList) {
                    /*List<YjGoodsSku> yjGoodsSkuList = sysAdminDao.getGoodsSkuByGoodsId(yjGoods.getGoodsId());
                    if (null != yjGoodsSkuList && yjGoodsSkuList.size() > 0) {
                        yjGoods.setGoodsSku(yjGoodsSkuList.get(0));
                    }*/
                }
            }
            //pageData.getContent().stream().peek(l -> l.setContent("")).collect(Collectors.toList());
            PageUtils pageUtils = new PageUtils(pageData.getContent(), pageData.getTotalPages(), pageable.getPageSize(), pageable.getPageNumber());
            return R.ok().put("data", pageUtils);
        }

        return R.error("查询条件中未包含指定参数：page and size");
    }

    /*
     * TODO 分类API结束
     */

    /**
     * 根据类型获取协议
     *
     * @param type 协议类型
     */
    @GetMapping("/protocol/{type}")
    public R protocolByType(@PathVariable("type") String type) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        List<Protocol> protocols = protocolService.findAll(new SpecificationUtil<Protocol>().getSpecification(param));
        return R.ok().put("data", protocols);
    }

    /**
     * 商品详情信息接口
     */
    @RequestMapping("/goods/yjgoods/info/{goodsId}")
    //@RequiresPermissions("goods:yjgoods:info")
    public R info(@PathVariable("goodsId") Integer goodsId) {
        YjGoods yjGoods = goodsService.findById(goodsId);
        if (null == yjGoods) {
            return R.error("商品不存在");
        }
       /* if (yjGoods.getGoodsStatus() == 20) {
            return R.error("商品已下架");
        }*/
        /*if (null != yjGoods && null != yjGoods.getActivityId()) {
            //展示最新活动2020.2.3零时
            yjGoods.setActivity(sysAdminDao.getNewActivity());

        }*/
        /*List<YjGoodsSku> yjGoodsSkuList = sysAdminDao.getGoodsSkuByGoodsId(yjGoods.getGoodsId());
        if (null != yjGoodsSkuList && yjGoodsSkuList.size() > 0) {
            yjGoods.setGoodsSku(yjGoodsSkuList.get(0));
        }*/
        return R.ok().put("yjGoods", yjGoods);
    }


    /**
     * app配置列表
     */
    @GetMapping("/appConfig/list")
    public R list() {
        Sort sort = new Sort(Sort.Direction.DESC, "number");
        Pageable pageable = PageRequest.of(0, 1, sort);
        Page<AppConfig> pageData = appConfigService.findAll((Specification<AppConfig>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        PageUtils pageUtils = new PageUtils(pageData.getContent(), (int) pageData.getTotalElements(), 1, 1);
        return R.ok().put("data", pageUtils);

    }

    //查询上传图片信息

    @RequestMapping("/getImg/{imgId}")
    public R find(@PathVariable("imgId") Integer imgId) {
        UploadFile uploadFile = uploadFileService.findById(imgId);
        return R.ok().put("data", uploadFile.getFileUrl());
    }

    /**
     * 根据商品id 得到其对应 sku
     **/
    @RequestMapping("/getGoodsSku/{goodsId}")
    public R getGoodsSku(@PathVariable("goodsId") int goodsId) {
        HashMap param = new HashMap();
        param.put("goodsId", goodsId);
        List<YjGoodsSku> resList = yjGoodsSkuService.findAll(param);
        if (null != resList && resList.size() > 0) {
            for (YjGoodsSku yjGoodsSku : resList) {
                String temp = "";
                if (null != yjGoodsSku) {
                   /* if (StringUtils.isNotBlank(yjGoodsSku.getSpecSkuId())) {
                        temp = yjGoodsSku.getSpecSkuId().replaceAll("，", ",");
                    }*/
                }
                if (StringUtils.isNotBlank(temp)) {
                    String vl = "";
                    List<String> result = Arrays.asList(temp.split(","));
                    if (null != result && result.size() > 0) {
                        for (String s : result) {
                            /*if (StringUtils.isNotBlank(vl)) {
                                vl = vl + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                            } else {
                                vl = vl + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                            }*/
                        }
                    }
                    yjGoodsSku.setSpecValue(vl);
                }
            }
        }
        return R.ok().put("yjGoodsSku", resList);
    }


    /**
     * 根据商品id 得到其对应 sku新增需求接口
     **/
    @RequestMapping("/getGoodsSkuList/{goodsId}")
    public R getGoodsSkuList(@PathVariable("goodsId") int goodsId) {
        YjGoods yjGoods = yjGoodsService.findById(goodsId);
        HashMap param = new HashMap();
        param.put("goodsId", goodsId);
        /*List<YjSpecRel> specRelList = yjSpecRelService.findAll(param);//商品属性关系表
        if (null != specRelList && specRelList.size() > 0) {
            Set<Integer> set = new HashSet<>();//存放speciD
            for (YjSpecRel yjSpecRel : specRelList) {
                set.add(yjSpecRel.getSpecId());
            }
            if (null != set && set.size() > 0) {
                List<YjSpec> yjSpecList = new ArrayList<>();
                for (Integer str : set) {
                    YjSpec yjSpec = yjSpecService.findById(str);
                    Map map = new HashMap();
                    map.put("goodsId", goodsId);
                    map.put("specId", str);
                    List<YjSpecRel> yjSpecRelList = yjSpecRelService.findAll(map);
                    List<YjSpecValue> specValueList = new ArrayList<>();
                    if (null != yjSpecRelList && yjSpecRelList.size() > 0) {
                        for (YjSpecRel yjSpecRel : yjSpecRelList) {
                            specValueList.add(yjSpecValueService.findById(yjSpecRel.getSpecValueId()));
                        }
                    }
                    yjSpec.setSpecValueList(specValueList);
                    yjSpecList.add(yjSpec);

                }
                yjGoods.setYjSpecList(yjSpecList);
            }

        }*/


        return R.ok().put("yjGoods", yjGoods);
    }

    @RequestMapping("/getGoodsSkuPrice")
    public R getGoodsSkuPrice(@RequestBody GoodsApiPar goodsApiPar) {
        Map map = new HashMap();
        map.put("goodsId", goodsApiPar.getGoodsId());
        List<String> list = new ArrayList<>();
        if (null != goodsApiPar.getGoodsSku()) {
            list = Arrays.asList(goodsApiPar.getGoodsSku().split(","));
        }
        List<YjGoodsSku> yjGoodsSkuList = yjGoodsSkuService.findAll(map);
        if (null != yjGoodsSkuList && yjGoodsSkuList.size() > 0) {
            if (null != list && list.size() > 0) {
                for (YjGoodsSku yjGoodsSku : yjGoodsSkuList) {
                    /*if (StringUtils.isNotBlank(yjGoodsSku.getSpecSkuId())) {
                        List<String> tempList = Arrays.asList(yjGoodsSku.getSpecSkuId().split(","));
                        if (list.containsAll(tempList) && tempList.containsAll(list)) {
                            return R.ok().put("data", yjGoodsSku);
                        }
                    }*/
                }
            }
        }
        return R.error("商品sku不存在");
    }


    /**
     * 快递查询接口
     */

    @RequestMapping("/selectKD/{expNo}")
    public R orderWholesaleconsign(@PathVariable("expNo") String expNo) throws Exception {
        try {
            String expCodeInfo = kdUtil.getOrderTracesByJson(expNo);
            JSONObject expCodeInfoObject = JSONObject.parseObject(expCodeInfo);
            KDCode kdCode = expCodeInfoObject.parseObject(expCodeInfo, KDCode.class);
            String expCode = kdCode.getShippers().get(0).getShipperCode();
            String returnInfo = kdUtil.getOrderTracesByJson(expCode, expNo);
            JSONObject jsonObject = JSONObject.parseObject(returnInfo);
            KDInfo kdInfo = jsonObject.parseObject(returnInfo, KDInfo.class);
            kdInfo.setKdInfo(kdCode); //保存物流信息
            if (null == kdInfo) {
                return R.error("查询失败");
            }
            return R.ok("查询成功").put("data", kdInfo);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }

    //组合区订单分享红包是否满
    @RequestMapping("/checkRed/{orderNo}")
    public R checkRed(@PathVariable("orderNo") String orderNo) {
        //1.判断订单是否可分享
        Order order = sysAdminDao.isExitShareOrder(orderNo);
        if (null == order) {
            return R.ok().put("data", 0);

        } else {
            if (order.getRedPacket() <= Integer.parseInt(dictService.getDictListByType(15).get(0).getDvalue())) {
                //生成红包金额
                Double maxMoney = Double.parseDouble(dictService.getDictListByType(17).get(0).getDvalue());
                Double minMoney = Double.parseDouble(dictService.getDictListByType(16).get(0).getDvalue());
                Double a, b;//[a,b]
                if (maxMoney >= minMoney) {
                    a = minMoney;
                    b = maxMoney;
                } else {
                    a = maxMoney;
                    b = minMoney;
                }
                Double num = a + (int) (Math.random() * (b - a + 1));
                BigDecimal.valueOf(num).setScale(2, BigDecimal.ROUND_HALF_UP);
                return R.ok().put("data", BigDecimal.valueOf(num).setScale(2, BigDecimal.ROUND_HALF_UP));
            } else {
                return R.ok().put("data", 0);
            }
        }
    }


    //组合区订单分享领取红包接口
    /*@RequestMapping("/receiveRed")
    @Transactional
    public R receiveRed(SysUserEntity _userVo, String orderNo, Double money) {
        if (null == money) {
            money = 0.0;
        }
        R resp = R.error(StatusMsgEnum.USER_IS_EXISTS);
        if (StringUtils.isBlank(_userVo.getUsername())) {
            return R.error("手机号不能为空");
        }
        if (StringUtils.isBlank(_userVo.getPassword())) {
            return R.error("密码不能为空");
        }
        if (StringUtils.isBlank(_userVo.getInvitationCode())) {
            return R.error("邀请码不能为空");
        }

        String userInvCode = _userVo.getInvitationCode();
        SysUserEntity userVo = sysUserService.queryByUserName(_userVo.getUsername());
        if (userVo != null) {  //表示是新增
            return resp;
        }
        //检查验证码是否正确或过期是否正确 ---测试期间屏蔽
        int chkRes = sysUserService.checkVerCode(_userVo.getUsername(), _userVo.getVerCode());
        if (chkRes == -1) { //redis 已经没有此phone的对应验证码了，
            return R.error(StatusMsgEnum.SMSCODESEND_TIMEOUT);
        }
        if (chkRes == -2) {  //验证码录入错误
            return R.error(StatusMsgEnum.SMSCODES_NOMATCH);
        }
        //从用户表找出此邀请码对应的用户来检查邀请是否正确
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Invitation_code", _userVo.getInvitationCode());
        SysUserEntity invCodeUser = sysUserDao.selectOne(queryWrapper);
        if (invCodeUser != null) {  //根据邀请码找到了人，则将其userId写入
            _userVo.setReferee(invCodeUser.getUserId());
        }
        if (invCodeUser == null && (!userInvCode.equals(fristInvCode))) {  //人也找不到，录入的邀请码也不是第一个，报错
            return R.error(StatusMsgEnum.INVICTCODE_ERR);
        }

        try {
            //保存新进此人  ---手机号生成邀请码
            //String shareCode = ShareCodeUtils.idToCode(Long.parseLong(_userVo.getUsername()));
            //tsh 2019-12-23 生成邀请码
            String shareCode = greateInvitation();
            _userVo.setInvitationCode(shareCode);
            //_userVo.setType(1);
            //_userVo.setStatus(1);
            //注册用户并发放红包
            _userVo.setBalance(BigDecimal.valueOf(money));//用户钱包
            sysUserService.saveUser(_userVo);
            if (StringUtils.isNotBlank(orderNo)) {
                //更新订单领取次数
                sysAdminDao.updateOrderRedPack(orderNo);
                //支付订单汇总
                PaymentSummary paymentSummary = new PaymentSummary();
                paymentSummary.setUserId(sysAdminDao.getUserIdByMobile(_userVo.getUsername()) == null ? Long.parseLong(_userVo.getUsername()) : sysAdminDao.getUserIdByMobile(_userVo.getUsername()));
                paymentSummary.setRelationId(orderService.findById(orderNo).getUserId());
                paymentSummary.setPaymentType(1);//0收入 1支出
                paymentSummary.setOrderNo(orderNo);
                paymentSummary.setOperaType(5);//1寄售2订单支付3推荐奖4分红5红包6转账7提现8退款处理9升级代理商
                paymentSummary.setMoney(BigDecimal.valueOf(money).setScale(2, BigDecimal.ROUND_HALF_UP));
                paymentSummary.setBeforeMoney(new BigDecimal(0));
                paymentSummary.setAfterMoney(BigDecimal.valueOf(money).setScale(2, BigDecimal.ROUND_HALF_UP));
                paymentSummary.setPayType(1);//1余额2微信3支付宝4易货币
                paymentSummary.setRemark("领取红包奖励金额:" + paymentSummary.getMoney() + "元");
                sysAdminDao.savePaymentSummary(paymentSummary);
            }
            resp = R.ok("用户注册成功！");
        } catch (Exception err) {
            resp = R.error(StatusMsgEnum.NOCAN_REGISTER);
        }
        return resp;

    }
    */

    //递归生成要求的六位邀请码，比对无重复方可
    public String greateInvitation() {
        String invCode = ShareCodeUtils.createInvitation();
        if (sysUserService.isExistInvition(invCode) > 0) {
            //用户邀请码重复
            greateInvitation();
        }
        return invCode;
    }
}
