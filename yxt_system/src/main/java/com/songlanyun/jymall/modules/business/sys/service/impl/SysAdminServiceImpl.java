package com.songlanyun.jymall.modules.business.sys.service.impl;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.goods.entity.Activity;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoods;
import com.songlanyun.jymall.modules.business.goods.entity.YjGoodsSku;
import com.songlanyun.jymall.modules.business.goods.entity.YjSpecValue;
import com.songlanyun.jymall.modules.business.goods.service.YjGoodsSkuService;
import com.songlanyun.jymall.modules.business.goods.service.YjSpecValueService;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.Notify;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import com.songlanyun.jymall.modules.business.order.entity.Order;
import com.songlanyun.jymall.modules.business.order.entity.OrderGoods;
import com.songlanyun.jymall.modules.business.order.service.OrderGoodsService;
import com.songlanyun.jymall.modules.business.sys.dao.SysAdminDao;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import com.songlanyun.jymall.modules.business.sys.service.SysAdminService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysAdminServiceImpl implements SysAdminService {

    @Resource
    private SysAdminDao sysAdminDao;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private YjGoodsSkuService goodsSkuService;
    @Autowired
    private YjSpecValueService yjSpecValueService;

    SimpleDateFormat formatter = new SimpleDateFormat("MMdd");

    @Override
    public R getGoodsByActId(Statistical statistical) {
        //入参校验
        if(statistical.getPage()>0){
        }else{
            return R.error("页码参数输入错误");
        }
        if(statistical.getSize()>=0){
        }else{
            return R.error("页数参数输入错误");
        }
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("page",statistical.getPage());
        resMap.put("size",statistical.getSize());
        List<YjGoods> resList=sysAdminDao.getGoodsByActId(statistical.getActivityId());
        if(null!=resList && resList.size()>0){
            resMap.put("total",resList.size());
            resList = resList.stream().skip((statistical.getPage() - 1) * statistical.getSize()).
                    limit(statistical.getSize()).collect(Collectors.toList());
        }else{
            resMap.put("total",0);
        }
        resMap.put("list",resList);
        return R.ok().put("data",resMap);
    }

    @Override
    public R appReturnGoods(Statistical statistical) {
        //查询订单信息
        Order order=sysAdminDao.getOrderInfo(statistical.getOrderNo());
        if (null == order) {
            return R.error("无效的订单");
        }
        if (order.getUserId() != ShiroUtils.getUserId()) {
            return R.error("订单与登录用户不匹配");
        }
        Map<String ,Object> parMap=new HashMap<>();
        parMap.put("orderNo",statistical.getOrderNo());
        parMap.put("reexpressNo",statistical.getExpressNo());
        sysAdminDao.updateReexpressNo(parMap);
        return R.ok();
    }

    @Override
    public R activitySelectList() {
        List<Activity>  activityList=  sysAdminDao.getActivitySelectList();
        if(null!=activityList && activityList.size()>0){
            activityList.forEach(temp -> {
                temp.setNameTime(temp.getName()+formatter.format(temp.getStartTime())+"-"+formatter.format(temp.getEndTime()));
            });
        }
        return R.ok().put("data",activityList);
    }
    @Override
    public R personUpgrade(String userId) {
        return R.ok().put("data",sysAdminDao.personUpgrade(userId));
    }


    @Override
    @Transactional
    public R deleteActGoods(Integer[] goods) {
        if (null != goods && goods.length > 0) {
            for (Integer s : goods) {
                sysAdminDao.deleteActGoods(s);
            }
        }
        return R.ok();
    }


    @Override
    @Transactional
    public void increateStock() {
        //未付款订单返库存,普通订单/批发订单1.订单状态不等70 2.付款状态未付款 3.是否已减 4.过期订单
        //更新状态
        List<Order> orderList= sysAdminDao.getIncreaOrderList();
        if(null!=orderList && orderList.size()>0){
            for(Order order:orderList){
                sysAdminDao.updateIncreaOrderList(order);//重新标识
                //加库存
                List<OrderGoods> orderGoods = orderGoodsService.findAllByOrderNo(order.getOrderNo());
                if (null != orderGoods && orderGoods.size() > 0) {
                    for (OrderGoods orderGood : orderGoods) {
                        YjGoodsSku goodsSku = goodsSkuService.findById(orderGood.getGoodsSkuId());
                        //goodsSku.setStockNum(goodsSku.getStockNum() + orderGood.getTotalNum());
                        goodsSkuService.save(goodsSku);
                    }
                }
            }
        }
    }


    /** 设置多个参数  **/
    @Async
    @Override
    public String saveMsg(int tempId,String userId,Object ...args){
        NotifyTemp notifyTemp= sysAdminDao.getTempModel(tempId);
        if(null ==notifyTemp)
            return "" ;
        Notify notify = new Notify();
        //判断userid是否为空
        if(userId==null){
            //没有用户赋值null
            notify.setUserId(null);
        }else{
            notify.setUserId(userId);
        }
        notify.setContent(String.format(notifyTemp.getContent(),args));
        notify.setTitle(notifyTemp.getTitle());
        notify.setType(10);//系统消息
        //保存消息通知
        sysAdminDao.saveAdviceNotify(notify);
        return notify.getContent();

    }
    /** 发送消息 **/
    @Override
    public R sendMsg(int tempId, String goodsName, String userId){
        //根据模板id获取模板信息
        NotifyTemp notifyTemp= sysAdminDao.getTempModel(tempId);
        if(null ==notifyTemp)return R.error("模板不存在");
        Notify notify = new Notify();
        //判断userid是否为空
        if(userId==null){
            //没有用户赋值null
            notify.setUserId(null);
        }else{
            notify.setUserId(userId);
        }
        notify.setContent(notifyTemp.getContent().replaceAll("%s",goodsName));
        notify.setTitle(notifyTemp.getTitle());
        notify.setType(10);//系统消息
        //保存消息通知
        sysAdminDao.saveAdviceNotify(notify);
        return R.ok();
    }

    @Override
    public void updateActivityRecommend(Integer id) {
        sysAdminDao.updateActivityRecommend(id);
    }
    @Override
    public void updateActivityRecommends() {
        sysAdminDao.updateActivityRecommends();
    }

    @Override
    public String getSecValue(String specValue){
        List<YjSpecValue> specValueList= yjSpecValueService.findAll();
        String temp ="";
        String vl = "";
        if(StringUtils.isNotBlank(specValue)){
            if (StringUtils.isNotBlank(specValue)) {
                temp = specValue.replaceAll("，", ",");
            }}
        if (StringUtils.isNotBlank(temp)) {

            List<String> result = Arrays.asList(temp.split(","));
            if (null != result && result.size() > 0) {
                for (String s : result) {
                    if (StringUtils.isNotBlank(vl)) {
                        String va="";
                        if(null != specValueList && specValueList.size()>0){
                            for(YjSpecValue yjSpecValue:specValueList){
                               /* if(yjSpecValue.getSpecValueId()==Integer.parseInt(s)){
                                    va=yjSpecValue.getSpecValue();
                                    break;
                                }*/
                            }
                        }
                        vl = vl + "," + va;
                    } else {
                        String va="";
                        if(null != specValueList && specValueList.size()>0){
                            for(YjSpecValue yjSpecValue:specValueList){
                               /* if(yjSpecValue.getSpecValueId()==Integer.parseInt(s)){
                                    va=yjSpecValue.getSpecValue();
                                    break;
                                }*/
                            }
                        }
                        vl = vl + va;
                    }
                }
            }

        }
        return vl;
    }

}
