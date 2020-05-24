package com.songlanyun.jymall.modules.business.jysys.apply.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zenghang.
 * Date: 2019/12/2
 * Time: 14:18
 * <p>
 * 地址表
 */
@Data
@Entity
@Table(name = "jy_apply_bind")
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;  //主键id

    @Column(name = "user_id")
    private String userId;  //用户id

    @Column(name = "user_name")
    private String userName;  //用户姓名

    @Column(name="apply_account")
    private String applyAccount;  //支付宝账号

    @Column(name = "is_delete")
    private Integer is_delete;  //删除标识

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time")
    private Date createTime; //创建时间

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime; //更新时间

}
