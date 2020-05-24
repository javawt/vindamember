package com.songlanyun.jymall.modules.business.jysys.bannertext.entity;

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
@Table(name = "jy_banner_text")
public class BannerText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    /**
     * 链接类型
     */
    private Integer type;//1是固定显示，2、从右到左滚动


    /**
     * 概述
     */
    @Lob
    private String content;

    /**
     * 生成时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
