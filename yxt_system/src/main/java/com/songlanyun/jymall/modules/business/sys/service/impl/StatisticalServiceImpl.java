package com.songlanyun.jymall.modules.business.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsService;
import com.songlanyun.jymall.modules.business.order.entity.*;
import com.songlanyun.jymall.modules.business.order.service.OrderService;
import com.songlanyun.jymall.modules.business.score.entity.*;
import com.songlanyun.jymall.modules.business.sys.dao.StatisticalDao;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.entity.PaymentSummary;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import com.songlanyun.jymall.modules.business.sys.entity.SysLogEntity;
import com.songlanyun.jymall.modules.business.sys.entity.UserDetail;
import com.songlanyun.jymall.modules.business.sys.service.StatisticalService;
import com.songlanyun.jymall.modules.business.sys.service.SysAdminService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    private YjGoodsService yjGoodsService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private OrderService orderService;
    @Resource
    private StatisticalDao statisticalDao;//统计dao
    @Resource
    private SysAdminDao sysAdminDao;
    @Resource
    private SysAdminService sysAdminService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 数据合计：商品总数、用户总数、订单总数（精品商品）、订单总数（易货商品）、订单总数（组合商品）
     * 已提现总额/当日提现总额
     */
    @Override
    public R summationsList() {
        List list = new ArrayList();
        Map<String, Object> resMap = new HashMap<>();
        //resMap.put("totalCommodity", yjGoodsService.getAllGoods());//商品总数
        resMap.put("totalUsers", sysUserService.getUserCount());//用户总数
        resMap.put("boutiqueOrders", orderService.getCompleOrderCount());//订单总数（精品商品）
        resMap.put("barterOrders", orderService.getCompleOrderCounts());//订单总数（易货商品）
        return R.ok().put("data", resMap);
    }

    /**
     * 销售总额 、今日销售额(只统计精品和组合)、今日支付订单数、今日新增用户数
     * 已提现总额/当日提现总额
     */
    @Override
    public R realList() {
        Map<String, Object> resMap = new HashMap<>();
        //今日新增用户数
        resMap.put("addUsersNum", null == sysUserService.getTodayAddCount() ? 0 : sysUserService.getTodayAddCount());
        //销售总额
        resMap.put("saleTotal", null == statisticalDao.getSaleTotal() ? 0 : BigDecimal.valueOf(statisticalDao.getSaleTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        //今日销售额
        resMap.put("todaySaleTotal", null == statisticalDao.getTodaySaleTotal() ? 0 : BigDecimal.valueOf(statisticalDao.getTodaySaleTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//今日销售额
        //今日支付订单数
        resMap.put("orderNum", statisticalDao.getOrderNum());//今日支付订单数
        //已提现总额cashTotal
        resMap.put("cashTotal", null == statisticalDao.getCashTotal() ? 0 : BigDecimal.valueOf(statisticalDao.getCashTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        //当日提现总额todayCashTotal
        resMap.put("todayCashTotal", null == statisticalDao.getTodayCashTotal() ? 0 : BigDecimal.valueOf(statisticalDao.getTodayCashTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        return R.ok().put("data", resMap);
    }

    /**
     * 曲线图（近七日交易走势）:精品、组合的销售金额走势图，按天
     */
    @Override
    public R graphList() {
        Map<String, Object> resMap = new HashMap<>();
        //精品走势图
        resMap.put("boutiqueMovements", statisticalDao.getBoutiqueMovements());
        //组合区走势图
        resMap.put("retailMovements", statisticalDao.getRetailMovements());
        return R.ok().put("data", resMap);
    }

    //订单明细
    @Override
    public R orderDetails(Statistical statistical) {
        //入参校验
        if (statistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (statistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        List<Order> resList = statisticalDao.getOrderDetails(statistical);
        resMap.put("page", statistical.getPage());
        resMap.put("size", statistical.getSize());
        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((statistical.getPage() - 1) * statistical.getSize()).
                    limit(statistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    //提现列表
    @Override
    public R cashList(CashStatistical cashStatistical) {
        //入参校验
        if (cashStatistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (cashStatistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> resList = statisticalDao.cashListAuto(cashStatistical);
        resMap.put("page", cashStatistical.getPage());
        resMap.put("size", cashStatistical.getSize());
        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((cashStatistical.getPage() - 1) * cashStatistical.getSize()).
                    limit(cashStatistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        if (null != resList && resList.size() > 0) {
            for (Map<String, Object> map : resList) {
                //根据用户id和卡号及卡片类型查询 账户姓名（开户姓名）  银行名称、银行地址
                String cashType = map.get("cashType").toString();
                if (cashType.equals("0")) {
                    map.put("bandAddress", "");
                    map.put("bandName", "");
                    map.put("accountName", statisticalDao.getApliInfo(map));
                } else {
                    Map<String, Object> bankMap = statisticalDao.getBankInfo(map);
                    if (null != bankMap) {
                        map.put("bandAddress", bankMap.get("bank_address"));
                        map.put("bandName", bankMap.get("bank"));
                        map.put("accountName", bankMap.get("real_name"));
                    } else {
                        map.put("bandAddress", "");
                        map.put("bandName", "");
                        map.put("accountName", "");
                    }
                }
            }
        }
        resMap.put("list", resList);
        //20200219新增提现总额
        resMap.put("cashTotal", null == sysAdminDao.getCashTotal(cashStatistical) ? 0 : BigDecimal.valueOf(sysAdminDao.getCashTotal(cashStatistical)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return R.ok().put("data", resMap);
    }

    //订单明细
    @Override
    public void orderDetailsExport(Statistical statistical, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 从数据库获取结果
        List<Order> resultList = statisticalDao.getOrderDetails(statistical);
        // 1.创建一个workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2.在workbook中添加一个sheet，对应Excel中的一个sheet
        // 创建单元格，设置值表头，设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);//设置自动换行
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//填充单元格
        style.setFillForegroundColor(IndexedColors.YELLOW.index);//设置单元格背景色
        HSSFFont font = wb.createFont();
        font.setBold(true);// 粗体显示
        style.setFont(font);
        // 设置表
        HSSFSheet sheet = wb.createSheet("订单明细表");
        sheet.setColumnWidth(0, 5000);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("订单号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("商品总金额");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("订单金额");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("优惠券");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("优惠券抵扣金额");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("实际付款金额");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("买家留言");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("支付方式");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("付款状态");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("付款时间");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("配送方式");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("运费金额");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("物流公司id");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("物流公司");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("物流单号");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("发货状态");
        cell.setCellStyle(style);
        cell = row.createCell(16);
        cell.setCellValue("发货时间");
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellValue("收货状态");
        cell.setCellStyle(style);
        cell = row.createCell(18);
        cell.setCellValue("收货时间");
        cell.setCellStyle(style);
        cell = row.createCell(19);
        cell.setCellValue("订单状态");
        cell.setCellStyle(style);
        cell = row.createCell(20);
        cell.setCellValue("是否已评价");
        cell.setCellStyle(style);
        cell = row.createCell(21);
        cell.setCellValue("用户id");
        cell.setCellStyle(style);
        cell = row.createCell(22);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);
        cell = row.createCell(23);
        cell.setCellValue("累积用户实际消费金额");
        cell.setCellStyle(style);
        cell = row.createCell(24);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);
        cell = row.createCell(25);
        cell.setCellValue("更新时间");
        cell.setCellStyle(style);
        cell = row.createCell(26);
        cell.setCellValue("订单类型");
        cell.setCellStyle(style);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 循环将数据写入Excel
        if (null != resultList && resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                row = sheet.createRow(i + 1);
                Order order = resultList.get(i);
                // 创建单元格，设置值
                HSSFCell cell0 = row.createCell(0);
                HSSFCell cell1 = row.createCell(1);
                HSSFCell cell2 = row.createCell(2);
                HSSFCell cell3 = row.createCell(3);
                HSSFCell cell4 = row.createCell(4);
                HSSFCell cell5 = row.createCell(5);
                HSSFCell cell6 = row.createCell(6);
                HSSFCell cell7 = row.createCell(7);
                HSSFCell cell8 = row.createCell(8);
                HSSFCell cell9 = row.createCell(9);
                HSSFCell cell10 = row.createCell(10);
                HSSFCell cell11 = row.createCell(11);
                HSSFCell cell12 = row.createCell(12);
                HSSFCell cell13 = row.createCell(13);
                HSSFCell cell14 = row.createCell(14);
                HSSFCell cell15 = row.createCell(15);
                HSSFCell cell16 = row.createCell(16);
                HSSFCell cell17 = row.createCell(17);
                HSSFCell cell18 = row.createCell(18);
                HSSFCell cell19 = row.createCell(19);
                HSSFCell cell20 = row.createCell(20);
                HSSFCell cell21 = row.createCell(21);
                HSSFCell cell22 = row.createCell(22);
                HSSFCell cell23 = row.createCell(23);
                HSSFCell cell24 = row.createCell(24);
                HSSFCell cell25 = row.createCell(25);
                HSSFCell cell26 = row.createCell(26);
                cell0.setCellValue((String) order.getOrderNo());
                //cell0.setCellStyle(style);
                cell1.setCellValue(null == order.getTotalPrice() ? 0 : (double) order.getTotalPrice().doubleValue());
                //cell1.setCellStyle(style);
                cell2.setCellValue(null == order.getOrderPrice() ? 0 : (double) order.getOrderPrice().doubleValue());
                //cell2.setCellStyle(style);
                cell3.setCellValue(null == order.getCouponId() ? 0 : (Integer) order.getCouponId());
                //cell3.setCellStyle(style);
                cell4.setCellValue(null == order.getCouponMoney() ? 0 : (double) order.getCouponMoney().doubleValue());
                //cell4.setCellStyle(style);
                cell5.setCellValue(null == order.getPayPrice() ? 0 : (double) order.getPayPrice().doubleValue());
                //cell5.setCellStyle(style);
                cell6.setCellValue((String) order.getBuyerRemark());
                //cell6.setCellStyle(style);
                cell7.setCellValue(null == order.getPayType() ? 0 : (Integer) order.getPayType());
                //cell7.setCellStyle(style);
                cell8.setCellValue(null == order.getPayStatus() ? 0 : (Integer) order.getPayStatus());
                //cell8.setCellStyle(style);
                cell9.setCellValue(null == order.getPayTime() ? null : (String) sdf.format(order.getPayTime()));
                //cell9.setCellStyle(style);
                cell10.setCellValue(null == order.getDeliveryType() ? 0 : (Integer) order.getDeliveryType());
                //cell10.setCellStyle(style);

                cell11.setCellValue(null == order.getExpressPrice() ? 0 : (Double) order.getExpressPrice().doubleValue());
                cell12.setCellValue(null == order.getExpressId() ? 0 : (Integer) order.getExpressId());
                cell13.setCellValue((String) order.getExpressCompany());
                cell14.setCellValue((String) order.getExpressNo());
                cell15.setCellValue(null == order.getDeliveryStatus() ? 0 : (Integer) order.getDeliveryStatus());
                cell16.setCellValue(null == order.getDeliveryTime() ? null : (String) sdf.format(order.getDeliveryTime()));
                cell17.setCellValue(null == order.getReceiptStatus() ? 0 : (Integer) order.getReceiptStatus());
                cell18.setCellValue(null == order.getReceiptTime() ? null : (String) sdf.format(order.getReceiptTime()));
                cell19.setCellValue(null == order.getOrderStatus() ? 0 : (Integer) order.getOrderStatus());
                cell20.setCellValue(null == order.getIsComment() ? 0 : (Integer) order.getIsComment());
                cell21.setCellValue(null == order.getUserId() ? null: (String) order.getUserId());
                cell22.setCellValue((String) order.getUsername());
                cell23.setCellValue(null == order.getIsUserExpend() ? 0 : (Integer) order.getIsUserExpend());
                cell24.setCellValue(null == order.getCreateTime() ? null : (String) sdf.format(order.getCreateTime()));
                cell25.setCellValue(null == order.getUpdateTime() ? null : (String) sdf.format(order.getUpdateTime()));
                cell26.setCellValue(null == order.getOrderType() ? 0 : (Integer) order.getOrderType());


            }
        }
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "订单明细表" + df.format(day);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        out.flush();
    }


    //递归查询所有子层级用户
    public List<Integer> getAllUser(List<Integer> subIdList, List<Integer> resList) {
        if (null != subIdList && subIdList.size() > 0) {
            for (Integer integer : subIdList) {
                if (!resList.contains(integer)) {
                    resList.add(integer);
                }
                resList = getAllUser(sysAdminDao.getSubUserId(integer), resList);
            }
        }
        return resList;
    }


    @Override
    public void UserResultExport(Integer id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 从数据库获取结果
        List<Integer> subIdList = new ArrayList<>();
        subIdList.add(id);
        List<Integer> resList = new ArrayList<>();//当前用户及下面所有用户的id集合
        resList = getAllUser(subIdList, resList);
        // 1.创建一个workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2.在workbook中添加一个sheet，对应Excel中的一个sheet
        // 创建单元格，设置值表头，设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);//设置自动换行
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//填充单元格
        style.setFillForegroundColor(IndexedColors.YELLOW.index);//设置单元格背景色
        HSSFFont font = wb.createFont();
        font.setBold(true);// 粗体显示
        style.setFont(font);
        // 设置表
        HSSFSheet sheet = wb.createSheet(sysAdminDao.getUserInfo(id).get("username") + "业绩表");
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("用户ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("用户昵称");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("等级");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("业绩(元)");
        cell.setCellStyle(style);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss");
        // 循环将数据写入Excel
        if (null != resList && resList.size() > 0) {
            for (int i = 0; i < resList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> userMap = sysAdminDao.getUserInfo(resList.get(i));
                // 创建单元格，设置值
                HSSFCell cell0 = row.createCell(0);
                HSSFCell cell1 = row.createCell(1);
                HSSFCell cell2 = row.createCell(2);
                HSSFCell cell3 = row.createCell(3);
                HSSFCell cell4 = row.createCell(4);
                cell0.setCellValue((Long) userMap.get("userId"));
                cell1.setCellValue((String) userMap.get("username"));
                cell2.setCellValue((String) userMap.get("niceName"));
                cell3.setCellValue((String) userMap.get("gradeId"));
                cell4.setCellValue(sysAdminDao.getUseryj(resList.get(i)));
            }
        }
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "用户(" + sysAdminDao.getUserInfo(id).get("username") + ")业绩表" + df.format(day);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        out.flush();
    }


    //定时任务确认
    @Override
    @Transient
    public void autoConfirm() {
        //tsh 2019/12/56 用户确认订单7天后标识不可退
        Integer days = Integer.parseInt(sysAdminDao.getDictValue(13));
        List<Order> orderCompleteList = statisticalDao.getPTautos(days);
        if (null != orderCompleteList && orderCompleteList.size() > 0) {
            for (Order order : orderCompleteList) {
                order.setIsComplete("1");//自动确认不可退货
                orderService.save(order);
                sysAdminDao.updateRecharge(order.getOrderNo());//ww 支付表
            }
        }


        //获取普通待确认订单表
        List<Order> orderList = statisticalDao.getPTauto();
        if (null != orderList && orderList.size() > 0) {
            for (Order order : orderList) {
                order.setReceiptStatus(20);
                order.setReceiptTime(new Date());
                order.setOrderStatus(Order.xlgOrderStatus.PAYMENT_FAILED);
                order.setUpdateTime(new Date());
                order.setIsComplete("1");//自动确认不可退货
                orderService.save(order);
                sysAdminDao.updateRecharge(order.getOrderNo());//ww 支付表
            }
        }
    }


    /**
     * yj_wholesa_log 寄售收益
     * yj_transfer  转账
     * yj_cash  提现
     * yj_money 分红
     * yj_money_team 团队收益
     * yj_order_recharge 精品支付
     * <p>
     * //0.收支明细  1.转账   2.提现   3.易货币 4、明细总表
     */
    @Override
    public R userReturnDetai(UserDetail userDetail) {
        //入参校验
        if (userDetail.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (userDetail.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("page", userDetail.getPage());
        resMap.put("size", userDetail.getSize());


        List<Map<String, Object>> resList = new ArrayList<>();
        if (userDetail.getType() == 2) {
            //提现
            List<YjCash> cashList = statisticalDao.cashList(ShiroUtils.getUserId());
            if (null != cashList && cashList.size() > 0) {
                for (YjCash yjCash : cashList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("opeDate", yjCash.getCreateDate());
                    map.put("opeAR", "-");
                    map.put("opeName", "提现");//操作名称
                    map.put("opeMoney", yjCash.getMoney());
                    map.put("orderNo", yjCash.getId());
                    resList.add(map);
                }
            }
        } else {

            //提现
            List<YjCash> cashList = statisticalDao.cashList(ShiroUtils.getUserId());
            if (null != cashList && cashList.size() > 0) {
                for (YjCash yjCash : cashList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("opeDate", yjCash.getCreateDate());
                    map.put("opeAR", "-");
                    map.put("opeName", "提现");//操作名称
                    map.put("opeMoney", yjCash.getMoney());
                    map.put("orderNo", yjCash.getId());
                    resList.add(map);
                }
            }
        }


        Collections.sort(resList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1.get("opeDate") == null && o2.get("opeDate") == null)
                    return 0;
                if (o1.get("opeDate") == null)
                    return -1;
                if (o2.get("opeDate") == null)
                    return 1;
                return Long.valueOf(JSON.toJSONString(o2.get("opeDate"))).compareTo(Long.valueOf(JSON.toJSONString(o1.get("opeDate"))));
            }
        });


        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((userDetail.getPage() - 1) * userDetail.getSize()).
                    limit(userDetail.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }

        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    /**
     * 转账明细
     *
     * @param userDetail
     * @return
     */
    @Override
    public R userTransferDetai(UserDetail userDetail) {
        //入参校验
        if (userDetail.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (userDetail.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("page", userDetail.getPage());
        resMap.put("size", userDetail.getSize());


        List<Map<String, Object>> resList = new ArrayList<>();


        Collections.sort(resList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1.get("opeDate") == null && o2.get("opeDate") == null)
                    return 0;
                if (o1.get("opeDate") == null)
                    return -1;
                if (o2.get("opeDate") == null)
                    return 1;
                return Long.valueOf(JSON.toJSONString(o1.get("opeDate"))).compareTo(Long.valueOf(JSON.toJSONString(o2.get("opeDate"))));
            }
        });

        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((userDetail.getPage() - 1) * userDetail.getSize()).
                    limit(userDetail.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }

        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    /**
     * 提现明细
     *
     * @param userDetail
     * @return
     */
    @Override
    public R userCashDetai(UserDetail userDetail) {
        //入参校验
        if (userDetail.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (userDetail.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("page", userDetail.getPage());
        resMap.put("size", userDetail.getSize());


        List<Map<String, Object>> resList = new ArrayList<>();

        //提现
        List<YjCash> cashList = statisticalDao.cashList(ShiroUtils.getUserId());
        if (null != cashList && cashList.size() > 0) {
            for (YjCash yjCash : cashList) {
                Map<String, Object> map = new HashMap<>();
                map.put("opeDate", yjCash.getCreateDate());
                map.put("opeAR", "-");
                map.put("opeName", "提现");//操作名称
                map.put("opeMoney", yjCash.getMoney());
                map.put("orderNo", yjCash.getId());
                resList.add(map);
            }
        }
        Collections.sort(resList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1.get("opeDate") == null && o2.get("opeDate") == null)
                    return 0;
                if (o1.get("opeDate") == null)
                    return -1;
                if (o2.get("opeDate") == null)
                    return 1;
                return Long.valueOf(JSON.toJSONString(o2.get("opeDate"))).compareTo(Long.valueOf(JSON.toJSONString(o1.get("opeDate"))));
            }
        });

        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((userDetail.getPage() - 1) * userDetail.getSize()).
                    limit(userDetail.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }

        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    @Override
    public R paymentSummaryList(Statistical statistical) {
        //入参校验
        if (statistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (statistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        if (ShiroUtils.getUserId().equals(1L)) {
            statistical.setUserId(null);
        }
        Map<String, Object> resMap = new HashMap<>();
        List<PaymentSummary> resList = statisticalDao.paymentSummaryList(statistical);
        resMap.put("page", statistical.getPage());
        resMap.put("size", statistical.getSize());
        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((statistical.getPage() - 1) * statistical.getSize()).
                    limit(statistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    @Override
    public R personResults(Statistical statistical) {
        //入参校验
        if (statistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (statistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        if (ShiroUtils.getUserId().equals(1L)) {
            statistical.setUserId(null);
        }
        Map<String, Object> resMap = new HashMap<>();
        List<Order> resList = statisticalDao.personResults(statistical);
        resMap.put("page", statistical.getPage());
        resMap.put("size", statistical.getSize());
        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((statistical.getPage() - 1) * statistical.getSize()).
                    limit(statistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    @Override
    public OrderAddress getOrderAddressByOrderNo(String orderNo) {
        return statisticalDao.getOrderAddressByOrderNo(orderNo);
    }

    @Override
    public R getLogList(Map<String, Object> params) {
        //入参校验
        if (Integer.parseInt(params.get("page").toString()) > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (Integer.parseInt(params.get("limit").toString()) >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        List<SysLogEntity> resList = statisticalDao.getLogList(params.get("key").toString());
        resMap.put("currPage", Integer.parseInt(params.get("page").toString()));
        resMap.put("pageSize", Integer.parseInt(params.get("limit").toString()));
        if (null != resList && resList.size() > 0) {
            resMap.put("totalCount", resList.size());
            resList = resList.stream().skip((Integer.parseInt(params.get("page").toString()) - 1) * Integer.parseInt(params.get("limit").toString())).
                    limit(Integer.parseInt(params.get("limit").toString())).collect(Collectors.toList());
        } else {
            resMap.put("totalCount", 0);
        }
        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }


    //提现导出
    @Override
    public void cashExport(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception {
        //1、列表显示：ID、用户名、提现金额、提现手续费、实提金额、账户类型（银行卡/支付宝）、账户姓名（开户姓名）、
        // 账户账号（银行/支付宝账号）、银行名称、银行地址、备注、操作人、状态、创建时间、审核时间、
        // 操作（审核通过、审核失败（审核失败，钱要退回给余额））
        // 从数据库获取结果
        //List<YjCash> resultList = yjCashService.findAll();
        List<Map<String, Object>> resList = statisticalDao.cashListAuto(cashStatistical);
        if (null != resList && resList.size() > 0) {
            for (Map<String, Object> map : resList) {
                //根据用户id和卡号及卡片类型查询 账户姓名（开户姓名）  银行名称、银行地址
                String cashType = map.get("cashType").toString();
                if (cashType.equals("0")) {
                    map.put("bandAddress", "");
                    map.put("bandName", "");
                    map.put("accountName", statisticalDao.getApliInfo(map));
                } else {
                    Map<String, Object> bankMap = statisticalDao.getBankInfo(map);
                    if (null != bankMap) {
                        map.put("bandAddress", bankMap.get("bank_address"));
                        map.put("bandName", bankMap.get("bank"));
                        map.put("accountName", bankMap.get("real_name"));
                    } else {
                        map.put("bandAddress", "");
                        map.put("bandName", "");
                        map.put("accountName", "");
                    }
                }
            }
        }

        // 1.创建一个workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2.在workbook中添加一个sheet，对应Excel中的一个sheet
        // 创建单元格，设置值表头，设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);//设置自动换行
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//填充单元格
        style.setFillForegroundColor(IndexedColors.YELLOW.index);//设置单元格背景色
        HSSFFont font = wb.createFont();
        font.setBold(true);// 粗体显示
        style.setFont(font);
        // 设置表
        HSSFSheet sheet = wb.createSheet("提现记录表");
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(7, 5000);
        ;
        sheet.setColumnWidth(13, 5000);
        ;
        sheet.setColumnWidth(14, 5000);
        ;
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("用户名");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("提现金额");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("提现手续费");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("实提金额");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("账户类型");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("账户姓名");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("账户账号");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("银行名称");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("银行地址");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("备注");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("操作人");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("状态");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("审核时间");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("操作");
        cell.setCellStyle(style);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-MM-ss");
        // 循环将数据写入Excel
        if (null != resList && resList.size() > 0) {
            for (int i = 0; i < resList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> yjCash = resList.get(i);
                // 创建单元格，设置值
                HSSFCell cell0 = row.createCell(0);
                HSSFCell cell1 = row.createCell(1);
                HSSFCell cell2 = row.createCell(2);
                HSSFCell cell3 = row.createCell(3);
                HSSFCell cell4 = row.createCell(4);
                HSSFCell cell5 = row.createCell(5);
                HSSFCell cell6 = row.createCell(6);
                HSSFCell cell7 = row.createCell(7);
                HSSFCell cell8 = row.createCell(8);
                HSSFCell cell9 = row.createCell(9);
                HSSFCell cell10 = row.createCell(10);
                HSSFCell cell11 = row.createCell(11);
                HSSFCell cell12 = row.createCell(12);
                HSSFCell cell13 = row.createCell(13);
                HSSFCell cell14 = row.createCell(14);
                HSSFCell cell15 = row.createCell(15);
                cell0.setCellValue(null == yjCash.get("id") ? "" : yjCash.get("id").toString());
                cell1.setCellValue(null == yjCash.get("username") ? "" : yjCash.get("username").toString());
                cell2.setCellValue(null == yjCash.get("money") ? "" : yjCash.get("money").toString());
                cell3.setCellValue(null == yjCash.get("poundage") ? "" : yjCash.get("poundage").toString());
                cell4.setCellValue(null == yjCash.get("tax_money") ? "" : yjCash.get("tax_money").toString());
                if (null == yjCash.get("cashType")) {
                    cell5.setCellValue("");
                } else {
                    if (yjCash.get("cashType").toString().equals("0")) {
                        cell5.setCellValue("支付宝");
                    } else {
                        cell5.setCellValue("银行卡");
                    }
                }
                cell6.setCellValue(null == yjCash.get("accountName") ? "" : yjCash.get("accountName").toString());
                cell7.setCellValue(null == yjCash.get("idCard") ? "" : yjCash.get("idCard").toString());
                cell8.setCellValue(null == yjCash.get("bandName") ? "" : yjCash.get("bandName").toString());
                cell9.setCellValue(null == yjCash.get("bandAddress") ? "" : yjCash.get("bandAddress").toString());
                cell10.setCellValue(null == yjCash.get("remark") ? "" : yjCash.get("remark").toString());
                cell11.setCellValue(null == yjCash.get("operName") ? "" : yjCash.get("operName").toString());
                if (null == yjCash.get("status")) {
                    cell12.setCellValue("");
                } else {
                    if (yjCash.get("status").toString().equals("2")) {
                        cell12.setCellValue("审核拒绝");
                    } else if (yjCash.get("status").toString().equals("1")) {
                        cell12.setCellValue("审核通过");
                    } else {
                        cell12.setCellValue("提交申请");
                    }
                }
                cell13.setCellValue(null == yjCash.get("createTime") ? "" : yjCash.get("createTime").toString());
                cell14.setCellValue(null == yjCash.get("opterDate") ? "" : yjCash.get("opterDate").toString());
                if (null == yjCash.get("status")) {
                    cell15.setCellValue("");
                } else {
                    if (yjCash.get("status").toString().equals("2")) {
                        cell15.setCellValue("审核拒绝");
                    } else if (yjCash.get("status").toString().equals("1")) {
                        cell15.setCellValue("审核通过");
                    } else {
                        cell15.setCellValue("提交申请");
                    }
                }
            }
        }
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "提现记录表" + df.format(day);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        out.flush();
    }


    //普通订单导出
    @Override
    public void orderExport(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception {
        //订单号	商品名称	商品规格	批发单价	数量
        // 支付方式	付款金额	运费金额	下单时间	买家
        // 买家留言	收货人姓名	联系电话	收货人地址	物流公司
        // 物流单号	付款状态	付款时间
        List<Map<String, Object>> resList = statisticalDao.ptListAuto(cashStatistical);
        // 1.创建一个workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2.在workbook中添加一个sheet，对应Excel中的一个sheet
        // 创建单元格，设置值表头，设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);//设置自动换行
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//填充单元格
        style.setFillForegroundColor(IndexedColors.YELLOW.index);//设置单元格背景色
        HSSFFont font = wb.createFont();
        font.setBold(true);// 粗体显示
        style.setFont(font);
        // 设置表
        HSSFSheet sheet = wb.createSheet("普通订单记录表");
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(2, 9000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 9000);
        sheet.setColumnWidth(17, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 9000);
        sheet.setColumnWidth(19, 5000);
        sheet.setColumnWidth(18, 5000);
        sheet.setColumnWidth(20, 5000);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("订单号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("供应商");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("商品名称");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("商品规格");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("零售单价");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("数量");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("订单类型");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("支付方式");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("订单实付款");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("零售付款金额");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("运费金额");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("买家");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("买家留言");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("收货人姓名");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("联系电话");
        cell.setCellStyle(style);
        cell = row.createCell(16);
        cell.setCellValue("收货人地址");
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellValue("物流公司");
        cell.setCellStyle(style);
        cell = row.createCell(18);
        cell.setCellValue("物流单号");
        cell.setCellStyle(style);
        cell = row.createCell(19);
        cell.setCellValue("付款状态");
        cell.setCellStyle(style);
        cell = row.createCell(20);
        cell.setCellValue("付款时间");
        cell.setCellStyle(style);
        // 循环将数据写入Excel
        if (null != resList && resList.size() > 0) {
            for (int i = 0; i < resList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> yjCash = resList.get(i);
                // 创建单元格，设置值
                HSSFCell cell0 = row.createCell(0);
                HSSFCell cell1 = row.createCell(1);
                HSSFCell cell2 = row.createCell(2);
                HSSFCell cell3 = row.createCell(3);
                HSSFCell cell4 = row.createCell(4);
                HSSFCell cell5 = row.createCell(5);
                HSSFCell cell6 = row.createCell(6);
                HSSFCell cell7 = row.createCell(7);
                HSSFCell cell8 = row.createCell(8);
                HSSFCell cell9 = row.createCell(9);
                HSSFCell cell10 = row.createCell(10);
                HSSFCell cell11 = row.createCell(11);
                HSSFCell cell12 = row.createCell(12);
                HSSFCell cell13 = row.createCell(13);
                HSSFCell cell14 = row.createCell(14);
                HSSFCell cell15 = row.createCell(15);
                HSSFCell cell16 = row.createCell(16);
                HSSFCell cell17 = row.createCell(17);
                HSSFCell cell18 = row.createCell(18);
                HSSFCell cell19 = row.createCell(19);
                HSSFCell cell20 = row.createCell(20);
                cell0.setCellValue(null == yjCash.get("order_no") ? "" : yjCash.get("order_no").toString());
                cell1.setCellValue(null == yjCash.get("dea_name") ? "" : yjCash.get("dea_name").toString());
                cell2.setCellValue(null == yjCash.get("goods_name") ? "" : yjCash.get("goods_name").toString());
                cell3.setCellValue(null == yjCash.get("spec_sku_id") ? "" : sysAdminService.getSecValue(null == yjCash.get("spec_sku_id") ? "" : yjCash.get("spec_sku_id").toString()));
                cell4.setCellValue(null == yjCash.get("ls_price") ? "" : yjCash.get("ls_price").toString());
                cell5.setCellValue(null == yjCash.get("totalNum") ? "" : yjCash.get("totalNum").toString());
                cell6.setCellValue(null == yjCash.get("order_type") ? "" : yjCash.get("order_type").toString());
                if (null != yjCash.get("order_type")) {
                    //0-组合的订单1-精品区订单2-易货区订单
                    if (yjCash.get("order_type").toString().equals("0")) {
                        cell6.setCellValue("组合区订单");
                    } else if (yjCash.get("order_type").toString().equals("1")) {
                        cell6.setCellValue("精品区订单");
                    } else if (yjCash.get("order_type").toString().equals("2")) {
                        cell6.setCellValue("易货区订单");
                    } else {
                        cell6.setCellValue("");
                    }
                } else {
                    cell6.setCellValue("");
                }
                if (null != yjCash.get("pay_type")) {
                    //支付方式(10余额支付 20微信支付 30支付宝支付40易货币)
                    if (yjCash.get("pay_type").toString().equals("10")) {
                        cell7.setCellValue("余额支付");
                    } else if (yjCash.get("pay_type").toString().equals("20")) {
                        cell7.setCellValue("微信支付");
                    } else if (yjCash.get("pay_type").toString().equals("30")) {
                        cell7.setCellValue("支付宝支付");
                    } else if (yjCash.get("pay_type").toString().equals("40")) {
                        cell7.setCellValue("易货币");
                    } else {
                        cell7.setCellValue("");
                    }
                } else {
                    cell7.setCellValue("");
                }
                cell8.setCellValue(null == yjCash.get("pay_price") ? "" : yjCash.get("pay_price").toString());
                cell9.setCellValue(null == yjCash.get("ws_price") ? "" : yjCash.get("ws_price").toString());
                cell10.setCellValue(null == yjCash.get("express_price") ? "" : yjCash.get("express_price").toString());
                cell11.setCellValue(null == yjCash.get("createTime") ? "" : yjCash.get("createTime").toString());
                cell12.setCellValue(null == yjCash.get("buyerName") ? "" : yjCash.get("buyerName").toString());
                cell13.setCellValue(null == yjCash.get("buyer_remark") ? "" : yjCash.get("buyer_remark").toString());
                cell14.setCellValue(null == yjCash.get("shName") ? "" : yjCash.get("shName").toString());
                cell15.setCellValue(null == yjCash.get("shPhone") ? "" : yjCash.get("shPhone").toString());
                cell16.setCellValue(null == yjCash.get("shAddress") ? "" : yjCash.get("shAddress").toString());
                cell17.setCellValue(null == yjCash.get("express_company") ? "" : yjCash.get("express_company").toString());
                cell18.setCellValue(null == yjCash.get("express_no") ? "" : yjCash.get("express_no").toString());
                cell19.setCellValue(null == yjCash.get("pay_status") ? "" : yjCash.get("pay_status").toString());
                if (null != yjCash.get("pay_status")) {
                    //付款状态(10未付款 20已付款)
                    if (yjCash.get("pay_status").toString().equals("20")) {
                        cell19.setCellValue("已付款");
                    } else {
                        cell19.setCellValue("未付款");
                    }
                } else {
                    cell19.setCellValue("未付款");
                }
                cell20.setCellValue(null == yjCash.get("pay_time") ? "" : yjCash.get("pay_time").toString());

            }
        }
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "普通订单记录表" + df.format(day);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        out.flush();
    }

    //普通订单列表
    @Override
    public R expOrderList(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception {
        //订单号	商品名称	商品规格	批发单价	数量
        // 支付方式	付款金额	运费金额	下单时间	买家
        // 买家留言	收货人姓名	联系电话	收货人地址	物流公司
        // 物流单号	付款状态	付款时间
        //入参校验
        if (cashStatistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (cashStatistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        int offset = (cashStatistical.getPage() - 1) * cashStatistical.getSize();
        cashStatistical.setOffset(offset);
        List<Map<String, Object>> resList = statisticalDao.ptListAuto(cashStatistical);
        resMap.put("page", cashStatistical.getPage());
        resMap.put("size", cashStatistical.getSize());
        if (null != resList && resList.size() > 0) {
            resMap.put("total", resList.size());
            resList = resList.stream().skip((cashStatistical.getPage() - 1) * cashStatistical.getSize()).
                    limit(cashStatistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        if (null != resList && resList.size() > 0) {
            for (Map<String, Object> map : resList) {
                if (null != map.get("pay_type")) {
                    //支付方式(10余额支付 20微信支付 30支付宝支付40易货币)
                    if (map.get("pay_type").toString().equals("10")) {
                        map.put("pay_type", "余额支付");
                    } else if (map.get("pay_type").toString().equals("20")) {
                        map.put("pay_type", "微信支付");
                    } else if (map.get("pay_type").toString().equals("30")) {
                        map.put("pay_type", "支付宝支付");
                    } else if (map.get("pay_type").toString().equals("40")) {
                        map.put("pay_type", "易货币");
                    } else {
                        map.put("pay_type", "");
                    }
                } else {
                    map.put("pay_type", "");
                }

                if (null != map.get("pay_status")) {
                    //付款状态(10未付款 20已付款)
                    if (map.get("pay_status").toString().equals("20")) {
                        map.put("pay_status", "已付款");
                    } else {
                        map.put("pay_status", "未付款");
                    }
                } else {
                    map.put("pay_status", "未付款");
                }

                if (null != map.get("order_type")) {
                    //0-组合的订单1-精品区订单2-易货区订单
                    if (map.get("order_type").toString().equals("0")) {
                        map.put("order_type", "组合区订单");
                    } else if (map.get("order_type").toString().equals("1")) {
                        map.put("order_type", "精品区订单");
                    } else if (map.get("order_type").toString().equals("2")) {
                        map.put("order_type", "易货区订单");
                    } else {
                        map.put("order_type", "");
                    }
                } else {
                    map.put("order_type", "");
                }
            }
        }
        resMap.put("list", resList);
        resMap.put("totalNum", null == statisticalDao.ptListAutoTotal(cashStatistical) ? 0 : BigDecimal.valueOf(statisticalDao.ptListAutoTotal(cashStatistical)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return R.ok().put("data", resMap);
    }

    //寄售订单列表
    @Override
    public R expWhoOrderList(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception {
        //订单号	商品名称	商品规格	批发单价	数量
        // 支付方式	付款金额	运费金额	下单时间	买家
        // 买家留言	收货人姓名	联系电话	收货人地址	物流公司
        // 物流单号	付款状态	付款时间
        //入参校验
        if (cashStatistical.getPage() > 0) {
        } else {
            return R.error("页码参数输入错误");
        }
        if (cashStatistical.getSize() >= 0) {
        } else {
            return R.error("页数参数输入错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> resList = statisticalDao.jsListAuto(cashStatistical);
        resMap.put("page", cashStatistical.getPage());
        resMap.put("size", cashStatistical.getSize());
        if (null != resList && resList.size() > 0) {
            Collections.sort(resList, new Comparator<Map<String, Object>>() {
                @SneakyThrows
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Date name1 = format.parse(o1.get("createTime").toString()); //这里时间可以自己定
                    Date name2 = format.parse(o2.get("createTime").toString());
                    return name2.compareTo(name1);
                }
            });


            resMap.put("total", resList.size());


            resList = resList.stream().skip((cashStatistical.getPage() - 1) * cashStatistical.getSize()).
                    limit(cashStatistical.getSize()).collect(Collectors.toList());
        } else {
            resMap.put("total", 0);
        }
        if (null != resList && resList.size() > 0) {
            for (Map<String, Object> map : resList) {
                if (null != map.get("pay_type")) {
                    //支付方式(10余额支付 20微信支付 30支付宝支付40易货币)
                    if (map.get("pay_type").toString().equals("10")) {
                        map.put("pay_type", "余额支付");
                    } else if (map.get("pay_type").toString().equals("20")) {
                        map.put("pay_type", "微信支付");
                    } else if (map.get("pay_type").toString().equals("30")) {
                        map.put("pay_type", "支付宝支付");
                    } else if (map.get("pay_type").toString().equals("40")) {
                        map.put("pay_type", "易货币");
                    } else {
                        map.put("pay_type", "");
                    }
                } else {
                    map.put("pay_type", "");
                }

                if (null != map.get("pay_status")) {
                    //付款状态(10未付款 20已付款)
                    if (map.get("pay_status").toString().equals("20")) {
                        map.put("pay_status", "已付款");
                    } else {
                        map.put("pay_status", "未付款");
                    }
                } else {
                    map.put("pay_status", "未付款");
                }

                if (null != map.get("order_type")) {
                    //0-组合的订单1-精品区订单2-易货区订单
                    if (map.get("order_type").toString().equals("0")) {
                        map.put("order_type", "组合区订单");
                    } else if (map.get("order_type").toString().equals("1")) {
                        map.put("order_type", "精品区订单");
                    } else if (map.get("order_type").toString().equals("2")) {
                        map.put("order_type", "易货区订单");
                    } else {
                        map.put("order_type", "");
                    }
                } else {
                    map.put("order_type", "");
                }
            }
        }
        resMap.put("list", resList);
        return R.ok().put("data", resMap);
    }

    //寄售订单导出
    @Override
    public void expWhoOrder(HttpServletRequest request, HttpServletResponse response, CashStatistical cashStatistical) throws Exception {
        //订单号	商品名称	商品规格	批发单价	数量
        // 支付方式	付款金额	运费金额	下单时间	买家
        // 买家留言	收货人姓名	联系电话	收货人地址	物流公司
        // 物流单号	付款状态	付款时间
        List<Map<String, Object>> resList = statisticalDao.jsListAuto(cashStatistical);
        // 1.创建一个workbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 2.在workbook中添加一个sheet，对应Excel中的一个sheet
        // 创建单元格，设置值表头，设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setWrapText(true);//设置自动换行
        style.setAlignment(HorizontalAlignment.CENTER);//水平居中显示
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        style.setBorderBottom(BorderStyle.THIN);//下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);//填充单元格
        style.setFillForegroundColor(IndexedColors.YELLOW.index);//设置单元格背景色
        HSSFFont font = wb.createFont();
        font.setBold(true);// 粗体显示
        style.setFont(font);
        // 设置表
        HSSFSheet sheet = wb.createSheet("批发订单记录表");
        sheet.setColumnWidth(0, 8000);
        sheet.setColumnWidth(2, 9000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 9000);
        sheet.setColumnWidth(17, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 9000);
        sheet.setColumnWidth(19, 5000);
        sheet.setColumnWidth(18, 5000);
        sheet.setColumnWidth(20, 5000);
        HSSFRow row = sheet.createRow((int) 0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("订单号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("供应商");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("商品名称");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("商品规格");
        cell.setCellStyle(style);
        cell = row.createCell(4);
        cell.setCellValue("批发单价");
        cell.setCellStyle(style);
        cell = row.createCell(5);
        cell.setCellValue("数量");
        cell.setCellStyle(style);
        cell = row.createCell(6);
        cell.setCellValue("订单类型");
        cell.setCellStyle(style);
        cell = row.createCell(7);
        cell.setCellValue("支付方式");
        cell.setCellStyle(style);
        cell = row.createCell(8);
        cell.setCellValue("订单实付款");
        cell.setCellStyle(style);
        cell = row.createCell(9);
        cell.setCellValue("批发付款金额");
        cell.setCellStyle(style);
        cell = row.createCell(10);
        cell.setCellValue("运费金额");
        cell.setCellStyle(style);
        cell = row.createCell(11);
        cell.setCellValue("下单时间");
        cell.setCellStyle(style);
        cell = row.createCell(12);
        cell.setCellValue("买家");
        cell.setCellStyle(style);
        cell = row.createCell(13);
        cell.setCellValue("买家留言");
        cell.setCellStyle(style);
        cell = row.createCell(14);
        cell.setCellValue("收货人姓名");
        cell.setCellStyle(style);
        cell = row.createCell(15);
        cell.setCellValue("联系电话");
        cell.setCellStyle(style);
        cell = row.createCell(16);
        cell.setCellValue("收货人地址");
        cell.setCellStyle(style);
        cell = row.createCell(17);
        cell.setCellValue("物流公司");
        cell.setCellStyle(style);
        cell = row.createCell(18);
        cell.setCellValue("物流单号");
        cell.setCellStyle(style);
        cell = row.createCell(19);
        cell.setCellValue("付款状态");
        cell.setCellStyle(style);
        cell = row.createCell(20);
        cell.setCellValue("付款时间");
        cell.setCellStyle(style);
        // 循环将数据写入Excel
        if (null != resList && resList.size() > 0) {
            for (int i = 0; i < resList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> yjCash = resList.get(i);
                // 创建单元格，设置值
                HSSFCell cell0 = row.createCell(0);
                HSSFCell cell1 = row.createCell(1);
                HSSFCell cell2 = row.createCell(2);
                HSSFCell cell3 = row.createCell(3);
                HSSFCell cell4 = row.createCell(4);
                HSSFCell cell5 = row.createCell(5);
                HSSFCell cell6 = row.createCell(6);
                HSSFCell cell7 = row.createCell(7);
                HSSFCell cell8 = row.createCell(8);
                HSSFCell cell9 = row.createCell(9);
                HSSFCell cell10 = row.createCell(10);
                HSSFCell cell11 = row.createCell(11);
                HSSFCell cell12 = row.createCell(12);
                HSSFCell cell13 = row.createCell(13);
                HSSFCell cell14 = row.createCell(14);
                HSSFCell cell15 = row.createCell(15);
                HSSFCell cell16 = row.createCell(16);
                HSSFCell cell17 = row.createCell(17);
                HSSFCell cell18 = row.createCell(18);
                HSSFCell cell19 = row.createCell(19);
                HSSFCell cell20 = row.createCell(20);
                cell0.setCellValue(null == yjCash.get("order_no") ? "" : yjCash.get("order_no").toString());
                cell1.setCellValue(null == yjCash.get("dea_name") ? "" : yjCash.get("dea_name").toString());
                cell2.setCellValue(null == yjCash.get("goods_name") ? "" : yjCash.get("goods_name").toString());
                cell3.setCellValue(null == yjCash.get("spec_sku_id") ? "" : sysAdminService.getSecValue(null == yjCash.get("spec_sku_id") ? "" : yjCash.get("spec_sku_id").toString()));
                cell4.setCellValue(null == yjCash.get("pf_price") ? "" : yjCash.get("pf_price").toString());
                cell5.setCellValue(null == yjCash.get("totalNum") ? "" : yjCash.get("totalNum").toString());
                cell6.setCellValue(null == yjCash.get("order_type") ? "" : yjCash.get("order_type").toString());
                if (null != yjCash.get("order_type")) {
                    //0-组合的订单1-精品区订单2-易货区订单
                    if (yjCash.get("order_type").toString().equals("0")) {
                        cell6.setCellValue("组合区订单");
                    } else if (yjCash.get("order_type").toString().equals("1")) {
                        cell6.setCellValue("精品区订单");
                    } else if (yjCash.get("order_type").toString().equals("2")) {
                        cell6.setCellValue("易货区订单");
                    } else {
                        cell6.setCellValue("");
                    }
                } else {
                    cell6.setCellValue("");
                }
                if (null != yjCash.get("pay_type")) {
                    //支付方式(10余额支付 20微信支付 30支付宝支付40易货币)
                    if (yjCash.get("pay_type").toString().equals("10")) {
                        cell7.setCellValue("余额支付");
                    } else if (yjCash.get("pay_type").toString().equals("20")) {
                        cell7.setCellValue("微信支付");
                    } else if (yjCash.get("pay_type").toString().equals("30")) {
                        cell7.setCellValue("支付宝支付");
                    } else if (yjCash.get("pay_type").toString().equals("40")) {
                        cell7.setCellValue("易货币");
                    } else {
                        cell7.setCellValue("");
                    }
                } else {
                    cell7.setCellValue("");
                }
                cell8.setCellValue(null == yjCash.get("pay_price") ? "" : yjCash.get("pay_price").toString());
                cell9.setCellValue(null == yjCash.get("ws_price") ? "" : yjCash.get("ws_price").toString());
                cell10.setCellValue(null == yjCash.get("express_price") ? "" : yjCash.get("express_price").toString());
                cell11.setCellValue(null == yjCash.get("createTime") ? "" : yjCash.get("createTime").toString());
                cell12.setCellValue(null == yjCash.get("buyerName") ? "" : yjCash.get("buyerName").toString());
                cell13.setCellValue(null == yjCash.get("buyer_remark") ? "" : yjCash.get("buyer_remark").toString());
                cell14.setCellValue(null == yjCash.get("shName") ? "" : yjCash.get("shName").toString());
                cell15.setCellValue(null == yjCash.get("shPhone") ? "" : yjCash.get("shPhone").toString());
                cell16.setCellValue(null == yjCash.get("shAddress") ? "" : yjCash.get("shAddress").toString());
                cell17.setCellValue(null == yjCash.get("express_company") ? "" : yjCash.get("express_company").toString());
                cell18.setCellValue(null == yjCash.get("express_no") ? "" : yjCash.get("express_no").toString());
                cell19.setCellValue(null == yjCash.get("pay_status") ? "" : yjCash.get("pay_status").toString());
                if (null != yjCash.get("pay_status")) {
                    //付款状态(10未付款 20已付款)
                    if (yjCash.get("pay_status").toString().equals("20")) {
                        cell19.setCellValue("已付款");
                    } else {
                        cell19.setCellValue("未付款");
                    }
                } else {
                    cell19.setCellValue("未付款");
                }
                cell20.setCellValue(null == yjCash.get("pay_time") ? "" : yjCash.get("pay_time").toString());

            }
        }


        /*HSSFSheet sheet1 = wb.createSheet("寄售订单记录表段");
        sheet1.setColumnWidth(0, 8000);
        sheet1.setColumnWidth(1, 9000);
        sheet1.setColumnWidth(8, 5000);
        sheet1.setColumnWidth(9, 5000);
        sheet1.setColumnWidth(10, 5000);
        sheet1.setColumnWidth(11, 5000);
        sheet1.setColumnWidth(12, 5000);
        sheet1.setColumnWidth(13, 9000);
        sheet1.setColumnWidth(17, 5000);
        HSSFRow row1 = sheet1.createRow((int) 0);
        HSSFCell cells = row1.createCell(0);
        cells.setCellValue("订单号");
        cells.setCellStyle(style);
        cells = row1.createCell(1);
        cells.setCellValue("商品名称");
        cells.setCellStyle(style);
        cells = row1.createCell(2);
        cells.setCellValue("商品规格");
        cells.setCellStyle(style);
        cells = row1.createCell(3);
        cells.setCellValue("批发单价");
        cells.setCellStyle(style);
        cells = row1.createCell(4);
        cells.setCellValue("数量");
        cells.setCellStyle(style);
        cells = row1.createCell(5);
        cells.setCellValue("支付方式");
        cells.setCellStyle(style);
        cells = row1.createCell(6);
        cells.setCellValue("付款金额");
        cells.setCellStyle(style);
        cells = row1.createCell(7);
        cells.setCellValue("运费金额");
        cells.setCellStyle(style);
        cells = row1.createCell(8);
        cells.setCellValue("下单时间");
        cells.setCellStyle(style);
        cells = row1.createCell(9);
        cells.setCellValue("买家");
        cells.setCellStyle(style);
        cells = row1.createCell(10);
        cells.setCellValue("买家留言");
        cells.setCellStyle(style);
        cells = row1.createCell(11);
        cells.setCellValue("收货人姓名");
        cells.setCellStyle(style);
        cells = row1.createCell(12);
        cells.setCellValue("联系电话");
        cells.setCellStyle(style);
        cells = row1.createCell(13);
        cells.setCellValue("收货人地址");
        cells.setCellStyle(style);
        cells = row1.createCell(14);
        cells.setCellValue("物流公司");
        cells.setCellStyle(style);
        cells = row1.createCell(15);
        cells.setCellValue("物流单号");
        cells.setCellStyle(style);
        cells = row1.createCell(16);
        cells.setCellValue("付款状态");
        cells.setCellStyle(style);
        cells = row1.createCell(17);
        cells.setCellValue("付款时间");
        cells.setCellStyle(style);
        // 从数据库获取结果
        List<Map<String, Object>> resultList1 = statisticalDao.jsListAuto(cashStatistical);
        // 循环将数据写入Excel
        if (null != resultList1 && resultList1.size()>0) {
            for (int i = 0; i < resultList1.size(); i++) {
                row1 = sheet1.createRow(i + 1);
                Map<String,Object> yjCash = resultList1.get(i);
                // 创建单元格，设置值
                HSSFCell cell0 = row1.createCell(0);
                HSSFCell cell1 = row1.createCell(1);
                HSSFCell cell2 = row1.createCell(2);
                HSSFCell cell3 = row1.createCell(3);
                HSSFCell cell4 = row1.createCell(4);
                HSSFCell cell5 = row1.createCell(5);
                HSSFCell cell6 = row1.createCell(6);
                HSSFCell cell7 = row1.createCell(7);
                HSSFCell cell8 = row1.createCell(8);
                HSSFCell cell9 = row1.createCell(9);
                HSSFCell cell10 = row1.createCell(10);
                HSSFCell cell11 = row1.createCell(11);
                HSSFCell cell12 = row1.createCell(12);
                HSSFCell cell13 = row1.createCell(13);
                HSSFCell cell14 = row1.createCell(14);
                HSSFCell cell15 = row1.createCell(15);
                HSSFCell cell16 = row1.createCell(16);
                HSSFCell cell17 = row1.createCell(17);
                cell0.setCellValue(null==yjCash.get("order_no")?"":yjCash.get("order_no").toString());
                cell1.setCellValue(null==yjCash.get("goods_name")?"":yjCash.get("goods_name").toString());
                cell2.setCellValue(null==yjCash.get("spec_sku_id")?"":sysAdminService.getSecValue(null == yjCash.get("spec_sku_id")?"":yjCash.get("spec_sku_id").toString()));
                cell3.setCellValue(null==yjCash.get("ws_price")?"":yjCash.get("ws_price").toString());
                cell4.setCellValue(null==yjCash.get("totalNum")?"":yjCash.get("totalNum").toString());
                if(null!=yjCash.get("pay_type")){
                    //支付方式(10余额支付 20微信支付 30支付宝支付40易货币)
                    if(yjCash.get("pay_type").toString().equals("10")){
                        cell5.setCellValue("余额支付");
                    }else if(yjCash.get("pay_type").toString().equals("20")){
                        cell5.setCellValue("微信支付");
                    }else if(yjCash.get("pay_type").toString().equals("30")){
                        cell5.setCellValue("支付宝支付");
                    }else if(yjCash.get("pay_type").toString().equals("40")){
                        cell5.setCellValue("易货币");
                    }else {
                        cell5.setCellValue("");
                    }
                }else{
                    cell5.setCellValue("");
                }
                cell6.setCellValue(null==yjCash.get("pay_price")?"":yjCash.get("pay_price").toString());
                cell7.setCellValue(null==yjCash.get("express_price")?"":yjCash.get("express_price").toString());
                cell8.setCellValue(null==yjCash.get("createTime")?"":yjCash.get("createTime").toString());
                cell9.setCellValue(null==yjCash.get("buyerName")?"":yjCash.get("buyerName").toString());
                cell10.setCellValue(null==yjCash.get("buyer_remark")?"":yjCash.get("buyer_remark").toString());
                cell11.setCellValue(null==yjCash.get("shName")?"":yjCash.get("shName").toString());
                cell12.setCellValue(null==yjCash.get("shPhone")?"":yjCash.get("shPhone").toString());
                cell13.setCellValue(null==yjCash.get("shAddress")?"":yjCash.get("shAddress").toString());
                cell14.setCellValue(null==yjCash.get("express_company")?"":yjCash.get("express_company").toString());
                cell15.setCellValue(null==yjCash.get("express_no")?"":yjCash.get("express_no").toString());
                cell16.setCellValue(null==yjCash.get("pay_status")?"":yjCash.get("pay_status").toString());
                if(null!=yjCash.get("pay_status")){
                    //付款状态(10未付款 20已付款)
                    if(yjCash.get("pay_status").toString().equals("20")){
                        cell16.setCellValue("已付款");
                    }else {
                        cell16.setCellValue("未付款");
                    }
                }else{
                    cell16.setCellValue("未付款");
                }
                cell17.setCellValue(null==yjCash.get("pay_time")?"":yjCash.get("pay_time").toString());
            }
        }*/
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = "批发订单记录表" + df.format(day);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String((fileName + ".xls").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        out.flush();
    }
}
