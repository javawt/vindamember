package com.songlanyun.jymall.modules.business.jysys.notify.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zenghang.
 * Date: 2019/12/17
 * Time: 10:41
 * <p>
 * 消息模板表
 */
@Data
@Entity
@Table(name = "jy_notify_temp")
public class NotifyTemp implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 模板标题
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 模板内容,站位内容使用  %s  占位
     */
    @Column(name = "content", nullable = false, length = 500)
    private String content;

    /**
     * 用途，对应消息通知的type
     */
    @Column(name = "used", nullable = false)
    private Integer used;

    /**
     * 创建人ID
     */
    @Column(name = "create_user", nullable = false)
    private String createUser;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime = new Date();

    /**
     * 是否启用
     */
    @Column(name = "is_use", nullable = false)
    private Boolean isUse = true;

    /**
     * 占位符数量
     */
    @Column(name = "num", nullable = false, length = 4)
    private Integer num;
}
