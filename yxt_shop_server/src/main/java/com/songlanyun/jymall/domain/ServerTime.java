package com.songlanyun.jymall.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

/**
 * 平台和商家的服务
 */
@Data
@Entity
@Table(name = "yj_server_time")
public class ServerTime implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, nullable = false)
    private Long id;

    /**
     * 店铺id  -1表示是平台
     */
    @Column(name = "shop_id")
    private Long shopId = -1L;

    /**
     * 服务项目类型id
     */
    @Column(name = "srv_id")
    private Long srvId;

    /**
     * 此服务id-开始时间
     */
    @Column(name = "start_time")
    private Time startTime;

    /**
     * 此服务id--结束时间
     */
    @Column(name = "end_time")
    private Time endTime;

    /**
     * 此服务时间段内服务最大人数
     */
    @Column(name = "srv_max")
    private Integer srvMax;

    /** 当前服务人数  **/
    @JsonIgnore
    private Integer currSrvCnt;
    
}