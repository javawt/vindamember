package com.songlanyun.jymall.modules.business.jysys.uploadfile.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tsh.
 * Date: 2019/12/10
 * Time: 14:18
 * <p>
 * 地址表
 */
@Data
@Entity
@Table(name = "yj_upload_file")
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Integer fileId;


    @Column(name = "storage")
    private String storage;

    @Column(name = "group_id")
    private Integer groupId;


    @Column(name = "file_url")
    private String fileUrl;


    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "file_type")
    private String fileType;


    @Column(name = "extension")
    private String extension;

    @Column(name = "is_user")
    private Integer isUser;

    @Column(name = "is_recycle")
    private Integer isRecycle;

    @Column(name = "is_delete")
    private Integer isDelete;

    @Column(name = "wxapp_id")
    private Integer wxappid;


    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime = new Date();


}
