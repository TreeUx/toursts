package com.bxly.toursts.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Description 出入口表字段实体类
 * @Author Breach
 * @Date 2018/11/19
 * @Version V1.0
 **/
@Data
//@Entity
public class ParkInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    /*城市名称*/
    private String city;
    /*出入口名称*/
    private String name;
    /*出入口uid*/
    private String uid;
    /*出入口地址*/
    private String address;
    /*父类uid*/
    private String parentUid;
    /*出入口展示名称*/
    private String showName;
    /*出入口坐标*/
    private String location;
    /*标签*/
    private String tag;

}
