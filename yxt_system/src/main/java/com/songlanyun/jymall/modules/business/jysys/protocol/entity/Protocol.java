package com.songlanyun.jymall.modules.business.jysys.protocol.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang on 2019/11/22.
 * <p>
 * 协议
 */
@Data
@Entity
@Table(name = "jy_protocol")
public class Protocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 协议所属类型
     */
    private String type;

    /**
     * 名称
     */
    private String name;

    /**
     * 简介
     */
    @Lob
    private String intro;

    /**
     * 内容
     */
    @Lob
    private String content;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;
}
