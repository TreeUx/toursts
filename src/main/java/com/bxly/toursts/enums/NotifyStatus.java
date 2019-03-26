package com.bxly.toursts.enums;

/**
 * @author: kevin
 * @date: 2018/10/29 13:02
 */
public enum NotifyStatus {
    SUCCESS("通知成功"),
    ERROR("通知失败");

    private String description;

    NotifyStatus(String description) {
        this.description = description;
    }
}
