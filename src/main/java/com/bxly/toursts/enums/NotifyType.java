package com.bxly.toursts.enums;

/**
 *  异步通知类型
 *
 * @author: kevin
 * @date: 2018/10/29 13:19
 */
public enum NotifyType {
    CONFIRMORDER("确认订单"),
    CONFIRMGUIDE("确认司导"),
    COMPLETESERVICE("订单完成");

    private String description;

    NotifyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
    public static void main(String[] args){
        System.out.println(NotifyType.CONFIRMGUIDE.getDescription());
    }

}
