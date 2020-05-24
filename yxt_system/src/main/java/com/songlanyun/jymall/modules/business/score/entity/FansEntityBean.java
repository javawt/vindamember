package com.songlanyun.jymall.modules.business.score.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FansEntityBean {

    /** 推荐人  **/
    private String refName;

    /** 推荐人手机号  **/
    private String refTel;

    /** 邀请码 **/
    private String nick_name="";
    /** 用户昵称 **/
    private String Invitation_code="";
    /** 入伙日期 **/
    private Date register_dt;
    /** 用户头像  **/
    private String headImg;

    /** 团队粉丝数  **/
    private int team_fans=0;

    /** 当月业绩 **/
    private BigDecimal currScore=BigDecimal.ZERO;

    /** 用户id **/
    private Long userId;

    /** 会员等级 **/
    private int lev=10;
    /** 会员等级名　**/
    private String levName="";

    /** 团队业线 **/
    private BigDecimal teamScore=BigDecimal.ZERO;

    /**联系电话 **/
    private String tel;

    /** 0---间推  1-- 直推**/
    private int isDirect=0;




}
