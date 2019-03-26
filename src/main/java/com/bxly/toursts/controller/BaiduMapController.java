/*
package com.bxly.toursts.controller;


import com.alibaba.fastjson.JSONArray;
import com.bxly.toursts.utils.ReadTextUtils;
import com.bxly.toursts.utils.WriteToExcelUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequestMapping("/bxly")
public class BaiduMapController {
    public static int num = 0;
    //excel列表头字段
    private static final String[] strs = {"province", "city", "area", "name", "location", "navi_location", "address", "uid", "street_id"
            , "telephone", "type", "tag", "price", "shop_hours", "overall_rating", "taste_rating", "service_rating", "environment_rating"
            , "facility_rating", "hygiene_rating", "technology_rating", "groupon_num", "discount_num", "image_num", "comment_num"
            , "favorite_num", "checkin_num", "detail_url"};



    */
/**
     * @return void
     * @Author Breach
     * @Description 根据百度api请求百度数据保存到excel
     * @Date 2018/11/14
     * @Param
     *//*

    @RequestMapping("/test")
    public void getInfo() {
        String excelFilePath = "F:\\district\\next\\excelNameFile.txt";//要导入的Excel表格表名
        String regionPath = "F:\\district\\next\\cityName.txt";//区域
        String typePath = "F:\\district\\next\\oneLevelFile.txt";//一级行业分类列表
        String tagPath = "F:\\district\\next\\twoLevelFile.txt";//二级行业分类表名列表

        JSONArray jaFile = ReadTextUtils.readText(excelFilePath);
        JSONArray jaRegion = ReadTextUtils.readText(regionPath);
        JSONArray jaType = ReadTextUtils.readText(typePath);
        JSONArray jaTag = ReadTextUtils.readText(tagPath);
        for (int i = 1; i < jaFile.size(); i++) {//要写入的excel表名集合层
            String filePath = jaFile.get(i).toString();//（获取单个要导入的excel名称）
            String textPath = jaTag.get(i).toString();//获取二级行业分类名称【如：E:\\district\\traffic.txt】
            String tag = jaType.get(i).toString();//（获取一级行业分类名称）【如：美食、酒店、购物等】
            for (int j = 0; j < jaRegion.size(); j++) {//城市、区域层
                String region = jaRegion.get(j).toString();//（获取区域名称）
                WriteToExcelUtils.writeToExcel(filePath, textPath, region, tag);//通用
                num++;
            }
        }
    }


   */
/* public static void main(String[] args) throws IOException {
        String excelFilePath = "F:\\district\\next\\excelNameFile.txt";//要导入的Excel表格表名
        String regionPath = "F:\\district\\next\\cityName.txt";//区域
        String typePath = "F:\\district\\next\\oneLevelFile.txt";//一级行业分类列表
        String tagPath = "F:\\district\\next\\twoLevelFile.txt";//二级行业分类表名列表

        JSONArray jaFile= ReadTextUtils.readText(excelFilePath);
        JSONArray jaRegion = ReadTextUtils.readText(regionPath);
        JSONArray jaType = ReadTextUtils.readText(typePath);
        JSONArray jaTag = ReadTextUtils.readText(tagPath);
        for(int i = 3; i < jaFile.size(); i++) {//要写入的excel表名集合层
            String filePath = jaFile.get(i).toString();//（获取单个要导入的excel名称）
            String textPath = jaTag.get(i).toString();//获取二级行业分类名称【如：E:\\district\\traffic.txt】
            String tag = jaType.get(i).toString();//（获取一级行业分类名称）【如：美食、酒店、购物等】
            for (int j = 0; j < jaRegion.size(); j++) {//城市、区域层
                String region = jaRegion.get(j).toString();//（获取区域名称）
                WriteToExcelUtils.writeToExcel(filePath, textPath, region, tag);//通用
                num++;
            }
        }
    }*//*



}


  */
