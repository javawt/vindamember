package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zenghang.
 * Date: 2019/11/26
 * Time: 15:46
 * <p>
 * 活动表
 */
@Data
@Entity
@Table(name = "yj_activity")
public class Activity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 活动开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 排序
     */
    private Integer orderNo;

    /**
     * 活动类型（0: 普通  1：零售， 2：批发）
     */
    private Integer type;

    /**
     * 是否推荐到首页
     */
    private Boolean hasRecommend;

    private BigDecimal maxAccount;

    /**
     * 活动商品list
     */
    @Transient
    private List<YjGoods> goodsList;

    @Transient
    private String nameTime;

    private int isComple=0;
}
