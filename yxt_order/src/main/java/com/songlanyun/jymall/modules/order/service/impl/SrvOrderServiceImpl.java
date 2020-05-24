package com.songlanyun.jymall.modules.order.service.impl;

import com.songlanyun.jymall.modules.order.service.SrvOrderService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;

/**
 * @author ww
 * @date 2019-12-13
 */
@Service
public class SrvOrderServiceImpl implements SrvOrderService {

   /* @PersistenceContext
    protected EntityManager em;*/

    /**  查询某时间段内 店铺id + 服务id + 已付款 的已预订服务数 ***/
    /*@Override
    public int getSrvOrderCount(long shop_id, long srv_id, LocalDateTime startDate, LocalDateTime endDate){
         Query query = em.createQuery("select count(o.srv_id) as srv_cnt from yj_order o where o.dealers_id=:shop_id and o.srv_id=:srv_id and o.pay_status=20 and o.srv_time between :startDate and :endDate");
        query.setParameter("shop_id",shop_id);
        query.setParameter("srv_id",srv_id);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getSingleResult() != null ? Integer.parseInt(query.getSingleResult().toString()) : 0;
    }*/

}