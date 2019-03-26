package com.bxly.toursts.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/11/21
 * @Version V1.0
 **/
@Data
//@Entity
public class JourneyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*国家*/
    private String nation;
    /*省*/
    private String province;
    /*市*/
    private String city;
    /*评分*/
    private String mainScore;
    /*大家印象*/
    @Column(columnDefinition = "TEXT")//设置字段类型为text类型
    private String impression;
    /*更多描述*/
    @Column(columnDefinition = "TEXT")//设置字段类型为text类型
    private String moreDesc;
    /*地图坐标*/
    private String mapInfo;
    /*交通出行方式*/
    @Column(columnDefinition = "TEXT")
    private String trafficInfo;
    /*最佳季节*/
    @Column(columnDefinition = "TEXT")
    private String bestSeason;
    /*建议游玩时间*/
    private String suggestPlayTime;
    /*门票价格*/
    private String ticketInfo;
    /*人均消费*/
    private String avgCost;
    /*访问人数*/
    private String goingCount;
    /*开放时间*/
    private String openTime;
    /*景点地址*/
    @Column(columnDefinition = "TEXT")
    private String address;
    /*景点排名*/
    private String jouneryRank;
    /*景点电话*/
    private String phone;
    /*官网*/
    private String webSite;
    /*友情贴士*/
    @Column(columnDefinition = "TEXT")
    private String friendshipTips;
}
