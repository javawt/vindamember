package com.songlanyun.jymall.modules.business.sys.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetail implements Serializable {

    private int page;

    private int size;

    private int type;//0.收支明细  1.转账   2.提现   3.易货币

    private Long userId;

}
