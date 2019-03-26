package com.bxly.toursts.enums;

/**
 * 支付状态定义
 *
 * @author: kevin
 * @date: 2018/10/27 10:50
 */
public enum PayType {
    NOPAY("尚未支付"),
    SUCCESS("支付成功"),
    ERROR("支付失败");


    private String description;

    PayType(String description) {
        this.description = description;
    }
}
