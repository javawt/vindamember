package com.songlanyun.jymall.modules.business.jysys.notify.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang.
 * Date: 2019/11/29
 * Time: 9:43
 * <p>
 * 消息通知
 */
@Data
@Entity
@Table(name = "yj_notify")
public class Notify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notifyId;

    /**
     * 消息通知的用户ID
     */
    private String userId;

    /**
     * 是否已读
     */
    private String isRead="0";

    /**
     * 消息通知标题
     */
    private String title;

    /**
     * 消息内容
     */
    @Lob
    private String content;

    /**
     * 消息通知时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 消息通知类型
     */
    private Integer type;

    /**
     * 消息通知备注
     */
    private String remark;

    /**
     * 消息通知人，默认0为系统
     */
    private String originator = "0";
}
