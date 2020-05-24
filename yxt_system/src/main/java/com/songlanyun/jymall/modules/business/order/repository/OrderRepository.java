package com.songlanyun.jymall.modules.business.order.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.order.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:33
 */
@Repository
public interface OrderRepository extends BaseRepository<Order, String> {

    @Query("SELECT COUNT(a.orderNo) FROM  Order a WHERE a.orderStatus=80 AND a.orderType=1")
    int getCompleOrderCount();

    @Query("SELECT COUNT(a.orderNo) FROM  Order a WHERE a.orderStatus=50 AND a.orderType=2")
    int getCompleOrderCounts();

    /*@Query("SELECT SUM(a.payPrice) FROM Order a WHERE a.payStatus=20 AND a.orderStatus NOT IN(40,90)")
    BigDecimal getSaleTotal();*/

//    @Query("SELECT SUM(a.payPrice) FROM Order a WHERE a.payStatus=20 AND a.orderStatus NOT IN(40,90) AND TO_DAYS(a.createTime)=To_day( ?1)")
//    BigDecimal getTodaySaleTotal(Date date);

    @Query(value = " select * from  (select o.order_no,g.goods_name, s.spec_sku_id as sku_id,'' as 'sku_name',o.pay_price ," +
            "(case o.pay_type when 10 then '余额支付' when 20 then '微信' when 30 then '支付宝'  else '易货币' end) as pay_type ," +
            " o.create_time,o.order_status as ostutas," +
            " (case  o.order_status when 10 then '待付款 ' when 20 then '已取消' when 30 then '待提货'  when 40 then '待寄售 ' when 50 then '兑换完成' when 60 then '待发货'  when 70 then '待收货 ' when 80 then '确认收货' when 90 then '寄售中'  when 100 then '已寄售 ' when 110 then '寄售失败' when 120 then '申请退款' when 130 then '退款完成'   else '订单无状态' end) as ostatus ,"+
           " o.express_price,o.buyer_remark ,  o.user_id,o.username," +
            " a.`name` as buyname,a.phone, CONCAT(a.province_id,a.city_id,a.region_id,a.detail) as address," +
            " o.express_no,o.express_company,y.create_time as paytm,y.order_status," +" t.gysid" +
            "from  yj_order_goods g  LEFT JOIN yj_order o on o.order_no= g.order_no" +
            "left join yj_goods_sku s on g.goods_sku_id=s.goods_sku_id" +
            "left join yj_user_address a on g.user_id=a.user_id" +
            "left join yj_order_recharge y on g.order_no = y.order_no" +
            "left join  t_dealers t on t.id = o.dealers_id" +
            ") a ?1",nativeQuery = true)
    List<Map<String,Object >> expOrder(String sqlWhere);


    @Query(value = " select * from  (select o.order_no,g.goods_name, s.spec_sku_id as sku_id,'' as 'sku_name',o.pay_price ," +
            "(case o.pay_type when 10 then '余额支付' when 20 then '微信' when 30 then '支付宝'  else '易货币' end) as pay_type ," +
            " o.create_time,o.order_status as ostutas," +
            " (case  o.order_status when 10 then '待付款 ' when 20 then '已取消' when 30 then '待提货'  when 40 then '待寄售 ' when 50 then '兑换完成' when 60 then '待发货'  when 70 then '待收货 ' when 80 then '确认收货' when 90 then '寄售中'  when 100 then '已寄售 ' when 110 then '寄售失败' when 120 then '申请退款' when 130 then '退款完成'   else '订单无状态' end) as ostatus ,"+
            " o.express_price,o.buyer_remark ,  o.user_id,o.username," +
            " a.`name` as buyname,a.phone, CONCAT(a.province_id,a.city_id,a.region_id,a.detail) as address," +
            " o.express_no,o.express_company,y.create_time as paytm,y.order_status," +" t.gysid" +
            "from  yj_order_goods g  LEFT JOIN yj_order_wholesale o on o.order_no= g.order_no" +
            "left join yj_goods_sku s on g.goods_sku_id=s.goods_sku_id" +
            "left join yj_user_address a on g.user_id=a.user_id" +
            "left join yj_order_recharge y on g.order_no = y.order_no" +
            "left join  t_dealers t on t.id = o.dealers_id" +
            ") a ?1",nativeQuery = true)
    List<Map<String,Object >> expWhilOrder(String sqlWhere);
}
