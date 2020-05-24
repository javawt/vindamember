package com.songlanyun.jymall.modules.business.jysys.notify.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.Notify;

import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/11/29
 * Time: 9:54
 */
public interface NotifyService extends BaseService<Notify, Integer> {
    void sendNotify(Notify notify);

    void send(Notify notify, List<Long> userIds);

    /** 保存消息 **/
    R saveMsg(int tempId, String replaceStr, long userId);
}
