package com.songlanyun.jymall.modules.business.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.common.utils.*;
import com.songlanyun.jymall.config.AlipayConfigs;
import com.songlanyun.jymall.config.OurWxPayConfig;
import com.songlanyun.jymall.config.WxPayConfig;
import com.songlanyun.jymall.modules.business.goods.entity.Activity;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsService;
import com.songlanyun.jymall.modules.business.jysys.address.entity.Address;
import com.songlanyun.jymall.modules.business.jysys.address.service.AddressService;
import com.songlanyun.jymall.modules.business.jysys.dict.entity.YjDictEntity;
import com.songlanyun.jymall.modules.business.jysys.dict.service.YjDictService;
import com.songlanyun.jymall.modules.business.order.entity.Order;
import com.songlanyun.jymall.modules.business.order.entity.OrderAddress;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import com.songlanyun.jymall.modules.business.order.model.OrderModel;
import com.songlanyun.jymall.modules.business.order.model.PayInfo;
import com.songlanyun.jymall.modules.business.order.model.PayParam;
import com.songlanyun.jymall.modules.business.order.model.RefundModel;
import com.songlanyun.jymall.modules.business.order.repository.OrderRepository;
import com.songlanyun.jymall.modules.business.order.service.OrderAddressService;
import com.songlanyun.jymall.modules.business.order.service.OrderGoodsService;
import com.songlanyun.jymall.modules.business.order.service.OrderService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.service.SysAdminService;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:35
 */
@Service
public class OrderServiceImpl extends BaseServiceImpl<Order, String, OrderRepository> implements OrderService {
    public OrderServiceImpl(OrderRepository repository) {
        super(repository);
    }
    @Resource
    private SysAdminDao sysAdminDao;

    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private OrderGoodsService orderGoodsService;


    //订单服务尖
    @Autowired
    private OrderService orderService;

    @Autowired
    private SysUserService userService;
    @Resource
    private SysAdminService sysAdminService;
    @Autowired
    private AlipayConfigs alipayConfigs;
    @Autowired
    private WxPayConfig wxPayConfig;
    @Autowired
    private YjDictService dictService;

    @Autowired
    private YjGoodsService goodsService;


    @Autowired
    private OurWxPayConfig ourWxPayConfig;

    /**
     * 结算收益
     * @param orderId
     */
    @Override
    @Transactional
    public void confirmEarnings(String orderId) {

    }

    @Override
    public int getCompleOrderCount() {
        return this.repository.getCompleOrderCount();
    }

    @Override
    public int getCompleOrderCounts() {
        return this.repository.getCompleOrderCounts();
    }


    @Override
    @Transactional
    public Order retail(String id, String AgentId, YjGoodsSku goodsSku, OrderModel orderModel, int type) {
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
        if (type == 0) {
            //total = goodsSku.getWsPrice().multiply(new BigDecimal(2));//批发数量2+普通数量1 固定不变
            //total = total.add(goodsSku.getRetailPrice());
        } else if (type == 2) {
            //total = goodsSku.getBarterPrice().multiply(BigDecimal.valueOf((long) orderModel.getNum()));

        } else {
            //设置总价 精品goods price 批发 ws 零售 retail
            //total = goodsSku.getGoodsPrice().multiply(BigDecimal.valueOf((long) orderModel.getNum()));
        }
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
        //orderService.save(order);
        this.save(order);
        return order;
    }

    @Override
    @Transactional
    public void saveOrderAddress(Integer addressId,String id) {
        Address address = addressService.findById(addressId);
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
    }

    @Override
    @Transactional
    public void saveOrderGoods(OrderGoods orderGoods) {
        orderGoodsService.save(orderGoods);
    }

    @Override
    public R cartpay(Map<String, Object> params, List<Order> orderList) throws Exception {
        //判断参数是否为空
        String orderNo = (String) params.get("orderNo");//支付宝回调订单号
        //支付方式//10余额支付 20微信支付 30 支付宝支付 40易货币
        Integer payType = (Integer) params.get("payType");

        if (StrUtil.isEmpty(orderNo) || payType == null) {
            return R.error("订单号/支付类型不能为空");
        }
        //订单不存在
        if (orderList == null) {
            return R.error("订单号/订单信息不存在");
        }
        //订单已完成
        if (orderList.get(0).getPayStatus() == 20) {
            return R.error("订单已支付");
        }
        return cartpay(payType, String.valueOf(params.get("password")), orderList);
    }

    /**
     * 订单支付
     *
     * @param params
     * @return
     */
    @Override
    public R pay(Map<String, Object> params, Order order) throws Exception {
        //判断参数是否为空
        String orderNo = (String) params.get("orderNo");
        //支付方式 //10余额支付 20微信支付 30 支付宝支付 40易货币
        Integer payType = (Integer) params.get("payType");
        if (StrUtil.isEmpty(orderNo) || payType == null) {
            return R.error("订单号/支付类型不能为空");
        }
        //获取正在操作的订单 --订单支付表
        //订单不存在
        if (order == null) {
            return R.error("订单号/订单信息不存在");
        }
        //订单已完成
        if (order.getPayStatus() == 20) {
            return R.error("订单已支付");
        }

        return pay(payType, order.getPayPrice(), orderNo, String.valueOf(params.get("password")), order.getOrderType());
    }

    /**
     * 订单支付
     */
    @Override
    //@Transactional
    public R pay(Integer payType, BigDecimal payPrice, String orderPayNo, String password, int orderType) throws Exception {
        //支付宝需要的预付订单号，必须与order的orderNo 配套
        Order order = orderService.findById(orderPayNo);

        //支付方式  10余额支付 20微信支付 30 -- 支付宝支付
        if (payType.equals(30)) {
            PayInfo payInfo = new PayInfo();
            payInfo.setMoney(order.getPayPrice().toString());
            payInfo.setOutTime("30m");
            payInfo.setOrderNo(orderPayNo);
            payInfo.setNotifyUrl(alipayConfigs.alipay_callback);
            System.out.println("回调地址******" + alipayConfigs.alipay_callback);
            return aliPay(payInfo);
        } else if (payType.equals(20)) {
            //微信支付
            WXPayInfo payInfo = new WXPayInfo();
            //微信支付金额处理
            payInfo.setMoney(order.getPayPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            //payInfo.setMoney("1");
            payInfo.setOutTime("30m");
            payInfo.setOrderNo(orderPayNo);
            //payInfo.setNotifyUrl(alipayConfigs.alipay_callback);
            return wxPay(payInfo);
        } else if (payType.equals(10)) {
            PayInfo payInfo = new PayInfo();
            payInfo.setMoney(payPrice.toString());
            payInfo.setPassword(password);
            payInfo.setOrderNo(orderPayNo);
            //return walletPay(payInfo);
            return null;
        } else if (payType.equals(40)) {
            //易货币支付
            PayInfo payInfo = new PayInfo();
            payInfo.setMoney(payPrice.toString());
            payInfo.setPassword(password);
            payInfo.setOrderNo(orderPayNo);
            //return walletPays(payInfo);
            return null;
        } else {
            return R.error("暂不支持此支付方式");
        }
    }

    /**
     * 购物车订单支付
     */
    @Override
    public R cartpay(Integer payType, String password, List<Order> orderList) throws Exception {
        BigDecimal moneypay = new BigDecimal(0);
        for (Order order : orderList) {
            moneypay = moneypay.add(order.getPayPrice());
            //支付宝需要的预付订单号，必须与order的orderNo 配套
            //根据订单表设置订单支付表数据
        }
        //支付方式  10余额支付 20微信支付 30 -- 支付宝支付
        if (payType.equals(30)) {
            PayInfo payInfo = new PayInfo();
            payInfo.setMoney(moneypay.toString());
            payInfo.setOutTime("30m");
            payInfo.setOrderNo(orderList.get(0).getCallbackNo());
            payInfo.setNotifyUrl(alipayConfigs.alipay_callback);
            return aliPay(payInfo);
        } else if (payType.equals(20)) {
            //微信支付
            WXPayInfo payInfo = new WXPayInfo();
            //微信支付金额处理
            payInfo.setMoney(moneypay.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
            //payInfo.setMoney("1");
            payInfo.setOutTime("30m");
            payInfo.setOrderNo(orderList.get(0).getCallbackNo());
            return wxPay(payInfo);
        } else if (payType.equals(10)) {
            PayInfo payInfo = new PayInfo();
            payInfo.setMoney(moneypay.toString());
            payInfo.setPassword(password);
            payInfo.setOrderNo(orderList.get(0).getCallbackNo());
            //return walletPay(payInfo);
            return null;
        } else {
            return R.error("暂不支持此支付方式");
        }
    }


    /**
     * 阿里支付
     **/
    public R aliPay(PayInfo payInfo) {
        PayParam wxpayParam = new PayParam();
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfigs.gateway_url, alipayConfigs.appid, alipayConfigs.rsa_private_key, alipayConfigs.format, alipayConfigs.charset, alipayConfigs.alipay_public_key, alipayConfigs.signtype);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(wxpayParam.getBody());
        model.setSubject("喜来购商城");
        model.setOutTradeNo(payInfo.getOrderNo());
        model.setTimeoutExpress(payInfo.getOutTime());
        model.setTotalAmount(payInfo.getMoney());
        //model.setTotalAmount("0.01");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(payInfo.getNotifyUrl());

        //这里和普通的接口调用不同，使用的是sdkExecute
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            return R.ok().put("payInfo", response.getBody());
        } catch (AlipayApiException e) {
            //得到待支付订单，将其设为支付失败
           /* Order order = orderService.findById(payInfo.getOrderNo());
            order.setPayStatus(30);*/
            e.printStackTrace();
            return R.error("支付异常！");
        }

    }

    /**
     * 微信支付
     **/
    public R wxPay(WXPayInfo payInfo) throws Exception {
        // 生成微信「统一下单」请求数据
        Map<String, String> dataMap = new HashMap<>(16);
        dataMap.put("appid", ourWxPayConfig.getAppID());
        dataMap.put("mch_id", ourWxPayConfig.getMchID());
        dataMap.put("nonce_str", UUIDUtil.getUUID());
        dataMap.put("body", wxPayConfig.getBody());
        dataMap.put("out_trade_no", payInfo.getOrderNo());
        dataMap.put("total_fee", payInfo.getMoney());
        //20200413
        //dataMap.put("total_fee", "1");
        dataMap.put("spbill_create_ip", HttpClientUtil.localIp());
        dataMap.put("notify_url", payInfo.getNotifyUrl());
        dataMap.put("trade_type", wxPayConfig.getTradeTypeApp());
        dataMap.put("attach", payInfo.getNotifyUrl());

        try {


            // 签名,请求「统一下单」接口，并解析返回结果
            Map<String, String> responseMap = signAndGetResponse(dataMap, wxPayConfig.getKey(),
                    wxPayConfig.getFieldSign(), wxPayConfig.getUnifiedOrderUrl());
            if (responseMap == null || responseMap.isEmpty()) {
                return null;
            }
            // 生成客户端「调起支付接口」的请求参数
            Map<String, String> resultMap = getAppPayOrder(responseMap, wxPayConfig.getKey(),
                    wxPayConfig.getFieldSign(), wxPayConfig.getPackageApp());
            // 添加预支付订单创建成功标识
            resultMap.put("pre_pay_order_status", wxPayConfig.getResponseSuccess());
            return R.ok().put("payInfo", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("支付异常！");
        }

    }

    /**
     * 生成签名,调用微信「统一下单」接口，并解析接口返回结果
     *
     * @param dataMap   请求参数
     * @param key       app Key
     * @param signField 签名字段
     * @param apiUrl    微信接口地址
     * @return
     */
    private static Map<String, String> signAndGetResponse(Map<String, String> dataMap, String key,
                                                          String signField, String apiUrl) throws Exception {
        // 生成签名 MD5 编码

        String md5Sign = WxPaySignatureUtils.signature(dataMap, key);


        String md5Sign2 = SignUtil.getMD5Sign(dataMap, key, signField);


        dataMap.put("sign", md5Sign);
        // 发送请求数据
        String respXml = HttpClientUtil.requestWithoutCert(apiUrl, dataMap,
                5000, 10000);
        // 解析请求数据
        dataMap.clear();
        dataMap = processResponseXml(respXml, key, signField);
        // 没有生成预支付订单,返回空
        if (StringUtils.isEmpty(dataMap.get("prepay_id"))) {
            return null;
        }
        return dataMap;
    }

    private static Map<String, String> processResponseXml(String xmlStr, String key, String signField)
            throws Exception {
        String returnCodeField = "return_code";
        String resultFail = "FAIL";
        String resultSuccess = "SUCCESS";
        String returnCode;
        Map<String, String> respData = MapUtil.xml2Map(xmlStr);
        if (respData.containsKey(returnCodeField)) {
            returnCode = respData.get(returnCodeField);
        } else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }
        if (returnCode.equalsIgnoreCase(resultFail)) {
            return respData;
        } else if (returnCode.equalsIgnoreCase(resultSuccess)) {
            /**
             * 签名校验
             */
            if (SignUtil.signValidate(respData, key, signField)) {
                return respData;
            } else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", returnCode, xmlStr));
        }
    }

    private static Map<String, String> getAppPayOrder(Map<String, String> data, String key, String fieldSign,
                                                      String packageStr) throws Exception {
        Map<String, String> resultMap = new HashMap<>(16);
        resultMap.put("appid", data.get("appid"));
        resultMap.put("partnerid", data.get("mch_id"));
        resultMap.put("prepayid", data.get("prepay_id"));
        resultMap.put("package", packageStr);
        resultMap.put("noncestr", UUIDUtil.getUUID());
        resultMap.put("timestamp", DateUtil.getTimeStampSecond());

        // 生成签名
        String sign = SignUtil.getMD5Sign(resultMap, key, fieldSign);
        resultMap.put("sign", sign);

        return resultMap;
    }

    /**
     * 支付宝支付回调订单的业务判断
     *
     * @param params
     * @throws AlipayApiException
     */
    @Transactional
    @Override
    public void check(Map<String, String> params) throws AlipayApiException {
        String outTradeNo = params.get("out_trade_no");//本地订单
        String trade_no = params.get("trade_no");//支付宝生成订单
        //验证支付订单的app_id是否一致
        if (!params.get("app_id").equals(alipayConfigs.appid)) {
            throw new AlipayApiException("app_id不一致");
        }

        //获取正在操作的订单 --订单支付表
        // 1、商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        Map<String, Object> parMap = new HashMap<>();
        parMap.put("callbackNo", outTradeNo);
        List<Order> orderList = orderService.findAll(parMap);
        //YjOrderRecharge orderPay = orderRechargeService.findByOrderNo(outTradeNo);
        if (orderList == null) {
            throw new AlipayApiException("out_trade_no错误！");
        }
        //订单支付成功

    }

    /**
     * 钱包支付
     */
   /* public R walletPay(PayInfo payInfo) {
        SysUserLoginInfo user = userService.getUserById(ShiroUtils.getUserId());
        if (StringUtils.isBlank(user.getPayPsw())) {

            return R.error(211, "未设置支付密码，请先设置");
        }
        if (payInfo.getPassword() == null || "null".equals(payInfo.getPassword())) {
            return R.error("请输入支付密码");
        }

        if (!(user.getPayPsw()).equals(new Sha256Hash(payInfo.getPassword(), user.getSalt()).toHex())) {
            return R.error("支付密码错误");
        }

        BigDecimal payMoney = new BigDecimal(payInfo.getMoney()),
                balance = user.getBalance().subtract(payMoney);

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return R.error("账户余额不足");
        }

        //用户当前可用余额
        user.setBalance(balance);

        //用户消费金额
        user.setPayMoney(user.getPayMoney().add(payMoney));

        //用户实际支付金额
        user.setExpendMoney(user.getExpendMoney().add(payMoney));

        user.setPayPsw(null);
        user.setPassword(null);
        userService.update(user);


        //tsh 钱包支付增加购物批发券 2019.12.21
        //OrderWholesale orderWholesale = orderWholesaleService.findById(payInfo.getOrderNo());
        Map<String, Object> whoMap = new HashMap<>();
        whoMap.put("callbackNo", payInfo.getOrderNo());
        List<Order> orderServiceList = orderService.findAll(whoMap);
        if (null != orderServiceList && orderServiceList.size() > 0) {
            for (Order order : orderServiceList) {


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
                YjGoods goods = null;
                List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo());
                for (OrderGoods orderGood : orderGoods) {
                    YjGoods yjGoods = goodsService.findById(orderGood.getGoodsId());
                    //yjGoods.setSalesActual(yjGoods.getSalesActual() + orderGood.getTotalNum());
                    goodsService.save(yjGoods);

                    if (goods == null) {
                        goods = yjGoods;
                    }
                }

                //付款成功通知

                OrderGoods orderGoods1 = orderGoodsService.findAllByOrderNo(order.getOrderNo()).get(0);
                sysAdminService.sendMsg(16, orderGoods1.getGoodsName(), order.getUserId());

                //支付订单汇总
                PaymentSummary paymentSummary = new PaymentSummary();
                paymentSummary.setUserId(user.getUserId());
                paymentSummary.setRelationId(0L);
                paymentSummary.setPaymentType(1);//0收入 1支出
                paymentSummary.setOrderNo(payInfo.getOrderNo());
                paymentSummary.setOperaType(2);//1寄售2订单支付3推荐奖4分红5红包6转账7提现8退款处理9升级代理商
                paymentSummary.setMoney(payMoney);
                paymentSummary.setBeforeMoney(user.getBalance().add(payMoney));
                paymentSummary.setAfterMoney(user.getBalance());
                paymentSummary.setPayType(1);//1余额2微信3支付宝4易货币
                paymentSummary.setRemark("订单号:" + paymentSummary.getOrderNo() + ",余额支付成功！消费金额:" + paymentSummary.getMoney() + "元,剩余余额:" + paymentSummary.getAfterMoney() + "元");
                sysAdminDao.savePaymentSummary(paymentSummary);
            }


        }

        return R.ok("支付成功");
    }*/


    /**
     * 易货币支付
     */
    /*public R walletPays(PayInfo payInfo) {
        SysUserEntity user = userService.getUserById(ShiroUtils.getUserId());

        BigDecimal payMoney = new BigDecimal(payInfo.getMoney()),
                balance = user.getEasyMoney().subtract(payMoney);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            //删除订单
            orderService.deleteById(payInfo.getOrderNo());
            sysAdminDao.deleteOrderGoods(payInfo.getOrderNo());
            return R.error("账户易货币不足");
        }
        //用户当前可用余额
        user.setEasyMoney(balance);
        //用户消费金额
        //user.setPayMoney(user.getPayMoney().add(payMoney));
        //用户实际支付金额
        //user.setExpendMoney(user.getExpendMoney().add(payMoney));
        user.setPayPsw(null);
        user.setPassword(null);
        userService.update(user);
        Order order = orderService.findById(payInfo.getOrderNo());
        if (null != order) {
            order.setPayStatus(20);
            order.setOrderStatus(Order.xlgOrderStatus.CANCEL);
            order.setUpdateTime(new Date());
            order.setPayType(40);
            order.setPayTime(new Date());
            orderService.save(order);
            //增加商品销量
            YjGoods goods = null;
            List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo());
            for (OrderGoods orderGood : orderGoods) {
                YjGoods yjGoods = goodsService.findById(orderGood.getGoodsId());
                //yjGoods.setSalesActual(yjGoods.getSalesActual() + orderGood.getTotalNum());
                goodsService.save(yjGoods);

                if (goods == null) {
                    goods = yjGoods;
                }
            }
        }
        //付款成功通知
        OrderGoods orderGoods1 = orderGoodsService.findAllByOrderNo(payInfo.getOrderNo()).get(0);
        sysAdminService.sendMsg(16, orderGoods1.getGoodsName(), order.getUserId());
        return R.ok("兑换成功");
    }*/

    /**
     * 获取商品需要的批发券数量
     */
    /*private int getCouponNum(Integer needCoupon, Integer totalNum, BigDecimal payPrice) {
        int couponNum = 0;
        if (needCoupon == null || needCoupon == 0) {
            List<YjDictEntity> dictEntities = dictService.getDictListByType(10);
            if (dictEntities.size() > 0) {
                couponNum = payPrice.multiply(BigDecimal.valueOf(Double.parseDouble(dictEntities.get(0).getDvalue()))).setScale(0, BigDecimal.ROUND_DOWN).intValue();
            }
        }
        if (couponNum == 0) {
            couponNum = totalNum * (needCoupon == null ? 0 : needCoupon);
        }
        return couponNum;
    }*/
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
     * 支付宝退款
     */
   /* @Override
    public R refundOrder(RefundModel refundModel) {
        if (refundModel.getPayType() == 30) {
            return aliRefund(refundModel);
        } else if (refundModel.getPayType() == 10) {
            return walletRefund(refundModel);
        } else {
            return R.error("该订单支付状态异常");
        }
    }*/

    /**
     * 支付宝退款
     *
     * @param refundModel
     * @return
     */
    private R aliRefund(RefundModel refundModel) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfigs.gateway_url, alipayConfigs.appid, alipayConfigs.rsa_private_key, alipayConfigs.format, alipayConfigs.charset, alipayConfigs.alipay_public_key, alipayConfigs.signtype);
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeRefundModel alipayTradeRefundModel = new AlipayTradeRefundModel();
        alipayTradeRefundModel.setTradeNo(refundModel.getTradeNo());
        alipayTradeRefundModel.setRefundAmount(refundModel.getRefundAmount().toString());
        alipayTradeRefundModel.setRefundReason(refundModel.getRefundReason());
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(alipayTradeRefundModel);
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            System.out.println(response.getMsg() + "\n");
            System.out.println(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("退款失败");
        }
        return R.ok("退款成功");
    }

    /**
     * 钱包退款
     */
   /* private R walletRefund(RefundModel refundModel) {
        Order order = orderService.findById(refundModel.getOrderNo());
        if (order == null) {
            return R.error("无效的订单");
        }

        SysUserEntity user = userService.getUserById(order.getUserId());

        //用户当前可用余额
        user.setBalance(user.getBalance().add(refundModel.getRefundAmount()));

        //用户消费金额
        user.setPayMoney(user.getPayMoney().subtract(refundModel.getRefundAmount()));

        //用户实际支付金额
//        user.setExpendMoney(user.getExpendMoney().add(payMoney));

        user.setPayPsw(null);
        user.setPassword(null);
        userService.update(user);

        return R.ok("退款成功");
    }*/


}
