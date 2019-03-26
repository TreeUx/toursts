package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bxly.toursts.controller.base.BaseController;
import com.bxly.toursts.model.TouristInfo;
import com.bxly.toursts.service.BaiduMapInfoService;
import com.bxly.toursts.utils.MapResultUtils;
import com.bxly.toursts.utils.ReadTextUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/11/15
 * @Version V1.0
 **/
@RestController
@Log4j2
@RequestMapping("/bxly")
public class BaiduMapInfoController extends BaseController {
    @Autowired
    BaiduMapInfoService baiduMapInfoService;

    public static String regionPath = "F:\\district\\next\\cityName.txt";//区域
    public static String typePath = "F:\\district\\next\\oneLevelFile.txt";//一级行业分类列表
    public static String tagPath = "F:\\district\\next\\twoLevelFile.txt";//二级行业分类表名列表
//    public static String regionPath = "D:\\tourist\\cityName.txt";//区域
//    public static String typePath = "D:\\tourist\\oneLevelFile.txt";//一级行业分类列表
//    public static String tagPath = "D:\\tourist\\twoLevelFile.txt";//二级行业分类表名列表
    //excel列表头字段
    private static final String[] strs = {"province", "city", "area", "name", "location", "navi_location", "address", "uid", "street_id"
            , "telephone", "type", "tag", "price", "shop_hours", "overall_rating", "taste_rating", "service_rating", "environment_rating"
            , "facility_rating", "hygiene_rating", "technology_rating", "groupon_num", "discount_num", "image_num", "comment_num"
            , "favorite_num", "checkin_num", "detail_url"};

    @RequestMapping("/getInfo")
    public Map<String, Object> getTouristInfo() {
        JSONArray jaRegion = ReadTextUtils.readText(regionPath);//所有区域
        JSONArray jaType = ReadTextUtils.readText(typePath);//一级行业分类
        JSONArray jaTag = ReadTextUtils.readText(tagPath);//二级行业分类
        Map<String, Object> ResultMap = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        JSONArray jas = new JSONArray();//存放读取的数据
        TouristInfo tv = new TouristInfo();
        param.put("scope", 2);
        param.put("page_size", 20);//一页查询的数据条数
        for (int i = 0; i < jaType.size(); i++) {//旅游一级菜单信息
            String tag = jaType.get(i).toString();//（获取一级行业分类名称）【如：美食、酒店、购物等】
            param.put("tag", tag);
            String twoLevelName = jaTag.get(i).toString();
            JSONArray ja = ReadTextUtils.readText(twoLevelName);
            for (int k = 0; k < jaRegion.size(); k++) {//区域
                String region = jaRegion.get(k).toString();
                param.put("region", region);//北京市、广州市
                for (int j = 0; j < ja.size(); j++) {//旅游二级菜单
                    param.put("query", ja.get(j).toString());
                    for (int m = 0; m < 20; m++) {//请求分页参数
                        param.put("page_num", m);
                        jas = MapResultUtils.getMapResult(param);
                        if (jas != null && !jas.isEmpty()) {
                            System.out.println(jas);
                            for (int n = 0; n < jas.size(); n++) {  
                                JSONObject jt = JSONObject.parseObject(jas.get(n).toString());
                                //请求结果中的详情信息
                                JSONObject detailInfoJo = JSONObject.parseObject(JSONObject.parseObject(jas.get(n).toString()).get("detail_info").toString());
//                                System.out.println(jt);//请求数据集合
                                tv.setProvince(jt.get("province").toString());//省
                                tv.setCity(jt.get("city").toString());//市
                                tv.setArea(jt.get("area").toString());//区
                                tv.setUid(jt.get("uid").toString());//地区uid
                                if(jt.containsKey("street_id")) {
                                    tv.setUid(jt.get("street_id").toString());//街景图uid
                                } else {
                                    tv.setUid("");//街景图uid
                                }
                                tv.setName(jt.get("name").toString());//名称
                                if(jt.containsKey("address")) {
                                    tv.setAddress(jt.get("address").toString());//地址
                                } else {
                                    tv.setAddress("");//地址
                                }
                                tv.setLocation(jt.get("location").toString());//中心点坐标
                                if(detailInfoJo.containsKey("navi_location")) {
                                    tv.setNaviLocation(detailInfoJo.get("navi_location").toString());//出入口坐标
                                } else {
                                    tv.setNaviLocation(jt.get("location").toString());//出入口坐标
                                }
                                if(detailInfoJo.containsKey("detail_url")) {
                                    tv.setDetailUrl(detailInfoJo.get("detail_url").toString());//详情页
                                } else {
                                    tv.setDetailUrl("");//详情页
                                }
                                if(detailInfoJo.containsKey("type")) {
                                    tv.setType(detailInfoJo.get("type").toString());//类型type
                                } else {
                                    tv.setType("");//类型type
                                }
                                if(detailInfoJo.containsKey("tag")) {
                                    tv.setTag(detailInfoJo.get("tag").toString());//英文类型tag
                                } else {
                                    tv.setTag("");//英文类型tag
                                }
                                if(detailInfoJo.containsKey("phone")) {
                                    tv.setPhone(detailInfoJo.get("phone").toString());//电话
                                } else {
                                    tv.setPhone("");//电话
                                }
                                if(detailInfoJo.containsKey("price")) {
                                    tv.setPrice(detailInfoJo.get("price").toString());//人均消费金额
                                } else {
                                    tv.setPrice("0");//人均消费金额
                                }
                                if(detailInfoJo.containsKey("overall_rating")) {
                                    tv.setOverallRating(detailInfoJo.get("overall_rating").toString());//总体评分
                                } else {
                                    tv.setOverallRating("0");
                                }
                                Boolean result = baiduMapInfoService.getBaiduMapInfo(tv);//插入数据
                                if(result) {
                                    ResultMap.put("status", "success");
                                    ResultMap.put("msg", "数据插入成功");
                                } else {
                                    ResultMap.put("status", "error");
                                    ResultMap.put("msg", "数据插入失败");
                                }
                            }
                        } else {
                            System.out.println("无数据跳出");
                            break;
                        }
                    }
                }
            }
        }
        return ResultMap;
    }

   /* public static void main(String[] args) {
        new BaiduMapInfoController().getTouristInfo();
    }*/

}
