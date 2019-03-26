package com.bxly.toursts.enums;

/**
 *  业务类型枚举
 * @author: kevin
 * @date: 2018/10/24 15:16
 */
public enum ServiceType {
    //接机：Pick someone up at the airport
    PA(1, "接机"),
    //送机：Feed machine
    FM(2, "送机"),
    //日租：Daily rent
    DN(3, "日租"),
    //次租：Sub renting
    SR(4, "次租"),
    //submitGoodsRouteOrder
    GRO(5, "提交固定线路或推荐线路订单"),
    //submitGoodsPickupOrder
    GPO(6, "提交交通接驳接机订单"),
    //submitGoodsTransferOrder
    GTO(6, "提交交通接驳送机订单"),
    //submitGoodsSingleOrder
    GSO(6, "提交交通接驳次租订单");

    private int type;
    private String description;

    private ServiceType(int type, String description) {
        this.type = type;
        this.description = description;
    }

}
