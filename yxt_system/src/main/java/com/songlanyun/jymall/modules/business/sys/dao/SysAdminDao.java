package com.songlanyun.jymall.modules.business.sys.dao;

import com.songlanyun.jymall.modules.business.goods.entity.*;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.Notify;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import com.songlanyun.jymall.modules.business.order.entity.*;
import com.songlanyun.jymall.modules.business.score.entity.CashStatistical;
import com.songlanyun.jymall.modules.business.sys.entity.PaymentSummary;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntity;
import com.songlanyun.jymall.modules.business.sys.entity.UserUpgradeRecode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 */
@Mapper
public interface SysAdminDao {

    List<YjGoods> getGoodsByActId(Integer activityId);

    void deleteActGoods(Integer goodsId);

    List<Order> getIncreaOrderList();

    Order getOrderInfo(String orderNo);

    void updateIncreaOrderList(Order order);

    List<YjGoodsSku> getGoodsSkuByGoodsId(int goodsId);

    Integer getServiceConsig(String orderNo);

    String getDictValue(Integer dtype);

    NotifyTemp getTempModel(int tempId);

    void saveAdviceNotify(Notify notify);

    void updateActivityRecommend(Integer id);

    void updateActivityRecommends();

    void updateReexpressNo(Map map);

    List<Activity> getActivitySelectList();

    Activity getActivitySelect(String id);

    void updateRecharge(String orderNo);

    List<SysUserEntity> getAllUsers();

    List<Long> getDirectUser(Long userId, Integer gradeId);

    Double getBoutiqueMoney(@Param("ids") List<Long> ids);

    void updateBoutiqueMoney(@Param("ids") List<Long> ids);

    void updateRetailMoney(@Param("ids") List<Long> ids);

    Double getRetailMoney(@Param("ids") List<Long> ids);

    Double getActivityTotal(Integer activityId);

    Double getGoodsRetailPrice(String orderNo);

    Integer getGoodsIdByOrderNo(String orderNo);

    Activity getNewActivity();

    void updateEasyStatus(String orderNo);

    void updateUserEasyMoney(SysUserEntity sysUserEntity);

    void updateOrderStatus(Integer orderStatu, String orderNo);

    void updateWholesaleStatus(Map<String, Object> parMap);

    int getOrderGoodsByOrderNo(String orderNo);

    Order isExitShareOrder(String orderNo);

    void updateOrderRedPack(String orderNo);

    void updateUserGrade(@Param("grade") Integer grade, @Param("userId") Long userId);

    SysUserEntity getUserAgentByUid(Long UserId);

    void savePaymentSummary(PaymentSummary paymentSummary);

    Long getUserIdByMobile(String mobile);


    int getUserIdByUserName(String mobile);

    void deleteNotifyById(Integer id);

    void saveUserUpgrade(UserUpgradeRecode userUpgradeRecode);

    List<UserUpgradeRecode> personUpgrade(String userId);

    List<YjCategory> getYjCategory();

    int getGoodsNo(YjGoodsSku yjGoodsSku);

    Cart getSameCart(Cart cart);

    void clearAgentId(Integer id);

    String getGoodSpecName(int goodsId);

    String getSpecName(Integer specId);

    List<Integer> getCategorySubId(Integer categoryId);

    void updateCategoryStatus(Integer id);

    int getSubGoods(@Param("ids") List<Integer> ids);

    Double getUpAgentMoney();

    void updateActivityComple(Integer id);

    String getUserNameById(String id);

    String getUserNameByIds(Integer id);

    Integer getDealerIdByGoodsId(Integer goodsId);

    void deleteOrderGoods(String orderNo);

    int getOrderNum(Map<String, Object> parMap);
    int getOrderNums(Map<String, Object> parMap);

    Double getCashTotal(CashStatistical cashStatistical);

    Double getRefuseTotal(String orderNo);
    Double getWholesaleTotal(String orderNo);

    List<Integer> getSubUserId(Integer id);

    Map<String,Object> getUserInfo(Integer id);

    Double getUseryj(Integer id);

    int getCountUserByUsername(String username);

    int getCountUserByMobile(String mobile);
}
