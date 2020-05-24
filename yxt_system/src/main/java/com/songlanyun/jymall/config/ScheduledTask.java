package com.songlanyun.jymall.config;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

 /*   @Resource
    private StatisticalService statisticalService;
    @Resource
    private ProceedsService proceedsService;
    @Resource
    private SysAdminService sysAdminService;*/

    //@Scheduled(cron = "${task4}")
    public void increateStock() {
        //未付款订单返库存,普通订单/批发订单
        //sysAdminService.increateStock();
    }

    //@Scheduled(cron = "${task3}")
    public void testTask(){
        System.out.println("确认订单");
        //statisticalService.autoConfirm();
    }

    //每月执行一次
    //@Scheduled(cron = "${task2}")
    public void testTask3(){
        //proceedsService.calcYmProfit();
    }
}
