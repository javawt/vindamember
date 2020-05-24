package com.songlanyun.jymall.modules.business.jysys.imgs.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by zenghang on 2019/11/21.
 *
 * 图片集管理
 */
@Data
@Entity
@Table(name = "jy_img_sets")
public class Imgs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 组ID
     */
    private Integer groupId;
    /**
     * 组名称
     */
    private String groupName;
    /**
     * 点击跳转连接
     */
    private String link;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 图片备注 alt
     */
    private String imgName;
    /**
     * 排序
     */
    private Integer orderNo;
    /**
     * 项目ID
     */
    private Integer projectId;
}
