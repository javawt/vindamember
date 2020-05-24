package com.songlanyun.jymall.modules.business.sys.controller;


import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 囍来购统计报表
 */
@RestController
@RequestMapping("/statistical")
public class StatisticalController {

    @Resource
    private StatisticalService statisticalService;



    /**
     * 数据合计：商品总数、用户总数、订单总数（精品商品）、订单总数（易货商品）、订单总数（组合商品）
     */
    @RequestMapping("/summationsList")
    public R summationsList() {
        return statisticalService.summationsList();
    }


    /**
     * 销售总额 、今日销售额(只统计精品和组合)、今日支付订单数、今日新增用户数
     */
    @RequestMapping("/realList")
    public R realList() {
        return statisticalService.realList();
    }


    /**
     * 曲线图（近七日交易走势）:精品、组合的销售金额走势图，按天
     */
    @RequestMapping("/graphList")
    public R graphList() {
        return statisticalService.graphList();
    }






    //订单明细
    @PostMapping("/orderDetails")
    public R orderDetails(@RequestBody Statistical statistical) {
        return statisticalService.orderDetails(statistical);
    }



    //订单明细导出
    @RequestMapping("/orderDetailsExport")
    public void orderDetailsExport(Statistical statistical, HttpServletRequest request, HttpServletResponse response)  throws Exception{
        statisticalService.orderDetailsExport(statistical,request,response);
    }


    @RequestMapping("/UserResultExport")
    public void UserResultExport(Integer id ,HttpServletRequest request, HttpServletResponse response) throws Exception{
        statisticalService.UserResultExport(id,request,response);
    }


    //平台服务费明细
    /*@PostMapping("/serviceDetails")
    public R serviceDetails(@RequestBody Statistical statistical) {
        return statisticalService.serviceDetails(statistical);
    }

    @RequestMapping("/serviceDetailsExport")
    public void serviceDetailsExport(Statistical statistical,HttpServletRequest request, HttpServletResponse response)  throws Exception{
        statisticalService.serviceDetailsExport(statistical,request,response);
    }*/
   @RequestMapping("/paymentSummaryList")
    public R paymentSummaryList(@RequestBody Statistical statistical) {
        return statisticalService.paymentSummaryList(statistical);
    }

    @RequestMapping("/paymentSummaryLists")
    public R paymentSummaryLists(@RequestBody Statistical statistical) {
        statistical.setUserId(ShiroUtils.getUserId());
        return statisticalService.paymentSummaryList(statistical);
    }
   @RequestMapping("/personResults")
    public R personResults(@RequestBody Statistical statistical) {
        return statisticalService.personResults(statistical);
    }

}
