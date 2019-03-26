package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bxly.toursts.utils.ExcelUtils;
import com.bxly.toursts.utils.MapResultUtils;
import com.bxly.toursts.utils.ReadTextUtils;
import com.bxly.toursts.utils.RequestUtils;

import java.util.HashMap;
import java.util.Map;

public class BaiduParkController {
    public static void main(String[] args) {
        String textPath = "F:\\district\\next\\park.txt";
//        String textPath = "D:\\district\\park.txt";//刀片机
        JSONArray parkUrl = getParkInfoUrl(textPath);
        for (int i = 0; i < parkUrl.size(); i++) {
            String url = parkUrl.get(i).toString();
            JSONObject ja = getChildInfo(url);
            String filePath = "F:\\全国地点详情信息-旅游景点-出入口.xlsx";
//            String filePath = "F:\\全国地点详情信息-旅游景点-出入口.xlsx";
//            int index = i;
            ExcelUtils.testWriteToExcel(filePath, ja);
        }

    }

    /**
     * @return com.alibaba.fastjson.JSONArray
     * @Author Breach
     * @Description 获取请求地址
     * @Date 2018/11/8
     * @Param textPath 存放城市及公园名称的txt地址
     */
    public static JSONArray getParkInfoUrl(String textPath) {
        Map<String, Object> param = new HashMap<>();
        JSONArray jas = new JSONArray();
        JSONArray ja = ReadTextUtils.readText(textPath);

        param.put("scope", 2);
        param.put("page_size", 1);
        param.put("page_num", 0);
        System.out.println(ja.size());
        for (int i = 0; i < ja.size(); i++) {
            String region = ja.get(i).toString().split(",")[0].trim();
            String query = ja.get(i).toString().split(",")[1].trim();
            param.put("region", region);
            param.put("query", query);
            String url = MapResultUtils.getRequestUrl(param);//单个请求地址
//            System.out.println(url);
            jas.fluentAdd(url);
        }
        return jas;
    }

    public static JSONObject getChildInfo(String url) {
        System.out.println(url);
        JSONObject jo = RequestUtils.sendPost(url);
//        JSONArray jos = JSONObject.parseArray(jo.get("results").toString());
//        JSONArray ja = JSONArray.parseArray(JSONObject.parseObject(JSONObject.parseObject(jos.get(0).toString()).get("detail_info").toString()).get("children").toString());
//        System.out.println(ja.size());
        return jo;
    }


}

  