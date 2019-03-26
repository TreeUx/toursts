package com.bxly.toursts.controller;

import com.bxly.toursts.service.BaiduMapInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/11/28
 * @Version V1.0
 **/
@RestController
@RequestMapping("/bxly")
public class MakeRelationByCity {
    @Autowired
    BaiduMapInfoService baiduMapInfoService;

    @RequestMapping("/relationCity")
    public Map<String, Object> relationCity() {
        int res = 0;
        Map<String, Object> results = new HashMap<>();
        List<Map<String, Object>> cityInfoList = new ArrayList<>();
        cityInfoList = baiduMapInfoService.queryCityInfos();
        System.out.println(cityInfoList);
        for(int i = 0; i < cityInfoList.size(); i++) {
            int id = (int) cityInfoList.get(i).get("id");
            String area_name = String.valueOf(cityInfoList.get(i).get("area_name"));
            Map<String, Object> para = new HashMap<>();
            para.put("id", id);
            para.put("area_name", area_name);
            int count =  baiduMapInfoService.findTouristCityInfo(area_name);//查询是市还是县
            long startTime = System.currentTimeMillis() / 1000;//操作起始时间
            if(count == 0) {
                System.out.println(area_name + "：修改中...请稍后...");
                res = baiduMapInfoService.updateTouistCityInfo(para);//修改市
                long endTime = System.currentTimeMillis() / 1000;//操作结束时间
                System.out.println("修改了" + res + "条数据，耗时：" + (endTime - startTime) + "秒");
            } else {
                System.out.println(area_name + "：修改中...请稍后...");
                res = baiduMapInfoService.updateTouistCountryInfo(para);//修改县
                long endTime = System.currentTimeMillis() / 1000;//操作结束时间
                System.out.println("修改了" + res + "条数据，耗时：" + (endTime - startTime) + "秒");
            }
            System.out.println(area_name + "：修改完毕");
            if(res != 0) {
                results.put("status", "success");
                results.put("msg", "修改成功");
            } else {
                results.put("status", "error");
                results.put("msg", "修改失败");
            }
        }
        return results;
    }
}
