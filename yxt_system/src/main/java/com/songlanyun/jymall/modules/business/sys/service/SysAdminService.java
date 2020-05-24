package com.songlanyun.jymall.modules.business.sys.service;

import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.sys.entity.Statistical;
import org.springframework.scheduling.annotation.Async;

public interface SysAdminService {

    R getGoodsByActId(Statistical statistical);

    R appReturnGoods(Statistical statistical);

    R activitySelectList();

    R personUpgrade(String userId);

    R deleteActGoods(Integer[] goods);

    void increateStock();

    @Async
    String saveMsg(int tempId, String userId, Object... args);

    R sendMsg(int tempId, String goodsName, String userId);

    void updateActivityRecommend(Integer id);

    void updateActivityRecommends();

    String getSecValue(String specValue);
}
