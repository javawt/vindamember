package com.songlanyun.jymall.modules.business.oss.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件库记录表
 */
@Entity
@Table(name = "yj_upload_file")
@Data
public class YjUploadFile implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 文件id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(insertable = false, name = "file_id", nullable = false)
  private Integer fileId;

  /**
   * 存储方式
   */
  @Column(name = "storage", nullable = false)
  private String storage = "";

  /**
   * 文件分组id
   */
  @Column(name = "group_id", nullable = false)
  private Integer groupId = 0;

  /**
   * 存储域名
   */
  @Column(name = "file_url", nullable = false)
  private String fileUrl = "";

  /**
   * 文件路径
   */
  @Column(name = "file_name", nullable = false)
  private String fileName = "";

  /**
   * 文件大小(字节)
   */
  @Column(name = "file_size", nullable = false)
  private Long fileSize = 0l;

  /**
   * 文件类型
   */
  @Column(name = "file_type", nullable = false)
  private String fileType = "";

  /**
   * 文件扩展名
   */
  @Column(name = "extension", nullable = false)
  private String extension = "";

  /**
   * 是否为c端用户上传
   */
  @Column(name = "is_user", nullable = false)
  private Boolean user = Boolean.FALSE;

  /**
   * 是否已回收
   */
  @Column(name = "is_recycle", nullable = false)
  private Boolean recycle = Boolean.FALSE;

  /**
   * 软删除
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
  private Date createTime;

  
}