package com.songlanyun.jymall.modules.business.jysys.notify.model;

import com.songlanyun.jymall.common.exception.RRException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/12/17
 * Time: 15:18
 * <p>
 * 发送消息通知的模板
 */
@Data
public class NotifyModel {
    /**
     * 消息通知类型
     */
    private Integer type;

    /**
     * 消息模板ID，如果没有该属性则通过type选择模板
     */
    private Integer notifyTempId;

    /**
     * 消息通知标题
     * 使用模板发送消息时不需要该属性
     */
    private String title;

    /**
     * 消息通知内容
     * 使用模板发送消息时不需要该属性
     */
    private String content;

    /**
     * 消息通知内容，使用模板时
     */
    private List<String> contents;

    /**
     * 消息推送到那些用户，如果不给则全推
     */
    private List<Long> userIds = new ArrayList<>();

    /**
     * 消息通知备注
     */
    private String remark;

    public Boolean isTemp() {
        if (this.notifyTempId != null) return true;

        if (this.type == null) throw new RRException("缺少消息类型");

        if (this.content != null && this.title != null) return false;

        return true;
    }

}
