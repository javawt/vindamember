package com.songlanyun.jymall.modules.business.jysys.appconfig.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tsh.
 * Date: 2019/12/9
 * Time: 14:18
 * <p>
 * app配置表
 */
@Data
@Entity
@Table(name = "yj_app_config")
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;  //主键


    @Column(name = "ver")
    private String ver;  //版本号ver


    @Column(name = "name")
    private String name;  //名称

    @Column(name = "number")
    private String number;  //版本号

    @Column(name = "info")
    private String info;  //详情

    @Column(name = "android_link")
    private String androidLink;  //安卓地址

    @Column(name = "ios_link")
    private String iosLink;  //ios文件

    @Column(name = "packge_size")
    private String packgeSize;  //大小

    @Column(name = "type")
    private String type;  //类型

    /**
     * 创建时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime = new Date();

    /**
     * 更新时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    private String download;//下载页面的址

}
