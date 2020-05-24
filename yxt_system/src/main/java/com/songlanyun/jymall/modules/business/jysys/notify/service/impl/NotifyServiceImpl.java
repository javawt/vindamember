package com.songlanyun.jymall.modules.business.jysys.notify.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.common.exception.RRException;
import com.songlanyun.jymall.common.utils.R;
import com.songlanyun.jymall.common.utils.ShiroUtils;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.Notify;
import com.songlanyun.jymall.modules.business.jysys.notify.entity.NotifyTemp;
import com.songlanyun.jymall.modules.business.jysys.notify.model.NotifyModel;
import com.songlanyun.jymall.modules.business.jysys.notify.repository.NotifyRepository;
import com.songlanyun.jymall.modules.business.jysys.notify.service.NotifyService;
import com.songlanyun.jymall.modules.business.jysys.notify.service.NotifyTempService;
import com.songlanyun.jymall.modules.business.sys.entity.SysUserEntityDTO;
import com.songlanyun.jymall.modules.business.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

/**
 * Created by zenghang.
 * Date: 2019/11/29
 * Time: 9:55
 */
@Service
public class NotifyServiceImpl extends BaseServiceImpl<Notify, Integer, NotifyRepository> implements NotifyService {
    public NotifyServiceImpl(NotifyRepository repository) {
        super(repository);
    }

    @Autowired
    private SysUserService userService;

    @Autowired
    private NotifyTempService notifyTempService;

    @Override
    public void sendNotify(Notify notify) {
        SysUserEntityDTO user = ShiroUtils.getUserEntity();
        notify.setOriginator(user == null ? "0": user.getUserId());
        this.save(notify);
    }

    @Async
    @Override
    public void send(Notify notify, List<Long> userIds) {
       /* if (userIds == null || userIds.size() == 0) {
            userIds = userService.list().stream().map(SysUserEntity::getUserId).collect(Collectors.toList());
        }

        for (Long userId : userIds) {
            notify.setUserId(userId);
            notify.setNotifyId(null);
            this.sendNotify(notify);
        }*/
    }
    @Override
    /** 保存消息 **/
    public R saveMsg(int tempId, String replaceStr, long userId){
        NotifyModel notifyModel = new NotifyModel();
        notifyModel.setNotifyTempId(tempId);
        List<String> contents = Arrays.asList(replaceStr.split(",") );
        //有多少个占位符就add多少个，超过的无用，少了自动使用空字符串替换
        notifyModel.setContents(contents);

        //String realContent=String.format(notifyModel.getContent(), contents);


        List<Long> userIds = new ArrayList<>();
        userIds.add(userId);

        //需要发送多少个用户就add多少个
        notifyModel.setUserIds(userIds);

        //如果全部推送可以传null,或者空的list
        notifyModel.setUserIds(new ArrayList<>());
        return sendNotify(notifyModel);
    }

    public R sendNotify(@RequestBody NotifyModel notifyModel) {
        if (notifyModel.isTemp()) {
            Map<String, Object> param = new HashMap<>();
            param.put("isUse", true);
            if (notifyModel.getNotifyTempId() == null) {
                param.put("id", notifyModel.getNotifyTempId());
            } else {
                param.put("used", notifyModel.getType());
            }
            NotifyTemp notifyTemp = notifyTempService.findOne(param);

            if (notifyTemp == null) {
                throw new RRException("没有找到指定的模板");
            }

            sendNotify(notifyTemp, notifyModel);

            return R.ok("消息保存成功");
        }

        sendNotifyNoTemp(notifyModel);

        return R.ok("消息保存成功");
    }


    public void sendNotifyNoTemp(NotifyModel notifyModel) {
        if (notifyModel.getContent() == null || notifyModel.getContent().length() == 0) {
            throw new RRException("请输入消息内容");
        }

        Notify notify = new Notify();
        notify.setTitle(notifyModel.getTitle());
        notify.setContent(notifyModel.getContent());
        notify.setType(notifyModel.getType());
        notify.setRemark(notifyModel.getRemark());
        List<Long> userIds=notifyModel.getUserIds();
        /*if (userIds == null || userIds.size() == 0) {
            userIds = userService.list().stream().map(SysUserEntity::getUserId).collect(Collectors.toList());
        }

        for (Long userId : userIds) {
            notify.setUserId(userId);
            notify.setNotifyId(null);
            this.sendNotify(notify);
        }*/
    }

    public void sendNotify(NotifyTemp notifyTemp, NotifyModel notifyModel) {
        if (notifyModel.getContents() == null || notifyModel.getContents().size() == 0) {
            throw new RRException("请输入消息内容");
        }

        int loopContent = notifyTemp.getNum() - notifyModel.getContents().size();
        if (loopContent > 0) {
            for (int i = 0; i < loopContent; i++) {
                notifyModel.getContents().add("");
            }
        }

        Notify notify = new Notify();
        notify.setTitle(notifyTemp.getTitle());

        String[] con = new String[notifyModel.getContents().size()];
        for (int i = 0; i < con.length; i++) {
            con[i] = notifyModel.getContents().get(i);
        }
        notify.setContent(String.format(notifyTemp.getContent(), con));

        notify.setType(notifyTemp.getUsed());
        notify.setRemark(notifyModel.getRemark());
        List<Long> userIds=notifyModel.getUserIds();
        /*if (userIds == null || userIds.size() == 0) {
            userIds = userService.list().stream().map(SysUserEntity::getUserId).collect(Collectors.toList());
        }

        for (Long userId : userIds) {
            notify.setUserId(userId);
            notify.setNotifyId(null);
            this.sendNotify(notify);
        }*/
    }

}
