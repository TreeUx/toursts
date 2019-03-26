package com.bxly.toursts.enums;

/**
 * 订单状态枚举
 *
 * @author: kevin
 * @date: 2018/10/26 10:24
 */
public enum OrderType {
    //PO: place Order
    PO("请求下单"),
    POSUCCESS("下单成功"),
    POERROR("下单失败"),
    POCANCEL("取消订单"),
    POCOMPLETE("订单完成");

    private String description;

    OrderType(String description) {
        this.description = description;
    }
    
    public static void main(String[] args){
        System.out.println(OrderType.PO.description);
    }
}
