package com.songlanyun.jymall.modules.business.sys.dao;

import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import com.songlanyun.jymall.modules.business.order.entity.*;
import com.songlanyun.jymall.modules.business.score.entity.*;
import com.songlanyun.jymall.modules.business.sys.entity.PaymentSummary;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import com.songlanyun.jymall.modules.business.sys.entity.SysLogEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface StatisticalDao {
	Double getSaleTotal();//获取销售总额

	Double getCashTotal();//提现总额
	Double getTodayCashTotal();//当日提现总额

	Double getTodaySaleTotal();//今日销售额

	Integer getOrderNum();//今日支付订单数

	List<Map<String,Object>> getBoutiqueMovements();//精品走势图

	List<Map<String,Object>> getRetailMovements();//精品走势图

	List<Order> getOrderDetails(Statistical statistical);

	List<PaymentSummary> paymentSummaryList(Statistical statistical);

	List<Order> personResults(Statistical statistical);

	List<SysLogEntity> getLogList(String key);

	Double getpersonSocre(Long userId);

	Double getrechargesList(Long userId);

	List<Order> getPTauto();

	List<Order> getPTautos(Integer days);

	void getyjMoneyList();

	List<YjCash> cashList(String userId);

	List<YjGoods> getGoodsList();

	List<YjGoods> getGoodsLists(Map<String, Object> map);

	OrderAddress getOrderAddressByOrderNo(String orderNo);

	String getAddress(String orderNo);

	List<Map<String,Object>> cashListAuto(CashStatistical cashStatistical);

	List<Map<String,Object>> ptListAuto(CashStatistical cashStatistical);

	Double ptListAutoTotal(CashStatistical cashStatistical);

	List<Map<String,Object>> jsListAuto(CashStatistical cashStatistical);

	String getApliInfo(Map<String, Object> map);

	Map<String,Object> getBankInfo(Map<String, Object> map);
}
