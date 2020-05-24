package com.songlanyun.jymall.modules.business.order.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.songlanyun.jymall.common.JpaUtils.SpecificationUtil;
import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.config.AlipayConfigs;
import com.songlanyun.jymall.config.PayCommonUtil;
import com.songlanyun.jymall.modules.business.goods.entity.*;
import com.songlanyun.jymall.modules.business.goods.service.*;
import com.songlanyun.jymall.modules.business.jysys.address.entity.Address;
import com.songlanyun.jymall.modules.business.jysys.address.service.AddressService;
import com.songlanyun.jymall.modules.business.jysys.dict.entity.YjDictEntity;
import com.songlanyun.jymall.modules.business.jysys.dict.service.YjDictService;
import com.songlanyun.jymall.modules.business.order.entity.Order;
import com.songlanyun.jymall.modules.business.order.entity.OrderAddress;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import com.songlanyun.jymall.modules.business.order.model.DeliverModel;
import com.songlanyun.jymall.modules.business.order.model.OrderModel;
import com.songlanyun.jymall.modules.business.order.model.PaymentModel;
import com.songlanyun.jymall.modules.business.order.model.RefundModel;
import com.songlanyun.jymall.modules.business.order.service.OrderAddressService;
import com.songlanyun.jymall.modules.business.order.service.OrderGoodsService;
import com.songlanyun.jymall.modules.business.order.service.OrderService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import com.songlanyun.jymall.modules.business.sys.service.SysAdminService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:36
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderAddressService orderAddressService;

    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private YjGoodsService goodsService;
    @Autowired
    private YjGoodsSkuService goodsSkuService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ExpressService expressService;

    @Autowired
    private YjDictService dictService;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private AlipayConfigs alipayConfigs;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CartService cartService;


    @Resource
    private StatisticalService statisticalService;
    @Resource
    private SysAdminService sysAdminService;
    @Resource
    private YjGoodsSkuService yjGoodsSkuService;
    @Autowired
    private YjSpecValueService yjSpecValueService;
    @Resource
    private SysAdminDao sysAdminDao;

    /*@Autowired
    private RedisTemplate redisTemplate;*/

    @Value("${order.expire.time}")
    private String orderExpireTime;

    /**
     * + app 订单列表 也是此接口
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");

        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");

            if (!ShiroUtils.getUserId().equals(1L)) {
                params.put("userId", ShiroUtils.getUserId());
            }

            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<Order> pageData = orderService.findAll((Specification<Order>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    //in查询
                    if (null != params.get("orderStatus")) {
                        if (params.get("orderStatus").equals("50") || params.get("orderStatus").equals("80")) {
                            Path<Object> path = root.get("orderStatus");
                            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                            in.value(50);//待发货
                            in.value(80);//待发货
                            predicates.add(criteriaBuilder.and(in));
                            params.remove("orderStatus");
                        }
                    }
                    if (null != params.get("orderNo")) {
                        if (StringUtils.isNotBlank(params.get("orderNo").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("orderNo"), params.get("orderNo")));
                            params.remove("orderNo");
                        }
                    }
                    if (null != params.get("userId")) {
                        if (!ShiroUtils.getUserId().equals(1L)) {
                            predicates.add(criteriaBuilder.equal(root.get("userId"), params.get("userId")));
                            params.remove("userId");
                        }
                    }
                    //时间区间查询
                    if (params.get("startTime") != null && params.get("endTime") != null && StringUtils.isNotBlank(String.valueOf(params.get("startTime"))) && StringUtils.isNotBlank(String.valueOf(params.get("endTime")))) {
                        Date startTime = new Date(Long.parseLong(String.valueOf(params.get("startTime"))));
                        Date endTime = new Date(Long.parseLong(String.valueOf(params.get("endTime"))));
                        SpecificationUtil.between(predicates, "createTime", root, criteriaBuilder, startTime, endTime);
                        params.remove("startTime");
                        params.remove("endTime");
                    }

                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        //排序字段
                        if ("asc".equals(entry.getKey())) {
                            SpecificationUtil.setAsc(entry, root, query, criteriaBuilder);
                            continue;
                        }

                        if ("desc".equals(entry.getKey())) {
                            SpecificationUtil.setDesc(entry, root, query, criteriaBuilder);
                            continue;
                        }

                        //查询字段
                        if (StringUtils.isNotBlank(String.valueOf(entry.getValue()))) {
                            if ("username".equals(entry.getKey()) || "orderNo".equals(entry.getKey())) {
                                predicates.add(criteriaBuilder.like(root.get(entry.getKey()), "%" + entry.getValue() + "%"));
                            } else {
                                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                            }
                        }
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Order> orders = pageData.getContent();
                //**********陶树华  2019/12/21 10:53 订单列表增加地址信息*********
                if (null != orders && orders.size() > 0) {
                    for (Order order : orders) {
                        order.setOrderAddress(statisticalService.getOrderAddressByOrderNo(order.getOrderNo()));
                        List<OrderGoods> orderGoodsList = orderGoodsService.findAllByOrderNo(order.getOrderNo());
                        if (null != orderGoodsList && orderGoodsList.size() > 0) {
                            for (OrderGoods orderGoods : orderGoodsList) {
                                //1. 查找此商品对应的 sku
                                YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                                orderGoods.setYjGoodsSku(yjGoodsSku);
                                orderGoods.setContent(null);
                                //查询skuValue值
                                if (null != yjGoodsSku) {
                                    // 2. 得到规格型号 specSkuid
                                    /*String specSkuIDS = yjGoodsSku.getSpecSkuId();
                                    if (StringUtils.isNotBlank(specSkuIDS)) {
                                        List<String> contents = Arrays.asList(specSkuIDS.split(","));
                                        //3 . 分割 此商品sku
                                        if (null != contents && contents.size() > 0) {
                                            for (String s : contents) {
                                                if (StringUtils.isNotBlank(orderGoods.getSpecValue())) {
                                                    orderGoods.setSpecValue(orderGoods.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                } else {
                                                    //20200409 tsh 针对商品sku值为空
                                                    if(null==yjSpecValueService.findById(Integer.parseInt(s))){
                                                        orderGoods.setSpecValue(null);
                                                    }else{
                                                        orderGoods.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                    }

                                                }
                                            }
                                        }
                                    }*/
                                }
                            }

                        }
                        order.setOrderGoods(orderGoodsList);


                        /*OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                        order.setOrderGoods(orderGoods);*/
                    }
                }


                PageUtils pageUtils = new PageUtils(orders, (int) pageData.getTotalElements(), page, size);
                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {
                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    /**
     * 列表 --- 后台管理系统查询
     */
    @RequestMapping("/adminlist")
    public R adminlist(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");

        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");


            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<Order> pageData = orderService.findAll((Specification<Order>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    //in查询
                    if (null != params.get("orderStatus")) {
                        if (params.get("orderStatus").equals("50") || params.get("orderStatus").equals("80")) {
                            Path<Object> path = root.get("orderStatus");
                            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                            in.value(50);
                            in.value(80);
                            predicates.add(criteriaBuilder.and(in));
                            params.remove("orderStatus");
                        }
                    }
                    if (null != params.get("dealersId")) {
                        if (StringUtils.isNotBlank(params.get("dealersId").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("dealersId"), params.get("dealersId")));
                            params.remove("dealersId");
                        }
                    }
                    if (null != params.get("orderNo")) {
                        if (StringUtils.isNotBlank(params.get("orderNo").toString())) {
                            predicates.add(criteriaBuilder.equal(root.get("orderNo"), params.get("orderNo")));
                            params.remove("orderNo");
                        }
                    }
                    //时间区间查询
                    if (params.get("startTime") != null && params.get("endTime") != null && StringUtils.isNotBlank(String.valueOf(params.get("startTime"))) && StringUtils.isNotBlank(String.valueOf(params.get("endTime")))) {
                        Date startTime = new Date(Long.parseLong(String.valueOf(params.get("startTime"))));
                        Date endTime = new Date(Long.parseLong(String.valueOf(params.get("endTime"))));
                        SpecificationUtil.between(predicates, "createTime", root, criteriaBuilder, startTime, endTime);

                        params.remove("startTime");
                        params.remove("endTime");
                    }

                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        //排序字段
                        if ("asc".equals(entry.getKey())) {
                            SpecificationUtil.setAsc(entry, root, query, criteriaBuilder);
                            continue;
                        }

                        if ("desc".equals(entry.getKey())) {
                            SpecificationUtil.setDesc(entry, root, query, criteriaBuilder);
                            continue;
                        }

                        //查询字段
                        if (StringUtils.isNotBlank(String.valueOf(entry.getValue()))) {
                            if ("username".equals(entry.getKey()) || "orderNo".equals(entry.getKey())) {
                                predicates.add(criteriaBuilder.like(root.get(entry.getKey()), "%" + entry.getValue() + "%"));
                            } else {
                                predicates.add(criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()));
                            }
                        }
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Order> orders = pageData.getContent();
                if (null != orders && orders.size() > 0) {
                    for (Order order : orders) {
                        order.setOrderAddress(statisticalService.getOrderAddressByOrderNo(order.getOrderNo()));
                        List<OrderGoods> orderGoodsList = orderGoodsService.findAllByOrderNo(order.getOrderNo());
                        if (null != orderGoodsList && orderGoodsList.size() > 0) {
                            for (OrderGoods orderGoods : orderGoodsList) {
                                YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                                orderGoods.setYjGoodsSku(yjGoodsSku);
                                orderGoods.setContent(null);
                                //查询skuValue值
                                if (null != yjGoodsSku) {
                                    /*String specSkuIDS = yjGoodsSku.getSpecSkuId();
                                    if (StringUtils.isNotBlank(specSkuIDS)) {
                                        List<String> contents = Arrays.asList(specSkuIDS.split(","));
                                        if (null != contents && contents.size() > 0) {
                                            for (String s : contents) {
                                                if (StringUtils.isNotBlank(orderGoods.getSpecValue())) {
                                                    orderGoods.setSpecValue(orderGoods.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                } else {
                                                    orderGoods.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                }
                                            }
                                        }
                                    }*/
                                }
                            }

                        }
                        order.setOrderGoods(orderGoodsList);
                    }
                }


                PageUtils pageUtils = new PageUtils(orders, (int) pageData.getTotalElements(), page, size);
                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {
                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    //组合零售待提货
    @GetMapping("/pklist")
    public R pklist(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");

        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");

            if (!ShiroUtils.getUserId().equals(1L)) {
                params.put("userId", ShiroUtils.getUserId());
            }

            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<Order> pageData = orderService.findAll((Specification<Order>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    //in查询
                    Path<Object> path = root.get("orderStatus");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    in.value(Order.xlgOrderStatus.RECEIVED);//待提货
                    predicates.add(criteriaBuilder.and(in));
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Order> orders = pageData.getContent();
                //**********陶树华  2019/12/21 10:53 订单列表增加地址信息*********
                if (null != orders && orders.size() > 0) {
                    for (Order order : orders) {
                        order.setOrderAddress(statisticalService.getOrderAddressByOrderNo(order.getOrderNo()));
                        /*List<OrderGoods> orderGoodsList=  orderGoodsService.findAllByOrderNo(order.getOrderNo());
                        if(null!=orderGoodsList && orderGoodsList.size()>0){
                            for(OrderGoods orderGoods:orderGoodsList){
                                YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                                orderGoods.setYjGoodsSku(yjGoodsSku);
                                //查询skuValue值
                                if (null != yjGoodsSku) {
                                    String specSkuIDS = yjGoodsSku.getSpecSkuId();
                                    if (StringUtils.isNotBlank(specSkuIDS)) {
                                        List<String> contents = Arrays.asList(specSkuIDS.split(","));
                                        if (null != contents && contents.size() > 0) {
                                            for (String s : contents) {
                                                if (StringUtils.isNotBlank(orderGoods.getSpecValue())) {
                                                    orderGoods.setSpecValue(orderGoods.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                } else {
                                                    orderGoods.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }*/


                        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                        order.setOrderGoodsTH(orderGoods);
                        //-----------
                        YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
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
                        order.setYjGoodsSku(yjGoodsSku);
                        //---------
                    }
                }


                PageUtils pageUtils = new PageUtils(orders, (int) pageData.getTotalElements(), page, size);
                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {
                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }


    //组合零售未发货
    @GetMapping("/unDeliverlist")
    public R unDeliverlist(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");

        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");

            if (!ShiroUtils.getUserId().equals(1L)) {
                params.put("userId", ShiroUtils.getUserId());
            }

            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<Order> pageData = orderService.findAll((Specification<Order>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    //in查询
                    Path<Object> path = root.get("orderStatus");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    in.value(Order.xlgOrderStatus.CANCEL);//待发货
                    predicates.add(criteriaBuilder.and(in));
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Order> orders = pageData.getContent();
                //**********陶树华  2019/12/21 10:53 订单列表增加地址信息*********
                if (null != orders && orders.size() > 0) {
                    for (Order order : orders) {
                        order.setOrderAddress(statisticalService.getOrderAddressByOrderNo(order.getOrderNo()));
                        /*List<OrderGoods> orderGoodsList=  orderGoodsService.findAllByOrderNo(order.getOrderNo());
                        if(null!=orderGoodsList && orderGoodsList.size()>0){
                            for(OrderGoods orderGoods:orderGoodsList){
                                YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                                orderGoods.setYjGoodsSku(yjGoodsSku);
                                //查询skuValue值
                                if (null != yjGoodsSku) {
                                    String specSkuIDS = yjGoodsSku.getSpecSkuId();
                                    if (StringUtils.isNotBlank(specSkuIDS)) {
                                        List<String> contents = Arrays.asList(specSkuIDS.split(","));
                                        if (null != contents && contents.size() > 0) {
                                            for (String s : contents) {
                                                if (StringUtils.isNotBlank(orderGoods.getSpecValue())) {
                                                    orderGoods.setSpecValue(orderGoods.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                } else {
                                                    orderGoods.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }*/


                        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                        order.setOrderGoodsTH(orderGoods);
                        //-----------
                        YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
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
                                   /* if (StringUtils.isNotBlank(vl)) {
                                        vl = vl + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                                    } else {
                                        vl = vl + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                                    }*/
                                }
                            }
                            yjGoodsSku.setSpecValue(vl);
                        }
                        order.setYjGoodsSku(yjGoodsSku);
                        //---------
                    }
                }


                PageUtils pageUtils = new PageUtils(orders, (int) pageData.getTotalElements(), page, size);
                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {
                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    //组合零售已发货
    @GetMapping("/deliverlist")
    public R deliverlist(@RequestParam Map<String, Object> params) {
        params.put("desc", "createTime");

        if (params.containsKey("page") && params.containsKey("size")) {
            int page = Integer.parseInt(String.valueOf(params.get("page")));
            int size = Integer.parseInt(String.valueOf(params.get("size")));

            params.remove("page");
            params.remove("size");

            if (!ShiroUtils.getUserId().equals(1L)) {
                params.put("userId", ShiroUtils.getUserId());
            }

            Pageable pageable = PageRequest.of(page - 1, size);
            try {
                Page<Order> pageData = orderService.findAll((Specification<Order>) (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    //in查询
                    Path<Object> path = root.get("orderStatus");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                    in.value(Order.xlgOrderStatus.PAYMENT_FAILED);//待收货
                    predicates.add(criteriaBuilder.and(in));
                    predicates.add(criteriaBuilder.equal(root.get("userId"), ShiroUtils.getUserId()));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                }, pageable);

                List<Order> orders = pageData.getContent();
                //**********陶树华  2019/12/21 10:53 订单列表增加地址信息*********
                if (null != orders && orders.size() > 0) {
                    for (Order order : orders) {
                        order.setOrderAddress(statisticalService.getOrderAddressByOrderNo(order.getOrderNo()));
                        /*List<OrderGoods> orderGoodsList=  orderGoodsService.findAllByOrderNo(order.getOrderNo());
                        if(null!=orderGoodsList && orderGoodsList.size()>0){
                            for(OrderGoods orderGoods:orderGoodsList){
                                YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                                orderGoods.setYjGoodsSku(yjGoodsSku);
                                //查询skuValue值
                                if (null != yjGoodsSku) {
                                    String specSkuIDS = yjGoodsSku.getSpecSkuId();
                                    if (StringUtils.isNotBlank(specSkuIDS)) {
                                        List<String> contents = Arrays.asList(specSkuIDS.split(","));
                                        if (null != contents && contents.size() > 0) {
                                            for (String s : contents) {
                                                if (StringUtils.isNotBlank(orderGoods.getSpecValue())) {
                                                    orderGoods.setSpecValue(orderGoods.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                } else {
                                                    orderGoods.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }*/


                        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                        order.setOrderGoodsTH(orderGoods);
                        //-----------
                        YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                        String temp = "";
                        if (null != yjGoodsSku) {
                            /*if (StringUtils.isNotBlank(yjGoodsSku.getSpecSkuId())) {
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
                        order.setYjGoodsSku(yjGoodsSku);
                        //---------
                    }
                }


                PageUtils pageUtils = new PageUtils(orders, (int) pageData.getTotalElements(), page, size);
                return R.ok().put("data", pageUtils);

            } catch (InvalidDataAccessApiUsageException ex) {
                ex.printStackTrace();
                throw new RRException("异常查询条件");
            }
        }

        throw new RRException("查询条件中未包含指定参数：page and size");
    }

    /**
     * 信息  ------ 查询订单详情 ---
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        Order order = orderService.findById(id);
        if (order == null) {
            return R.error("未知订单");
        }

        //如果是退款--退货，如果有订单，将退款退货信息补上去,只包含退款退货信息不含商品信息


        //将订单商品信息补上
        List<OrderGoods> orderGoodsList = orderGoodsService.findAllByOrderNo(order.getOrderNo());
        if (null != orderGoodsList && orderGoodsList.size() > 0) {
            for (OrderGoods orderGoods : orderGoodsList) {
                YjGoodsSku yjGoodsSku = yjGoodsSkuService.findById(orderGoods.getGoodsSkuId());
                orderGoods.setYjGoodsSku(yjGoodsSku);
                orderGoods.setContent(null);
                //查询skuValue值
                if (null != yjGoodsSku) {
                    /*String specSkuIDS = yjGoodsSku.getSpecSkuId();
                    if (StringUtils.isNotBlank(specSkuIDS)) {
                        List<String> contents = Arrays.asList(specSkuIDS.split(","));
                        if (null != contents && contents.size() > 0) {
                            for (String s : contents) {
                                if (StringUtils.isNotBlank(orderGoods.getSpecValue())) {
                                    orderGoods.setSpecValue(orderGoods.getSpecValue() + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                } else {
                                    orderGoods.setSpecValue(yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue());
                                }
                            }
                        }
                    }*/
                    String temp = "";
                    if (null != yjGoodsSku) {
                        /*if (StringUtils.isNotBlank(yjGoodsSku.getSpecSkuId())) {
                            temp = yjGoodsSku.getSpecSkuId().replaceAll("，", ",");
                        }*/
                    }
                    if (StringUtils.isNotBlank(temp)) {
                        String vl = "";
                        List<String> result = Arrays.asList(temp.split(","));
                        if (null != result && result.size() > 0) {
                            for (String s : result) {
                               /* if (StringUtils.isNotBlank(vl)) {
                                    vl = vl + "," + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                                } else {
                                    vl = vl + yjSpecValueService.findById(Integer.parseInt(s)).getSpecValue();
                                }*/
                            }
                        }
                        yjGoodsSku.setSpecValue(vl);
                    }
                    orderGoods.setYjGoodsSku(yjGoodsSku);
                }
            }

        }
        order.setOrderGoods(orderGoodsList);
        //发货信息 ww 1.20
        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", order.getOrderNo());
        List<OrderAddress> orderAddresses = orderAddressService.findAll(param);
        order.setOrderAddress(orderAddresses.get(0));

        R r = new R();
        r.put("data", order);
        //进入订单详情页时，判断是否未付款
        long expireTime = (order.getCreateTime().getTime() + (Integer.parseInt(orderExpireTime) * 60 * 1000));
        if (expireTime - new Date().getTime() <= 0 && order.getOrderStatus().equals(Order.OrderStatus.PAYMENT)) {

            order.setOrderStatus(Order.xlgOrderStatus.SHIPPED);
            orderService.save(order);
            r.put("surplusTime", expireTime - new Date().getTime());
        }
        return r;
    }

    /**
     * 生成前端订单号
     *
     * @return
     */
    private String generateOrderNo() {
        Date now = new Date();
        return DateUtils.format(now, "yyyyMMdd") + now.getTime();
    }

    /**
     * 立即购买
     *
     * @param orderModel
     * @return 优化下订单性能 2020/02/10
     */
    @PostMapping("/generate/order")
    public R generateOrder(@RequestBody OrderModel orderModel) throws Exception {
        Date date = new Date();
        //return transactionTemplate.execute(transactionStatus -> {
        R r = new R();
        //tsh 2019.12.23 校验地址信息不为空
        if (null == orderModel.getAddressId()) {
            return R.error("地址信息不能为空");
        }
        String id = generateOrderNo();
        YjGoods goods = goodsService.findById(orderModel.getGoodsId());
        YjGoodsSku goodsSku = goodsSkuService.findById(orderModel.getGoodsSkuId());
        /*if (orderModel.getNum() > goodsSku.getStockNum()) {
            return R.error("库存不足");
        }*/
        //0 组合 1 精选 2 易货
        if (orderModel.getActivityType() == 0) {
            //获取推荐首页的活动
            //Activity activity = activityService.findById(goods.getActivityId());
            Activity activity = sysAdminDao.getNewActivity();
            //判断此次活动是否达到最大限额
            Double nowActTotal = sysAdminDao.getActivityTotal(activity.getId());
            if (null == nowActTotal) {
                nowActTotal = 0.0;
            }
            //is comple
            if (null == activity) {
                return R.error("暂无活动");
            } else {
                if (activity.getIsComple() == 1) {
                    return R.error("活动已结束");
                }
            }
            if (BigDecimal.valueOf(nowActTotal).compareTo(activity.getMaxAccount()) == 1) {
                //修改状态
                sysAdminDao.updateActivityComple(activity.getId());
                return R.error("活动已结束");
            }
            //判断活动时间
            //sysAdminDao.updateActivityComple(activity.getId());

            //新增两条订单，普通订单和批发订单
            //22 1.18  直接将当前商品传过去，免得又查找一次
            //22 1.18  将供应商id 传过去
            //r.put("data", orderService.retail(id, goods.getAgentId(), goodsSku, orderModel, orderModel.getActivityType()));
        } else {
            //易货+精选 //0 组合 1 精选 2 易货
           // r.put("data", orderService.retail(id, goods.getAgentId(), goodsSku, orderModel, orderModel.getActivityType()));
        }

        orderService.saveOrderAddress(orderModel.getAddressId(), id);
        //设置订单商品基本信息
        /*OrderGoods orderGoods = new OrderGoods();
        orderGoods.setGoodsId(goods.getGoodsId());
        orderGoods.setGoodsName(goods.getGoodsName());
        orderGoods.setGoodsSkuId(goodsSku.getGoodsSkuId());
        orderGoods.setImageId(goodsSku.getImageId());
        String[] imgs = goods.getImgUrls().split(",");
        if (imgs.length > 0) {
            orderGoods.setImageUrl(goods.getImgUrls().split(",")[0]);
        }
        orderGoods.setContent(goods.getContent());
        orderGoods.setGoodsNo(goodsSku.getGoodsNo());
        orderGoods.setGoodsPrice(goodsSku.getGoodsPrice());
        orderGoods.setLinePrice(goodsSku.getLinePrice());
        orderGoods.setTotalNum(orderModel.getNum());
        if (orderModel.getActivityType() == 0) {
            orderGoods.setOrdinaryNum(1);
            orderGoods.setConsignNum(2);
        }
        if (orderModel.getActivityType() == 0) {
            //组合
            BigDecimal total = goodsSku.getWsPrice().multiply(new BigDecimal(2));//批发数量2+普通数量1 固定不变
            orderGoods.setTotalPrice(total.add(goodsSku.getGoodsPrice()));
            orderGoods.setTotalPayPrice(total.add(goodsSku.getGoodsPrice()));
        } else if (orderModel.getActivityType() == 2) {
            //易货
            BigDecimal total = goodsSku.getBarterPrice().multiply(BigDecimal.valueOf((long) orderModel.getNum()));
            orderGoods.setEasyMoney(total);
        } else {
            BigDecimal total = goodsSku.getGoodsPrice().multiply(BigDecimal.valueOf((long) orderModel.getNum()));
            orderGoods.setTotalPrice(total);
            orderGoods.setTotalPayPrice(total);
        }
        orderGoods.setOrderNo(id);
        orderGoods.setUserId(ShiroUtils.getUserId());
        //orderGoodsService.save(orderGoods);
        orderService.saveOrderGoods(orderGoods);
        if (orderModel.getActivityType() == 2) {
            //易货币兑换
            r = orderService.pay(40, orderGoods.getEasyMoney(), id, "", orderModel.getActivityType());
        }*/
        //减少库存  ---
        reduceStock(id); //id --订单
        return r;
        //});
    }

    /**
     * 生成 普通订单 / 零售订单
     * OrderModel --自己定义的类，--
     */

    private Order retail(String id, String AgentId, YjGoodsSku goodsSku, OrderModel orderModel, int type) {
        //0 组合 1 精选 2 易货  --type
        Order order = new Order();
        //设置基础信息
        order.setOrderNo(id);
        order.setCallbackNo(id);
        order.setDealersId(AgentId);//保存供应商
        order.setUserId(ShiroUtils.getUserId());
        order.setUsername(ShiroUtils.getUserEntity().getUsername());
        order.setOrderType(type);

        BigDecimal total;
        //根据订单类型算订单价格
        /*if (type == 0) {
            total = goodsSku.getWsPrice().multiply(new BigDecimal(2));//批发数量2+普通数量1 固定不变
            total = total.add(goodsSku.getRetailPrice());
        } else if (type == 2) {
            total = goodsSku.getBarterPrice().multiply(BigDecimal.valueOf((long) orderModel.getNum()));

        } else {
            //设置总价 精品goods price 批发 ws 零售 retail
            total = goodsSku.getGoodsPrice().multiply(BigDecimal.valueOf((long) orderModel.getNum()));
        }*/
        //order.setTotalPrice(total);
        //order.setEasyMoney(total);//设置易货币
        //卖家留言
        order.setBuyerRemark(orderModel.getBuyerRemark());
        //付款状态未付款
        order.setPayStatus(Order.xlgOrderStatus.PAYMENT);
        order.setOrderStatus(Order.xlgOrderStatus.PAYMENT);
        //运费
        //YjGoods yjGoods= goodsService.findById(goodsSku.getGoodsId());
        order.setExpressPrice(new BigDecimal(0));
        //实付金额
        //order.setPayPrice(total);
        Activity activity = sysAdminDao.getNewActivity();
        order.setActivityId(activity.getId());
        orderService.save(order);
        return order;
    }

    /**
     * 减库存
     */
    private void reduceStock(String orderNo) {
        //减库存
        List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(orderNo);
        if (null != orderGoods && orderGoods.size() > 0) {
            for (OrderGoods orderGood : orderGoods) {
                YjGoodsSku goodsSku = goodsSkuService.findById(orderGood.getGoodsSkuId());
                //goodsSku.setStockNum(goodsSku.getStockNum() - orderGood.getTotalNum());
                goodsSkuService.save(goodsSku);
            }
        }
    }

    /**
     * 增加库存  从订单里取数量来---- 1、未付款订单超时 2、订单取消时，要补上库存 。 3、支付失败--要补上  ，4、退货 ---- 后台管理系统 确定退货
     */
    private void increaseStock(String orderNo) {
        //减库存
        List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(orderNo);
        if (null != orderGoods && orderGoods.size() > 0) {
            for (OrderGoods orderGood : orderGoods) {
                YjGoodsSku goodsSku = goodsSkuService.findById(orderGood.getGoodsSkuId());
                //goodsSku.setStockNum(goodsSku.getStockNum() + orderGood.getTotalNum());
                goodsSkuService.save(goodsSku);
            }
        }
    }

    /**
     * 获取商品需要的批发券数量  --根据字典表中的 券价比来获取批发券
     * 先看商品里是否有赠送比例 有，就用它，没有，根据比例计算
     */
    private BigDecimal getCouponNum(BigDecimal needCoupon, Integer totalNum, BigDecimal payPrice) {
        BigDecimal couponNum = new BigDecimal(0);
        if (needCoupon == null || needCoupon.intValue() == 0) {
            List<YjDictEntity> dictEntities = dictService.getDictListByType(10);
            if (dictEntities.size() > 0) {
                couponNum = payPrice.multiply(BigDecimal.valueOf(Double.parseDouble(dictEntities.get(0).getDvalue()))).setScale(2, BigDecimal.ROUND_DOWN);
            }
        }
        if (couponNum.intValue() == 0) {
            couponNum = BigDecimal.valueOf(totalNum).multiply(needCoupon == null ? new BigDecimal(0) : needCoupon);
        }
        return couponNum;
    }

    /**
     * 付款
     */
    @PostMapping("/payment")
    @Transactional
    public R payment(@RequestBody PaymentModel paymentModel) throws Exception {

        //10余额支付 20微信支付 30支付宝支付 40易货币
        Order order = orderService.findById(paymentModel.getOrderNo());
        if (null != order) {
            Activity activity = sysAdminDao.getNewActivity();
            //判断此次活动是否达到最大限额
            Double nowActTotal = sysAdminDao.getActivityTotal(activity.getId());
            if (null == nowActTotal) {
                nowActTotal = 0.0;
            }
            //is comple
            if (null == activity) {
                return R.error("暂无活动");
            } else {
                if (activity.getIsComple() == 1) {
                    return R.error("活动已结束");
                }
            }
            if (BigDecimal.valueOf(nowActTotal).compareTo(activity.getMaxAccount()) == 1) {
                //修改状态
                sysAdminDao.updateActivityComple(activity.getId());
                return R.error("活动已结束");
            }
            //判断活动时间
            //sysAdminDao.updateActivityComple(activity.getId());
        }

        long expireTime = (order.getCreateTime().getTime() + (Integer.parseInt(orderExpireTime) * 60 * 1000));
        if (expireTime - new Date().getTime() <= 0) {
            order.setOrderStatus(Order.xlgOrderStatus.SHIPPED);
            orderService.save(order);
            return R.error("订单已过期");
        }
        if (order.getPayStatus().equals(20)) {
            return R.error("订单已支付");
        }
        order.setPayType(paymentModel.getPayType());
        orderService.save(order);
        order.setPayTime(new Date());
        //判断订单类型走不同类型支付 0-组合的订单1-精品区订单2-易货区订单

        //根据选择不同的付款方式调接口
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", paymentModel.getOrderNo());
        params.put("payType", paymentModel.getPayType());
        params.put("password", paymentModel.getPassword());
        R r = orderService.pay(params, order);
        return r;
    }

    /**
     * 购物车付款(精品商品)
     */
    @PostMapping("/cartpayment")
    @Transactional
    public R cartpayment(@RequestBody PaymentModel paymentModel) throws Exception {
        //10余额支付 20微信支付 30支付宝支付 40易货币
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("callbackNo", paymentModel.getCallbackNo());
        List<Order> orderList = orderService.findAll(parMap);
        //Order order = orderService.findById(paymentModel.getOrderNo());
        long expireTime = (orderList.get(0).getCreateTime().getTime() + (Integer.parseInt(orderExpireTime) * 60 * 1000));
        if (expireTime - new Date().getTime() <= 0) {
            return R.error("订单已过期");
        }
        /*if (expireTime - new Date().getTime() <= 0) {
            order.setOrderStatus(Order.xlgOrderStatus.SHIPPED);
            orderService.save(order);
            orderWholesale.setOrderStatus(Order.xlgOrderStatus.SHIPPED);
            orderWholesaleService.save(orderWholesale);
            return R.error("订单已过期");
        }*/
        if (!orderList.get(0).getOrderStatus().equals(Order.OrderStatus.PAYMENT)) {
            return R.error("订单已支付");
        }
        for (Order order : orderList) {
            order.setPayType(paymentModel.getPayType());
            orderService.save(order);
            order.setPayTime(new Date());
        }
        //判断订单类型走不同类型支付 0-组合的订单1-精品区订单2-易货区订单
        //根据选择不同的付款方式调接口
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", paymentModel.getCallbackNo());
        params.put("payType", paymentModel.getPayType());
        params.put("password", paymentModel.getPassword());
        R r = orderService.cartpay(params, orderList);
        return r;

    }

    /**
     * 支付宝回调
     *
     * @param request
     * @return
     */
    @RequestMapping("/alipay_callback")

    public String callback(HttpServletRequest request) {
        Map<String, String> params = StrUtil.convertRequestParamsToMap(request); // 将异步通知中收到的待验证所有参数都存放到map中
        String paramsJson = JSON.toJSONString(params);
        String tradeStatus = request.getParameter("trade_status");            //交易状态
        System.err.println("支付宝回调，{}" + paramsJson);
        // return transactionTemplate.execute(status -> {
        try {
        // 调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayConfigs.alipay_public_key,
                alipayConfigs.charset, alipayConfigs.signtype);
        if (signVerified) {
            if (tradeStatus.equals("TRADE_SUCCESS")) {
                // 按照支付结果异步通知中的描述，对支付结果中的业务内容进行1\2\3\4二次校验，校验成功后在response中返回success，校验失败返回failure
                orderService.check(params);
                // 如果签名验证正确，立即返回success，后续业务另起线程单独处理
                //修改订单状态
                //修改状态，存数据库
                String outTradeNo = params.get("out_trade_no");//本地订单 支付宝回调地址
                Map<String, Object> parmap = new HashMap<>();
                parmap.put("callbackNo", outTradeNo);
                List<Order> orderList = orderService.findAll(parmap);
                if (null != orderList && orderList.size() > 0) {
                    for (Order order : orderList) {
                        String trade_no = params.get("trade_no");//支付宝生成订单


                        order.setPayStatus(20);
                        if (order.getOrderType() == 1) {
                            //精品
                            order.setOrderStatus(Order.xlgOrderStatus.CANCEL);
                        } else {
                            order.setOrderStatus(Order.xlgOrderStatus.RECEIVED);
                        }
                        order.setUpdateTime(new Date());
                        orderService.save(order);
                        //增加商品销量
                        List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo());
                        for (OrderGoods orderGood : orderGoods) {
                            YjGoods yjGoods = goodsService.findById(orderGood.getGoodsId());
                            //yjGoods.setSalesActual(yjGoods.getSalesActual() + orderGood.getTotalNum());
                            goodsService.save(yjGoods);
                        }
                        //付款成功通知
                        OrderGoods orderGoods1 = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                        //sysAdminService.sendMsg(16, orderGoods1.getGoodsName(), order.getUserId());
                        //支付订单汇总
                        /*SysUserEntity sue = sysUserService.getUserById(order.getUserId());
                        PaymentSummary paymentSummary = new PaymentSummary();
                        paymentSummary.setUserId(order.getUserId());
                        paymentSummary.setRelationId(0L);
                        paymentSummary.setPaymentType(1);//0收入 1支出
                        paymentSummary.setOrderNo(outTradeNo);
                        paymentSummary.setOperaType(2);//1寄售2订单支付3推荐奖4分红5红包6转账7提现8退款处理9升级代理商
                        paymentSummary.setMoney(order.getPayPrice());
                        paymentSummary.setBeforeMoney(sue.getBalance());
                        paymentSummary.setAfterMoney(sue.getBalance());
                        paymentSummary.setPayType(3);//1余额2微信3支付宝4易货币
                        paymentSummary.setRemark("订单号:" + paymentSummary.getOrderNo() + ",支付宝支付成功！消费金额:" + paymentSummary.getMoney() + "元");
                        sysAdminDao.savePaymentSummary(paymentSummary);*/
                    }
                }
                //Order order = orderService.findById(outTradeNo);
                return "success";
            } else {
                System.err.println("支付宝回调签名认证失败，signVerified=false, paramsJson:{}" + paramsJson);
                return "failure";
            }
        } else {
            //
            System.err.println("支付宝回调签名认证失败，signVerified=false, paramsJson:{}" + paramsJson);
            return "failure";
        }
    } catch (AlipayApiException e) {
        //, paramsJson, e.getMessage()
        System.err.println("支付宝回调签名认证失败,paramsJson:{},errorMsg:{}" + e.getMessage());
        return "failure";
    }
}



    /**
     * 微信回调
     *
     * @param request
     * @return
     */
    @RequestMapping("/wxpay_callback")
    //@Transactional
    public String wxcallback(HttpServletRequest request) throws IOException, JDOMException {
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params = PayCommonUtil.doXMLParse(resultxml);
        outSteam.close();
        inStream.close();
        Map<String, String> return_data = new HashMap<String, String>();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
            System.err.println("支付失败！");
            return_data.put("return_code", "FAIL");
            return_data.put("return_msg", "return_code不正确");
            return wxFallReturnStr();
        } else {
            // 处理业务开始
            //修改状态，存数据库
            String outTradeNo = params.get("out_trade_no");//本地订单 支付宝回调地址
            Map<String, Object> parmap = new HashMap<>();
            parmap.put("callbackNo", outTradeNo);
            List<Order> orderList = orderService.findAll(parmap);
            if (null != orderList && orderList.size() > 0) {
                for (Order order : orderList) {
                    String trade_no = params.get("trade_no");//支付宝生成订单


                    order.setPayStatus(20);
                    if (order.getOrderType() == 1) {
                        //精品
                        order.setOrderStatus(Order.xlgOrderStatus.CANCEL);
                    } else {
                        order.setOrderStatus(Order.xlgOrderStatus.RECEIVED);
                    }
                    order.setUpdateTime(new Date());
                    orderService.save(order);
                    //增加商品销量
                    List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo());
                    for (OrderGoods orderGood : orderGoods) {
                        YjGoods yjGoods = goodsService.findById(orderGood.getGoodsId());
                        //yjGoods.setSalesActual(yjGoods.getSalesActual() + orderGood.getTotalNum());
                        goodsService.save(yjGoods);
                    }
                    //付款成功通知
                    OrderGoods orderGoods1 = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                    //sysAdminService.sendMsg(16, orderGoods1.getGoodsName(), order.getUserId());
                    //支付订单汇总
                    /*SysUserEntity sue = sysUserService.getUserById(order.getUserId());
                    PaymentSummary paymentSummary = new PaymentSummary();
                    paymentSummary.setUserId(order.getUserId());
                    paymentSummary.setRelationId(0L);
                    paymentSummary.setPaymentType(1);//0收入 1支出
                    paymentSummary.setOrderNo(outTradeNo);
                    paymentSummary.setOperaType(2);//1寄售2订单支付3推荐奖4分红5红包6转账7提现8退款处理9升级代理商
                    paymentSummary.setMoney(order.getPayPrice());
                    paymentSummary.setBeforeMoney(sue.getBalance());
                    paymentSummary.setAfterMoney(sue.getBalance());
                    paymentSummary.setPayType(2);//1余额2微信3支付宝4易货币
                    paymentSummary.setRemark("订单号:" + paymentSummary.getOrderNo() + ",支付宝支付成功！消费金额:" + paymentSummary.getMoney() + "元");
                    sysAdminDao.savePaymentSummary(paymentSummary);*/
                }
            }
            return  wxReturnStr();
        }
    }


    private String wxReturnStr(){
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("return_code", "SUCCESS");
        resultMap.put("return_msg", "OK");
        String resultV = MapUtil.map2Xml(resultMap);
        return resultV;
    }
    private String wxFallReturnStr(){
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("return_code", "FAIL");
        resultMap.put("return_msg", "OK");
        String resultV = MapUtil.map2Xml(resultMap);
        return resultV;
    }
    /**
     * 得到订单支付信息
     **/
    @PostMapping("/getOrderPayInfo")
    public R getOrderPayInfo(@RequestBody String recordNo) {
        Order order = orderService.findById(recordNo);
        if (order == null) {
            return R.error("支付订单号不存在");
        }
        if (order.getDeliveryStatus() == 10) {  //待发货
            return R.ok(StatusMsgEnum.ORDER_PAY_SUCESS);
        }
        if (order.getPayStatus() == 30) {  //待发货
            return R.ok(StatusMsgEnum.ORDER_PAY_FAIL);
        } else {
            return R.ok(StatusMsgEnum.ORDER_PAY_WAITING);//等待支付
        }
    }

    /**
     * 发货
     */
    @PostMapping("/deliver")
    @Transactional
    public R deliver(@RequestBody DeliverModel deliverModel) {
        return transactionTemplate.execute(transactionStatus -> {
            Order order = orderService.findById(deliverModel.getOrderNo());
            order.setDeliveryType(10);//配送方式
            //设置物流公司
            Express express = expressService.findById(deliverModel.getExpressId());
            order.setExpressId(deliverModel.getExpressId());
            order.setExpressCompany(express.getExpressName());
            order.setExpressNo(deliverModel.getExpressNo());
            //设置发货状态
            order.setDeliveryStatus(20);
            order.setDeliveryTime(new Date());
            order.setOrderStatus(Order.xlgOrderStatus.PAYMENT_FAILED);//待收货
            order.setUpdateTime(new Date());
            orderService.save(order);
            //发送系统消息
            OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
            //sysAdminService.sendMsg(1, orderGoods.getGoodsName(), order.getUserId());
            return R.ok();
        });
    }

    /**
     * app用户确认收货
     */
    @PostMapping("/receiving/{id}")
    public R receiving(@PathVariable("id") String orderId) {
        Order order = orderService.findById(orderId);

        if (!ShiroUtils.getUserId().equals(order.getUserId())) {
            return R.error("非法操作");
        }
        //订单未收货
        if (!order.getOrderStatus().equals(Order.xlgOrderStatus.PAYMENT_FAILED) || !order.getReceiptStatus().equals(10)) {
            return R.error("当前状态不可收货");
        }
        order.setReceiptStatus(20);
        order.setReceiptTime(new Date());
        order.setOrderStatus(Order.xlgOrderStatus.EXPIRE);//确认收货
        order.setUpdateTime(new Date());
        orderService.save(order);
        //ww 12.12支付记录表，将此订单支付表此订单标记为完成

        //yjMoneyService.saveUserProfit(ShiroUtils.getUserId(), orderId);
        //发送系统消息
        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(orderId).get(0);
        //sysAdminService.sendMsg(3, orderGoods.getGoodsName(), order.getUserId());
        return R.ok();
    }

    /**
     * 修改地址
     */
    @PostMapping("/update/address")
    public R updateAddress(@RequestBody OrderModel orderModel) {
        if (orderModel.getOrderNo() == null) {
            return R.error("缺少订单号");
        }

        Order order = orderService.findById(orderModel.getOrderNo());
        if (order == null) {
            return R.error("无效的订单");
        }

        if (!order.getOrderStatus().equals(Order.OrderStatus.PAYMENT)) {
            return R.error("当前状态不可修改地址");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("orderNo", orderModel.getOrderNo());
        List<OrderAddress> orderAddresses = orderAddressService.findAll(param);

        if (orderAddresses.size() != 1) {
            return R.error("无效的订单");
        }

        OrderAddress orderAddress = orderAddresses.get(0);

        //设置地址信息表
        Address address = addressService.findById(orderModel.getAddressId());
        orderAddress.setName(address.getName());
        orderAddress.setPhone(address.getPhone());
        orderAddress.setProvinceName(address.getProvinceName());
        orderAddress.setCityName(address.getCityName());
        orderAddress.setRegionName(address.getRegionName());
        orderAddress.setDetail(address.getDetail());
        orderAddress.setUserId(ShiroUtils.getUserId());
        orderAddressService.save(orderAddress);

        return R.ok();
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{id}")
    @Transactional
    public R cancel(@PathVariable("id") String orderId) {
        //tsh 2019.12.24 判断订单类型，批发/零售
        Order order = orderService.findById(orderId);
        if (null != order) {
            if (!order.getOrderStatus().equals(Order.xlgOrderStatus.PAYMENT)) {
                return R.error("订单当前状态不可取消");
            }
            //设置订单状态为取消
            order.setOrderStatus(Order.xlgOrderStatus.SHIPPED);//取消订单
            orderService.save(order);
            sysAdminDao.updateIncreaOrderList(order);
            //发送系统消息
            OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
            //sysAdminService.sendMsg(5, orderGoods.getGoodsName(), order.getUserId());

        }
        //增加库存
        increaseStock(orderId);
        return R.ok();
    }

    /**
     * 退货
     *
     * @param param orderNo => 订单No
     *              reason => 退货原因
     */
    @PostMapping("/returned")
    public R returned(@RequestBody Map<String, Object> param) {
        return transactionTemplate.execute(status -> {
            String orderId = String.valueOf(param.get("orderNo"));


            Order order = orderService.findById(orderId);
            if (order == null) {
                return R.error("无效的订单");
            }

            if (!Order.xlgOrderStatus.EXPIRE.equals(order.getOrderStatus())) {
                return R.error("订单当前状态不可退货");
            }

            //标记订单状态为退货状态
            order.setOrderStatus(Order.xlgOrderStatus.RETURN_GOODS);
            orderService.save(order);

            //退货表中插入数据

            //发送系统消息
            OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(orderId).get(0);
            //sysAdminService.sendMsg(11, orderGoods.getGoodsName(), order.getUserId());

            return R.ok("已成功申请退货，等待商家处理");
        });
    }

    /**
     * 申请退款
     *
     * @param param orderNo => 订单ID
     *              reason  => 原因
     */
    @PostMapping("/refund")
    public R refund(@RequestBody Map<String, Object> param) {
        String orderId = String.valueOf(param.get("orderNo"));
        Order order = orderService.findById(orderId);
        if (order == null) {
            return R.error("无效的订单");
        }

        if (!Order.xlgOrderStatus.CANCEL.equals(order.getOrderStatus())) {
            return R.error("该订单已发货，无法退款");
        }


        //修改状态
        order.setOrderStatus(Order.xlgOrderStatus.RETURN_GOODS);
        orderService.save(order);
        //tsh 2019/12/23  退款加库存
        increaseStock(orderId);

        //发送系统消息
        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
       // sysAdminService.sendMsg(8, orderGoods.getGoodsName(), order.getUserId());

        return R.ok("退款申请已提交，等待商家处理");
    }

    /**
     * 是否同意退款
     *
     * @param param orderNo => 订单ID
     *              whether =>  0： 拒绝退货申请， 1：同意退货申请
     *              reason => 拒绝退款描述，如果拒绝退货申请则需要此字段；
     */
    @PostMapping("/whether/refund")
    public R whetherRefund(@RequestBody Map<String, Object> param) {
        String orderId = String.valueOf(param.get("orderNo"));

        Order order = orderService.findById(orderId);
        if (order == null) {
            return R.error("无效的订单");
        }

        if (!Order.xlgOrderStatus.RETURN_GOODS.equals(order.getOrderStatus())) {
            return R.error("该订单状态无法进行此操作");
        }



        return transactionTemplate.execute(status -> {
            if ("1".equals(String.valueOf(param.get("whether")))) {
                //退款
                RefundModel refundModel = new RefundModel();
                refundModel.setTradeNo(orderId);
                refundModel.setRefundAmount(order.getPayPrice());
                refundModel.setPayType(order.getPayType());
                refundModel.setOrderNo(orderId);
                //R r = orderService.refundOrder(refundModel);
                R r =new R();


                if (500 == (int) r.get("code")) {
                    return r;
                }

                //退货、退款表修改状态


                //修改状态
                order.setOrderStatus(Order.xlgOrderStatus.RETURN_GOODS);
                orderService.save(order);

                return R.ok();
            } else if ("0".equals(String.valueOf(param.get("whether")))) {
                //退货、退款表修改状态

                //修改状态
                order.setOrderStatus(Order.xlgOrderStatus.REJECTION_RETURNS);
                orderService.save(order);

                return R.ok("操作成功");
            } else {
                return R.error("未知操作");
            }
        });
    }

    /**
     * 购物车订单---- 购物车里要付款的商品
     */
    @PostMapping("/cart/order")
    @Transactional
    public R cartOrder(@RequestBody OrderModel orderModel) {
        //校验购物车不能为空
        if (orderModel.getCartIds() == null || orderModel.getCartIds().size() == 0) {
            return R.error("请选择需要购买的商品");
        }
        //tsh 2020.1.18 订单按供应商进行分类
        String callbackNo = generateOrderNo();//生成支付回调订单号

        //逻辑上处理是 用户选择的购物车商品，每个商品都相当于一个购物车
        List<Cart> cartsa = cartService.findAllByCartIdIn(orderModel.getCartIds());
        //将购物车根据供应商划分不同批次购物车
        Map<String, List<Cart>> cartMap = new HashMap<>();

        if (null != cartsa && cartsa.size() > 0) {
            for (Cart cart : cartsa) {
                Integer delearId = sysAdminDao.getDealerIdByGoodsId(cart.getGoodsId());//获取供应商id
                if (cartMap.containsKey(delearId.toString())) {
                    List<Cart> cList = cartMap.get(delearId.toString());
                    cList.add(cart);
                    cartMap.put(delearId.toString(), cList);
                } else {
                    List<Cart> tempList = new ArrayList<>();
                    tempList.add(cart);
                    cartMap.put(delearId.toString(), tempList);
                }
            }
        } else {
            return R.error("请选择需要购买的商品");
        }

        //供应商分组后，处理---
        cartMap.forEach((k, carts) -> {
            //走普通订单流程
            String id = generateOrderNo();//生成订单号
            /*List<YjGoodsSku> goodsSkus = goodsSkuService.findAllByGoodsSkuIdIn(carts.stream().map(Cart::getGoodsSkuId).collect(Collectors.toList()));
            //判断库存
            BigDecimal total = new BigDecimal(0),
                    freight = new BigDecimal(0);
            for (YjGoodsSku skus : goodsSkus) {
                for (Cart cart : carts) {
                    *//*if (skus.getGoodsSkuId().equals(cart.getGoodsSkuId())) { //多规则 13
                        YjGoods goods = goodsService.findById(skus.getGoodsId());

                        if (cart.getNum() > skus.getStockNum()) {
                            throw new RRException(goods.getGoodsName() + "库存不足");
                        }
                        //设置运费和总价
                        freight = freight.add(skus.getFreight() == null ? new BigDecimal(0) : skus.getFreight());
                        total = total.add(skus.getGoodsPrice().multiply(BigDecimal.valueOf((long) cart.getNum())));
                        //设置订单商品基本信息
                        OrderGoods orderGoods = new OrderGoods();
                        orderGoods.setGoodsId(goods.getGoodsId());
                        orderGoods.setGoodsName(goods.getGoodsName());
                        orderGoods.setGoodsSkuId(skus.getGoodsSkuId());

                        orderGoods.setImageId(skus.getImageId());
                        String[] imgs = goods.getImgUrls().split(",");
                        if (imgs.length > 0) {
                            orderGoods.setImageUrl(goods.getImgUrls().split(",")[0]);
                        }
                        orderGoods.setContent(goods.getContent());
                        orderGoods.setGoodsNo(skus.getGoodsNo());
                        orderGoods.setGoodsPrice(skus.getGoodsPrice());
                        orderGoods.setLinePrice(skus.getLinePrice());
                        //        orderGoods.setCouponMoney();
                        orderGoods.setTotalNum(cart.getNum());
                        BigDecimal goodsTotal = skus.getGoodsPrice().multiply(BigDecimal.valueOf((long) cart.getNum()));
                        orderGoods.setTotalPrice(goodsTotal);
                        orderGoods.setTotalPayPrice(goodsTotal);
                        orderGoods.setOrderNo(id);
                        orderGoods.setUserId(ShiroUtils.getUserId());
                        orderGoodsService.save(orderGoods);

                        break;
                    }*//*
                }
            }*/
            //减少库存
            reduceStock(id);
            //设置基础信息
            Order order = new Order();
            order.setOrderNo(id);
            order.setCallbackNo(callbackNo);
            order.setDealersId(k);//保存供应商id
            order.setUserId(ShiroUtils.getUserId());
            order.setUsername(ShiroUtils.getUserEntity().getUsername());
            order.setOrderType(1);
            //设置总价
            //order.setTotalPrice(total);
            //实付金额
            //order.setPayPrice(total);
            //payMoneys=payMoneys.add(total);
            //卖家留言
            order.setBuyerRemark(orderModel.getBuyerRemark());
            //付款状态未付款
            order.setPayStatus(Order.xlgOrderStatus.PAYMENT);
            order.setOrderStatus(Order.xlgOrderStatus.PAYMENT);
            //运费
            //order.setExpressPrice(freight);
            Activity activity = sysAdminDao.getNewActivity();
            order.setActivityId(activity.getId());
            orderService.save(order);
            //orderList.add(order);
            //设置地址信息表
            Address address = addressService.findById(orderModel.getAddressId());
            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setName(address.getName());
            orderAddress.setPhone(address.getPhone());
            orderAddress.setProvinceName(address.getProvinceName());
            orderAddress.setCityName(address.getCityName());
            orderAddress.setRegionName(address.getRegionName());
            orderAddress.setDetail(address.getDetail());
            orderAddress.setOrderNo(id);
            orderAddress.setUserId(ShiroUtils.getUserId());
            orderAddressService.save(orderAddress);
        });

        //删除购物车
        for (Integer cartId : orderModel.getCartIds()) {
            cartService.deleteById(cartId);
        }
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("callbackNo", callbackNo);
        List<Order> orderList = orderService.findAll(parMap);
        BigDecimal bigDecimal = new BigDecimal(0);
        if (null != orderList && orderList.size() > 0) {
            for (Order order : orderList) {
                bigDecimal = bigDecimal.add(order.getPayPrice());
            }
        }
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("callbackNo", callbackNo);
        retMap.put("payPrice", bigDecimal);
        return R.ok().put("data", retMap);//购物车返回回调订单号

    }

    /**
     * 同意/拒绝退货申请
     *
     * @param params orderNo => 订单号
     *               whether => 0： 拒绝退货申请， 1：同意退货申请
     *               reason => 拒绝退货描述，如果拒绝退货申请则需要此字段；
     *               address => 收货地址
     */
    @PostMapping("whether/returned")
    public R whetherReturned(@RequestBody Map<String, Object> params) {
        String orderNo = String.valueOf(params.get("orderNo"));
        Order order = orderService.findById(orderNo);
        //发送系统消息
        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(orderNo).get(0);
        //sysAdminService.sendMsg(12, orderGoods.getGoodsName(), order.getUserId());


        if (order == null) {
            return R.error("无效的订单");
        }

        /*if (!Order.OrderStatus.RETURN_GOODS.equals(order.getOrderStatus())) {
            return R.error("订单当前状态不可进行该操作");
        }*/

        String whether = String.valueOf(params.get("whether"));
        if ("0".equals(whether)) {
            //拒绝退货修改状态
            order.setOrderStatus(Order.xlgOrderStatus.EXPIRE);
            orderService.save(order);

            //退货表中修改状态，写入数据


            return R.ok("操作成功");
        } else if ("1".equals(whether)) {
            //同意退货
            order.setOrderStatus(Order.xlgOrderStatus.REJECTION_RETURNS);
            orderService.save(order);

            //退货表中修改状态，写入数据

            //填写地址等信息


            return R.ok("操作成功");
        } else {
            return R.error("未知操作");
        }
    }

    /**
     * app用户操作退货
     *
     * @param params orderNo => 订单号
     *               expressId => 物流ID
     *               expressNo => 物流单号
     */
    @PostMapping("/returns/goods")
    public R returnsGoods(@RequestBody Map<String, Object> params) {
        String orderNo = String.valueOf(params.get("orderNo"));
        Order order = orderService.findById(orderNo);
        String allowDays = sysAdminDao.getDictValue(13);

        if (order == null) {
            return R.error("无效的订单");
        }
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(allowDays));  //设置为7天前
        Date before7days = calendar.getTime();   //得到7天前的时间
        Calendar calendar1 = Calendar.getInstance();  //得到日历
        calendar1.setTime(order.getReceiptTime());//把当前时间赋给日历
        Date after7days = calendar1.getTime();
        if (after7days.getTime() < before7days.getTime()) {
            return R.error("订单超过" + allowDays + "天，无法进行售后服务");
        }
        /*if (!Order.OrderStatus.ACCEPTANCE_RETURNS.equals(order.getOrderStatus())) {
            return R.error("订单当前状态不可进行该操作");
        }*/

        //退货表中修改状态，写入数据

        return R.ok();
    }


    /**
     * 后台确认收货
     *
     * @param params orderNo => 订单号
     */
    @PostMapping("/confirm/receiving")
    public R confirmReceiving(@RequestBody Map<String, Object> params) {
        String orderNo = String.valueOf(params.get("orderNo"));
        Order order = orderService.findById(orderNo);

        if (order == null) {
            return R.error("无效的订单");
        }

       /* if (!Order.OrderStatus.ACCEPTANCE_RETURNS.equals(order.getOrderStatus())) {
            return R.error("订单当前状态不可进行该操作");
        }*/

        return transactionTemplate.execute(status -> {
            //退款
            RefundModel refundModel = new RefundModel();
            refundModel.setRefundAmount(order.getPayPrice());
            refundModel.setPayType(order.getPayType());

            refundModel.setOrderNo(orderNo);
            //R r = orderService.refundOrder(refundModel);
            R r = new R();

            if ((int) r.get("code") == 500) {
                return r;
            }

            //退货表中修改状态，写入数据


            //修改订单状态为退货完成
            order.setOrderStatus(Order.xlgOrderStatus.REJECTION_RETURNS);
            orderService.save(order);
            //发送系统消息
            OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(orderNo).get(0);
           // sysAdminService.sendMsg(13, orderGoods.getGoodsName(), order.getUserId());

            return R.ok();
        });
    }


    /**
     * 组合区提货
     */
    @RequestMapping("/applyForDelivery/{orderNo}")
    @Transactional
    public R applyForDelivery(@PathVariable("orderNo") String orderNo) {
        //申请提货30-->待发货60
        sysAdminDao.updateOrderStatus(Order.xlgOrderStatus.CANCEL, orderNo);
        OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(orderNo).get(0);
       // sysAdminService.sendMsg(9, orderGoods.getGoodsName(), ShiroUtils.getUserId());
        return R.ok("保存成功");

    }

    @RequestMapping("/applyForDeliverys")
    @Transactional
    public R applyForDeliverys(@RequestBody Map<String, List<String>> orderNo) {
        if (null != orderNo && null != orderNo.get("orderNo")) {
            for (String s : orderNo.get("orderNo")) {
                //申请提货30-->待发货60
                sysAdminDao.updateOrderStatus(Order.xlgOrderStatus.CANCEL, s);
                OrderGoods orderGoods = orderGoodsService.findAllByOrderNo(s).get(0);
                //sysAdminService.sendMsg(9, orderGoods.getGoodsName(), ShiroUtils.getUserId());

            }
        }

        return R.ok("保存成功");

    }

    //获取用户订单数量  待付款，待提货，待发货，待收货
    @RequestMapping("/getOrderNum/{orderStatus}")
    public R getOrderNum(@PathVariable("orderStatus") int orderStatus) {

        Map<String, Object> parMap = new HashMap<>();
        parMap.put("userId", ShiroUtils.getUserId());
        if (orderStatus == 0) {
            return R.ok().put("data", sysAdminDao.getOrderNum(parMap));
        } else {
            parMap.put("orderStatus", orderStatus);
            return R.ok().put("data", sysAdminDao.getOrderNums(parMap));
        }
    }

}
