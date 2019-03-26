package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bxly.toursts.common.MapConstant;
import com.bxly.toursts.model.BxMerchantCo;
import com.bxly.toursts.model.ParkInfo;
import com.bxly.toursts.service.BaiduMapInfoService;
import com.bxly.toursts.utils.ReadTextUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bxly.toursts.controller.BaiduParkController.getChildInfo;
import static com.bxly.toursts.controller.BaiduParkController.getParkInfoUrl;

/**
 * @Description 获取百度景区出入口信息
 * @Author Breach
 * @Date 2018/11/19
 * @Version V1.0
 **/
@RestController
@Log4j2
@RequestMapping("/bxly")
public class BaiduParkInfoController {
    @Autowired
    BaiduMapInfoService baiduMapInfoService;

    /**
     * @return java.util.Map<java.lang.String                                                               ,                                                               java.lang.Object>
     * @Author Breach
     * @Description 获取广东省所有地区旅游景点出入口信息
     * @Date 2018/11/19
     * @Param
     */
    @RequestMapping("/getParkInfo")
    public Map<String, Object> getBaiduParkInfo() {
        Map<String, Object> resultMap = new HashMap<>();
//        String textPath = "D:\\district\\park.txt";//刀片机
        String textPath = "F:\\district\\next\\park.txt";
        JSONArray parkUrl = getParkInfoUrl(textPath);//获取请求地址
        ParkInfo pi = new ParkInfo();
        JSONArray results = new JSONArray();
        Boolean result = false;
        for (int i = 0; i < parkUrl.size(); i++) {
            String url = parkUrl.get(i).toString();
            JSONObject ja = getChildInfo(url);
            if (ja.containsKey("results")) {
                results = JSONArray.parseArray(ja.get("results").toString());
            } else {
                System.out.println("ja不存在results");
                continue;
            }
            if (!results.isEmpty() && results != null) {
                String city = "";
                if (JSONObject.parseObject(results.get(0).toString()).containsKey("city")) {
                    city = JSONObject.parseObject(results.get(0).toString()).getString("city");//城市
                }
                String parentUid = "";
                if (JSONObject.parseObject(results.get(0).toString()).containsKey("uid")) {
                    parentUid = JSONObject.parseObject(results.get(0).toString()).getString("uid").toString();//父类Uid(对应tourist_info表中的uid字段)
                }
                if (JSONObject.parseObject(results.get(0).toString()).containsKey("detail_info")) {
                    JSONArray children = null;
                    if (JSONObject.parseObject(JSONObject.parseObject(results.get(0).toString()).get("detail_info").toString()).containsKey("children")) {
                        children = JSONArray.parseArray(JSONObject.parseObject(JSONObject.parseObject(results.get(0).toString()).get("detail_info").toString()).get("children").toString());
                    }
                    String navi_location = "";//出入口坐标
                    if (children != null && !children.isEmpty()) {
                        for (int j = 0; j < children.size(); j++) {
                            JSONObject child = JSONObject.parseObject(children.get(j).toString());
                            JSONObject loca = JSONObject.parseObject(child.get("location").toString());
                            String name = child.get("name").toString();//出入口名称
                            String uid = child.get("uid").toString();//出入口uid
                            String address = child.get("address").toString();//出入口地址
                            String showName = child.get("show_name").toString();//出入口展示名称
                            String location = loca.get("lng") + "," + loca.get("lat");//出入口坐标
                            String tag = child.get("tag").toString();//标签
                            pi.setName(name);
                            pi.setCity(city);
                            pi.setUid(uid);
                            pi.setParentUid(parentUid);
                            pi.setAddress(address);
                            pi.setLocation(location);
                            pi.setShowName(showName);
                            pi.setTag(tag);
                            if (baiduMapInfoService.findCountByUid(uid) == 0) {
                                result = baiduMapInfoService.insertTouristParkInfo(pi);//插入数据
                            } else {
                                System.out.println("重复数据,跳过");
                                continue;
                            }

                            if (result) {
                                resultMap.put("status", "success");
                                resultMap.put("msg", "数据插入成功");
                            } else {
                                resultMap.put("status", "error");
                                resultMap.put("msg", "数据插入失败");
                            }
                        }
                    } else {
                        System.out.println("无children数据");
                        continue;
                    }
                }
            } else {
                System.out.println("无数据跳过");
                continue;
            }
        }

        return resultMap;
    }

    @RequestMapping("/update")
    public Map<String, Object> updateLocaInfo() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> locaList = baiduMapInfoService.getLocationInfos();
        if (!locaList.isEmpty()) {
            System.out.println("数据添加开始...");
            int count = baiduMapInfoService.updateLocaInfos(locaList);
            System.out.println(count);
            System.out.println("数据添加完毕");
        } else {
            result.put("status", "error");
            result.put("msg", "未查询到任何出入口坐标信息");
        }

        return result;
    }

    /**
     * @return java.util.Map<java.lang.String                               ,                               java.lang.Object>
     * @Author Breach
     * @Description
     * @Date 2019/1/2
     * @Param
     */
    @RequestMapping("/addParkInfo")
    public Map<String, Object> addAmericaWestCoastParkInfo() {
        Map<String, Object> resultMap = new HashMap<>();
//        Map<String, Object> param = new HashMap<>();
        JSONArray results = new JSONArray();
        JSONObject jo = new JSONObject();
        Boolean result = false;
        BxMerchantCo bmc = new BxMerchantCo();
//        param.put("scope", 2);
//        param.put("page_size", 1);
//        param.put("page_num", 0);
//        String path = "F:\\district\\west-coast.xlsx";
        String path = "C:\\app\\west-coast.xlsx";//服务器
        XSSFSheet sheet = new ReadTextUtils().readExcelInfo(path);//读取excel
        int rowNum = sheet.getLastRowNum();//总行数
        for (int i = 1; i < rowNum + 1; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            String query = row.getCell(0).getStringCellValue().trim();//景点名称
            String region = row.getCell(1).getStringCellValue().trim();//city
            String merAddress = row.getCell(2).getStringCellValue().trim();//地址
            String merIntroduce = row.getCell(3).getStringCellValue().trim();//详细介绍
            String ticketInfo = row.getCell(4).getStringCellValue().trim();//门票信息
            String merBest = row.getCell(5).getStringCellValue().trim();//最佳游玩时长
            String openTime = row.getCell(6).getStringCellValue().trim();//开放时间
            String trafficInfo = row.getCell(7).getStringCellValue().trim();//交通信息
            Map<String, Object> cityInfo = baiduMapInfoService.findCityId(region);//查询城市id
            int city = 0;
            int dstOffset = 0;
            int rawOffset = 0;
            int parentId = 0;
            String timezone = "";
            if (!cityInfo.isEmpty() && cityInfo != null) {
                if (cityInfo.get("id").toString() != "" && cityInfo.get("id") != null) {
                    city = Integer.parseInt(cityInfo.get("id").toString());//城市id
                }
                timezone = cityInfo.get("time_zone_id").toString();//时区id
                if (cityInfo.get("dst_offset").toString() != null && cityInfo.get("dst_offset") != null) {
                    dstOffset = Integer.parseInt(cityInfo.get("dst_offset").toString());//夏令时间编移秒数
                }
                if (cityInfo.get("dst_offset").toString() != null && cityInfo.get("dst_offset") != null) {
                    rawOffset = Integer.parseInt(cityInfo.get("raw_offset").toString());//坐标点位置时间协调世界编移秒数
                }
                if (cityInfo.get("parent_id").toString() != null && cityInfo.get("parent_id") != null) {//父类id
                    parentId = Integer.parseInt(cityInfo.get("parent_id").toString());
                }
            }

            jo.put("address", region + query);
            jo.put("location", "");
            String requestUrl = MapConstant.GOOGLE_PLACE_URL + region + query
                    + MapConstant.GOOGLE_LANGUAGE + MapConstant.GOOGLE_SUFFIX_URL + MapConstant.API_KEY;//请求地址 //google
            System.out.println(requestUrl);
//            JSONObject resultJo = RequestUtils.sendPost(requestUrl);
            JSONObject resultJo = new GoogMapController().getGoogMapInfo(jo);
            System.out.println(resultJo);
            System.out.println("************");
            String location = "";
            if (resultJo.containsKey("location")) {
                /*JSONObject resultJos = JSONObject.parseObject(JSON.toJSONString(resultJo.get("results").toString())
                        .substring(1, JSON.toJSONString(resultJo.get("results").toString()).length() - 1));*/
                JSONObject locaJo = JSONObject.parseObject(resultJo.get("location").toString());
                BigDecimal lng = new BigDecimal(locaJo.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal lat = new BigDecimal(locaJo.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                location = lat + "," + lng;
                System.out.println("location" + location);
                System.out.println("************");
            }

            bmc.setMerDuplex(location);//双出入口坐标
            bmc.setMerIntroduce(merIntroduce);//详情介绍
            bmc.setTimezone(timezone);//时区id
            bmc.setDstOffset(dstOffset);//夏令时间编移秒数
            bmc.setRawOffset(rawOffset);//坐标点位置时间协调世界编移秒数
            bmc.setState(10242);//美国国家id
            bmc.setCity(city);
            bmc.setMerCentral(location);//中心点坐标
            bmc.setMerName(query);//景点名称
            bmc.setMerAddress(merAddress);//地址
            bmc.setTicketInfo(ticketInfo);//门票信息
            bmc.setTrafficInfo(trafficInfo);//交通信息
            bmc.setParentId(parentId);//父类id

            result = baiduMapInfoService.insertMerchantParkInfo(bmc);//插入数据
            /*JSONObject ja = getChildInfo(url);
            if (ja.containsKey("results")) { //百度
                results = JSONArray.parseArray(ja.get("results").toString());
            } else {
                System.out.println("ja不存在results");
                continue;
            }
            if (!results.isEmpty() && results != null) {

                if (JSONObject.parseObject(results.get(0).toString()).containsKey("detail_info")) {
                    JSONArray children = null;
                    if (JSONObject.parseObject(JSONObject.parseObject(results.get(0).toString()).get("detail_info").toString()).containsKey("children")) {
                        children = JSONArray.parseArray(JSONObject.parseObject(JSONObject.parseObject(results.get(0).toString()).get("detail_info").toString()).get("children").toString());
                    }
                    String navi_location = "";//出入口坐标
                    if (children != null && !children.isEmpty()) {
                        for (int k = 0; k < children.size(); k++) {
                            JSONObject child = JSONObject.parseObject(children.get(k).toString());
                            JSONObject loca = JSONObject.parseObject(child.get("location").toString());
//                            String address = child.get("address").toString();//出入口地址
                            String location = loca.get("lng") + "," + loca.get("lat");//出入口坐标
                            bmc.setMerDuplex(location);//双出入口坐标
                            bmc.setMerIntroduce(merIntroduce);//详情介绍
                            bmc.setTimezone(timezone);//时区id
                            bmc.setDstOffset(dstOffset);//夏令时间编移秒数
                            bmc.setRawOffset(rawOffset);//坐标点位置时间协调世界编移秒数
                            bmc.setState(10242);//美国国家id
                            bmc.setCity(city);
                            bmc.setMerCentral(location);//中心点坐标
                            bmc.setMerName(query);//景点名称
                            bmc.setMerAddress(merAddress);//地址
                            bmc.setTicketInfo(ticketInfo);//门票信息
                            bmc.setTrafficInfo(trafficInfo);//交通信息
                            bmc.setParentId(parentId);//父类id

                            result = baiduMapInfoService.insertMerchantParkInfo(bmc);//插入数据

                            if (result) {
                                resultMap.put("status", "success");
                            } else {
                                resultMap.put("status", "error");
                            }
                        }
                    } else {
                        System.out.println("无children数据");
                        continue;
                    }
                }
            } else {
                System.out.println("无数据跳过");
                continue;
            }*/
        }
        return resultMap;
    }


    public static void main(String[] args) throws InterruptedException {
        /*Thread t = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            if(i == 6) {
                t.sleep(3000);
            }
            System.out.println(i);
        }*/

    }

}
