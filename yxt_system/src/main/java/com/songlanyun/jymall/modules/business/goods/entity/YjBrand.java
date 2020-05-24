package com.songlanyun.jymall.modules.business.goods.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "yj_brand")
public class YjBrand implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", insertable = false, nullable = false)
  private Integer id;

  /**
   * 品牌名称
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * 品牌logo图片【在照片墙中选择的id】
   */
  @Column(name = "logo")
  private String logo;

  /**
   * 品牌说明
   */
  @Column(name = "content")
  private String content;

  /**
   * 10---热门
   */
  @Column(name = "hot")
  private Integer hot = 0;

  /**
   * 排序号
   */
  @Column(name = "orderNo")
  private Integer orderNo;

  /**
   * 录入时间
   */
  @Column(name = "create_time")
  private Date createTime;

  
}