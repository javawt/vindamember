package com.songlanyun.jymall.modules.business.jysys.banner.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang on 2019/11/22.
 * <p>
 * 轮播图
 */
@Data
@Entity
@Table(name = "jy_banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 轮播图位置
     */
    private String location;

    /**
     * 图片ID,对应图片集管理中的ID
     */
    private Integer picture;

    /**
     * 链接类型
     */
    private Integer type;//链接类型(0内部链接1外部链接)

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 概述
     */
    @Lob
    private String content;

    /**
     * 生成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date ctime;

    /**
     * 状态：0，正常；1，删除
     */
    private Boolean isDelete;

    /**
     * 图片地址
     */
    private String imgPath;

    private String bannerName;//广告名称
}
