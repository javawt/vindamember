package com.songlanyun.jymall.modules.business.order.controller;

import com.songlanyun.jymall.common.utils.PageUtils;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import com.songlanyun.jymall.modules.business.order.service.OrderGoodsService;
import com.songlanyun.jymall.modules.business.score.entity.CashStatistical;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/12/5
 * Time: 17:27
 */
@RestController
@RequestMapping("/order/goods")
public class OrderGoodsController {
    @Autowired
    private OrderGoodsService orderGoodsService;

    @Resource
    private StatisticalService statisticalService;


    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderGoodsService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        return R.ok().put("data", orderGoodsService.findById(id));
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody OrderGoods orderGoods) {
        orderGoodsService.save(orderGoods);
        return R.ok("保存成功");
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id) {
        orderGoodsService.deleteById(id);
        return R.ok("删除成功");
    }


    /**
     * 导出普通订单
     * @param request
     * @param response
     * @param cashStatistical
     * @throws Exception
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @RequestMapping("/expOrder")
    public void expOrder(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception{
        if(!"null".equals(cashStatistical.getStartDate()) && StringUtils.isNotBlank(cashStatistical.getStartDate())){
            cashStatistical.setStartDates(sdf.parse(cashStatistical.getStartDate()));
        }
        if(!"null".equals(cashStatistical.getEndDate()) && StringUtils.isNotBlank(cashStatistical.getEndDate())){
            cashStatistical.setEndDates(sdf.parse(cashStatistical.getEndDate()));
        }

        statisticalService.orderExport(request,response,cashStatistical);
    }

    @RequestMapping("/expOrderList")
    public R expOrderList(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception{
        if(!"null".equals(cashStatistical.getStartDate()) && StringUtils.isNotBlank(cashStatistical.getStartDate())){
            cashStatistical.setStartDates(sdf.parse(cashStatistical.getStartDate()));
        }
        if(!"null".equals(cashStatistical.getEndDate()) && StringUtils.isNotBlank(cashStatistical.getEndDate())){
            cashStatistical.setEndDates(sdf.parse(cashStatistical.getEndDate()));
        }
        return statisticalService.expOrderList(request,response,cashStatistical);
    }

    /**
     * 导出寄售订单
     * @param request
     * @param response
     * @param cashStatistical
     * @throws Exception
     */
    @RequestMapping("/expWhoOrder")
    public void expWhoOrder(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception{
        if(!"null".equals(cashStatistical.getStartDate()) && StringUtils.isNotBlank(cashStatistical.getStartDate())){
            cashStatistical.setStartDates(sdf.parse(cashStatistical.getStartDate()));
        }
        if(!"null".equals(cashStatistical.getEndDate()) && StringUtils.isNotBlank(cashStatistical.getEndDate())){
            cashStatistical.setEndDates(sdf.parse(cashStatistical.getEndDate()));
        }
        statisticalService.expWhoOrder(request,response,cashStatistical);
    }
    @RequestMapping("/expWhoOrderList")
    public R expWhoOrderList(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception{
        if(!"null".equals(cashStatistical.getStartDate()) && StringUtils.isNotBlank(cashStatistical.getStartDate())){
            cashStatistical.setStartDates(sdf.parse(cashStatistical.getStartDate()));
        }
        if(!"null".equals(cashStatistical.getEndDate()) && StringUtils.isNotBlank(cashStatistical.getEndDate())){
            cashStatistical.setEndDates(sdf.parse(cashStatistical.getEndDate()));
        }
        return statisticalService.expWhoOrderList(request,response,cashStatistical);
    }





}
