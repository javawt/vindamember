package com.songlanyun.jymall.modules.business.oss.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 文件库分组记录表
 */
@Entity
@Data
@Table(name = "yj_upload_group")
public class YjUploadGroup implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 分类id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "group_id", insertable = false, nullable = false)
  private Integer groupId;

  /**
   * 文件类型
   */
  @Column(name = "group_type", nullable = false)
  private String groupType = "";

  /**
   * 分类名称
   */
  @Column(name = "group_name", nullable = false)
  private String groupName = "";

  /**
   * 分类排序(数字越小越靠前)
   */
  @Column(name = "sort", nullable = false)
  private Integer sort = 0;

  /**
   * 是否删除
   */
  @Column(name = "is_delete", nullable = false)
  private Boolean delete = Boolean.FALSE;

  /**
   * 小程序id
   */
  @Column(name = "wxapp_id", nullable = false)
  private Integer wxappId = 0;

  /**
   * 创建时间
   */
  @Column(name = "create_time", nullable = false)
  private Integer createTime = 0;

  /**
   * 更新时间
   */
  @Column(name = "update_time", nullable = false)
  private Integer updateTime = 0;

  
}