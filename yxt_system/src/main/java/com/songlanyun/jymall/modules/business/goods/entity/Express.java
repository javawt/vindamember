package com.songlanyun.jymall.modules.business.goods.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * 物流表
 */
@Data
@Entity
@Table(name = "yj_express")
public class Express {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer expressId; //物流公司id

    private String expressName; //物流公司名称

    private String expressCode; //物流公司代码

    private Integer sort; //排序

    private Integer wxappId; //小程序id

    private String expressLogo;//logo地址

}
