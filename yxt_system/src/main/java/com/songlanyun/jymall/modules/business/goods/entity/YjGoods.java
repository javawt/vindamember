package com.songlanyun.jymall.modules.business.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商品记录表
 *
 * @author tsh
 * @email
 * @date 2020-04-23 16:38:40
 */
@Data
@Entity
@Table(name = "yj_goods")
public class YjGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer goodsId;

    /**
     * 0---普通 1---销售 2--批发
     **/
    private Integer activityType=0;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品卖点
     */
    private String sellingPoint;

    /**
     * 商品分类id
     */
    private Integer categoryId;

    /**
     * 商品规格(10单规格 20多规格)
     */
    private Integer specType;

    /**
     * 商品详情
     */
    private String content;

    /**
     * 初始销量
     */
    private Integer salesInitial = 0;

    /**
     * 实际销量
     */
    private Integer salesActual = 0;

    /**
     * 商品排序(数字越小越靠前)
     */
    private Integer goodsSort;

    /**
     * 商品状态(10上架 20下架)
     */
    private Integer goodsStatus;

    /**
     * 是否删除
     */
    private Integer isDelete = 0;

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 图片imgs(多张图片用英文“,”逗号分隔)
     */
    private String imgUrls;

    /**
     * 是否参加活动
     */
    private Boolean hasActivity = false;

    /**
     * 活动ID
     */
    private Integer activityId =0;

    /**
     * 售价
     */
    private Double price;

    /**
     * 打折价
     */
    private Double discount;
    /**
     * 运费
     */
    private Integer freight;
    /**
     * 寄售费用百分比
     **/
    private String goodsType;//0-普通 1-推荐 2-热卖

    private Integer shopId;//经销商id

    @Transient
    private Activity activity;//活动信息

    @Transient
    private Integer wsLimitnum;//商品限购数量



    @Transient
    private YjGoodsSku goodsSku;
    @Transient
    private  List<YjSpec> yjSpecList;

}
