package com.songlanyun.jymall.common.utils;

import lombok.Data;

import java.util.List;

@Data
public class KDCode {
    private String EBusinessID; //电商用户ID

    private String LogisticCode; //物流单号

    private Boolean Success; //成功与否

    private String Code; //失败原因

    private List<ShipperInfo> Shippers;


}
