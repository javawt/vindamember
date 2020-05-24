package com.songlanyun.jymall.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 服务所属大类
 */
@Data
@Entity
@Table(name = "yj_server_prj")
public class ServerPrj implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 生成时间
     */
    @Column(name = "create_time")
    private Timestamp createTime;

    /**
     * 服务项目
     */
    @Id
    @Column(name = "id", insertable = false, nullable = false)
    private Long id;

    /**
     * 服务项目图片
     */
    @Column(name = "imgs")
    private String imgs;

    /**
     * 服务项目名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 排序值
     */
    @Column(name = "recno")
    private Integer recno;

    
}