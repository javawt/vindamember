package com.songlanyun.jymall.modules.business.sys.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.order.entity.OrderAddress;
import com.songlanyun.jymall.modules.business.score.entity.CashStatistical;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import com.songlanyun.jymall.modules.business.sys.entity.UserDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface StatisticalService {

    R summationsList();

    R realList();

    R getLogList(Map<String, Object> params);

    R graphList();

    R orderDetails(Statistical statistical);

    R cashList(CashStatistical cashStatistical);

    void orderDetailsExport(Statistical statistical, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void cashExport(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception;

    void orderExport(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception;

    R expOrderList(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception;

    void expWhoOrder(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception;

    R expWhoOrderList(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception;

    void UserResultExport(Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void autoConfirm();

    R userReturnDetai(UserDetail userDetail);

    R userTransferDetai(UserDetail userDetail);

    R userCashDetai(UserDetail userDetail);

    R paymentSummaryList(Statistical statistical);

    R personResults(Statistical statistical);

    OrderAddress getOrderAddressByOrderNo(String orderNo);
}
