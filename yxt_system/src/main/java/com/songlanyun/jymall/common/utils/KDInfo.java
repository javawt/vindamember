package com.songlanyun.jymall.common.utils;

import lombok.Data;

import java.util.List;

@Data
public class KDInfo {
    private String LogisticCode; //物流运单号

    private String ShipperCode; //快递公司编码

    private String State; //物流状态：2-在途中,3-签收,4-问题件

    private String EBusinessID; //用户ID

    private Boolean Success; //成功与否

    private String OrderCode; //订单编号

    private String Reason; //失败原因

    private List<KDTraces> Traces; //节点信息

    private KDCode kdInfo; //快递物流信息

}
