package com.songlanyun.jymall.modules.business.sys.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogParameter implements Serializable {
    private int page;

    private int limit;

    private String key;
}
