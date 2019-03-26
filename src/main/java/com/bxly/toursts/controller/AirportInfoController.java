package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bxly.toursts.common.CommodityConstant;
import com.bxly.toursts.common.MapConstant;
import com.bxly.toursts.common.ReadExcel;
import com.bxly.toursts.service.BaiduMapInfoService;
import com.bxly.toursts.utils.MapResultUtils;
import com.bxly.toursts.utils.ReadTextUtils;
import com.bxly.toursts.utils.RequestUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.print.attribute.standard.JobOriginatingUserName;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/13
 * @Version V1.0
 **/
@RestController
@RequestMapping("/bx")
public class AirportInfoController {
    @Autowired
    BaiduMapInfoService baiduMapInfoService;

    /**
     * @return java.util.Map<java.lang.String       ,       java.lang.Object>
     * @Author Breach
     * @Description 添加全球机场信息到bx_commodity商品表中
     * @Date 2018/12/14
     * @Param
     */
    @RequestMapping("/addAirplaneInfo")
    public Map<String, Object> addAirplaceInfo() {
        Map<String, Object> result = new HashMap<>();
        Sheet sheet;
//        String path = "F:\\district\\airplanecode.xls";//读取全球机场信息
//        String filePath = "F:\\district\\globalairport.xls";//读取全球机场信息
        String path = "C:\\app\\airport\\airplanecode.xls";//存放全球国家excel(美国服务器)
        String filePath = "C:\\app\\airport\\globalairport.xls";//读取全球机场信息(美国服务器)
        JSONObject nationJo = getGlobalNationInfo(path);//获取全球国家信息
        sheet = ReadExcel.readExcelInfo(filePath);
        int rowNums = sheet.getPhysicalNumberOfRows();//获取当前sheet的总行数
        for (int i = 1; i < rowNums; i++) {//从第二行开始读 （标题栏数据不读）
            Row row = sheet.getRow(i);
            Map<String, Object> para = new HashMap<>();
            String city = row.getCell(0).getStringCellValue();//城市
            String com_code = row.getCell(1).getStringCellValue();//商品编码（三字码）
            String com_name = row.getCell(3).getStringCellValue();//商品名称（机场名称）
            String state = "";
            if (nationJo.containsKey(com_code)) {
                state = nationJo.get(com_code).toString();//国家
            }
            String com_entrance = "";//入口坐标
            String com_exit = "";//出口坐标
            String com_duplex = "";//出入口坐标
            String address = "";//详细地址
            if ("中国".equals(state)) {
                state = "国内";
            }
            para.put("state", state);//国家
            para.put("city", "%" + city + "%");//城市
            JSONObject navi_loca = new JSONObject();
            if ("国内".equals(state)) {//国内机场获取百度地图坐标
                JSONArray results = new AirportInfoController().getParkInfoUrl(city, com_name);
                System.out.println(results);
                if (!results.isEmpty() && results != null) {
                    JSONObject jo = JSONObject.parseObject(results.get(0).toString());
                    address = jo.get("address").toString();//详细地址
                    if (jo.containsKey("detail_info")) {
                        System.out.println(JSONObject.parseObject(jo.get("detail_info").toString()).get("navi_location"));
                        if(jo.get("detail_info") != null && JSONObject.parseObject(jo.get("detail_info").toString()).get("navi_location") != null) {
                            navi_loca = JSONObject.parseObject(JSONObject.parseObject(jo.get("detail_info").toString()).get("navi_location").toString());
                        } else {
                            navi_loca = JSONObject.parseObject(jo.get("location").toString());
                        }
                        BigDecimal lng = new BigDecimal(navi_loca.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                        BigDecimal lat = new BigDecimal(navi_loca.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                        com_duplex = lng + "," + lat;//出入口坐标
                        if (JSONObject.parseObject(jo.get("detail_info").toString()).containsKey("children")) {
                            JSONArray jas = new JSONArray();
                            jas = JSONArray.parseArray(JSONObject.parseObject(jo.get("detail_info").toString()).get("children").toString());
                            for (int j = 0; j < jas.size(); j++) {
                                JSONObject childrenJo = JSONObject.parseObject(jas.get(j).toString());
                                String tag = childrenJo.get("tag").toString();
                                JSONObject locaJo = JSONObject.parseObject(childrenJo.get("location").toString());
                                if (tag.trim().contains("入口")) {
                                    BigDecimal lngs = new BigDecimal(locaJo.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                    BigDecimal lats = new BigDecimal(locaJo.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                    com_entrance = lngs + "," + lats;//入口坐标
                                } else if (tag.trim().contains("出口")) {
                                    BigDecimal lngs = new BigDecimal(locaJo.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                    BigDecimal lats = new BigDecimal(locaJo.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                    com_exit = lngs + "," + lats;//出口坐标
                                }
                            }
                        }
                    }
                } else {
                    continue;
                }

            } else {//国外机场获取google地图坐标点
                Map<String, Object> paras = getGoogMapInfo(city, com_name);//国外机场出入口坐标
                System.out.println("************* google坐标 ***********");
                System.out.println(paras);
                System.out.println("************* google坐标 ***********");
                if (!paras.isEmpty() && paras != null) {
                    com_duplex = paras.get("com_duplex").toString();//出入口坐标
                    address = paras.get("address").toString();//详细地址
                }
            }
            List<Map<String, Object>> list = baiduMapInfoService.getStateAndCityInfo(para);//获取国家id和城市id及parentid
            Map<String, Object> params = new HashMap<>();
            System.out.println("list = " + list);
            if (!list.isEmpty() && list != null && list.size() > 0) {
                String stateId = list.get(0).get("stateId") == null ? "" : list.get(0).get("stateId").toString();//国家id
                String cityId = list.get(0).get("cityId") == null ? "" : list.get(0).get("cityId").toString();//城市id
                params.put("state", stateId);//国家
                params.put("city", cityId);//城市
            }
            params.put("com_code", com_code);//商品代码
            params.put("com_address", address);//详细地址
            params.put("parentid", 0);//父指针id
            params.put("com_name", com_name);//机场名称
            params.put("com_best", 120);//最佳游玩时长
            params.put("com_entrance", com_entrance);//出口坐标
            params.put("com_exit", com_exit);//入口坐标
            params.put("com_duplex", com_duplex);//出入口坐标
            params.put("com_type", 3);//商品类型(吃住行游娱购)
            params.put("mer_code", CommodityConstant.MER_CODE);//商家编码
            params.put("mer_agency", CommodityConstant.AGENCY_CODE);//代理编码
            int res = baiduMapInfoService.addAirportInfo(params);
            if (res != 0) {
                result.put("status", "success");
                result.put("msg", "操作成功");
            } else {
                result.put("status", "error");
                result.put("msg", "操作失败");
            }
        }
        return result;
    }


    /**
     * @return com.alibaba.fastjson.JSONObject
     * @Author Breach
     * @Description 根据全球机场三字码获取全球国家名称
     * @Date 2018/12/14
     * @Param path
     */
    public JSONObject getGlobalNationInfo(String path) {
        JSONObject jo = new JSONObject();
        Sheet sheet = ReadExcel.readExcelInfo(path);
        int rowNums = sheet.getPhysicalNumberOfRows();//获取当前sheet的总行数
        for (int i = 2; i < rowNums - 1; i++) {//从第二行开始读 （标题栏数据不读）
            Row row = sheet.getRow(i);
            Map<String, Object> para = new HashMap<>();
            String com_code = row.getCell(0).getStringCellValue();//商品编码(三字码)
            String state = row.getCell(3).getStringCellValue();//国家
            jo.put("" + com_code, state);
        }
        System.out.println(jo);
        return jo;
    }

    /**
     * @param query
     * @return com.alibaba.fastjson.JSONObject
     * @Author Breach
     * @Description 获取百度地图坐标
     * @Date 2018/12/13
     * @Param region
     */
    public JSONArray getParkInfoUrl(String region, String query) {
        Map<String, Object> param = new HashMap<>();
        JSONObject jo = new JSONObject();

        param.put("scope", 2);
        param.put("page_size", 1);
        param.put("page_num", 0);
        param.put("region", region);//城市名称
        param.put("query", query);//机场名称
        String url = MapResultUtils.getRequestUrl(param);//单个请求地址
        System.out.println(url);
        jo = RequestUtils.sendPost(url);
        JSONArray results = JSONArray.parseArray(jo.get("results").toString());
        return results;
    }

    /**
     * @param query 机场名称
     * @return java.lang.String
     * @Author Breach
     * @Description
     * @Date 2018/12/13
     * @Param region 国家名称
     */
    public Map<String, Object> getGoogMapInfo(String region, String query) {
        JSONArray results = null;
        JSONObject tempJo = new JSONObject();
        JSONObject jo = null;

        JSONObject geometry = null;
//        JSONArray address_components = null;
        StringBuffer buffer = null;
        InputStream in = null;
        String navi_location = "";
        Map<String, Object> para = new HashMap<>();
        String requestUrl = MapConstant.GOOGLE_PREFIX_URL + MapConstant.JSON_URL + region + query
                + MapConstant.GOOGLE_SUFFIX_URL + MapConstant.API_KEY;//请求地址
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            System.out.println(url);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            //设置当前实例使用SSLSocketFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();

            in = conn.getInputStream();
            //读取服务端返回的内容
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf8"));
            buffer = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString());
            jo = JSONObject.parseObject(buffer.toString());
            String status = jo.get("status").toString();//状态码
            results = JSONArray.parseArray(jo.get("results").toString());
            System.out.println(jo);
            System.out.println("开始");
            System.out.println(results);
            if (!results.isEmpty() && results != null) {
                for (int i = 0; i < results.size(); i++) {
                    JSONObject jot = JSONObject.parseObject(results.get(i).toString());
                    if (jot.containsKey("geometry")) {
                        geometry = JSONObject.parseObject(jot.get("geometry").toString());
                    } else {
                        geometry = JSONObject.parseObject("");
                    }/*
                    if(jot.containsKey("address_components")) {
                        address_components = JSONArray.parseArray(jot.get("address_components").toString());
                    } else {
                        address_components = JSONArray.parseArray("");
                    }*/
                    String formatted_address = jot.get("formatted_address").toString();//地点位置
                    JSONObject loca = JSONObject.parseObject(geometry.get("location").toString());//坐标
                    BigDecimal lat = new BigDecimal(loca.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                    BigDecimal lng = new BigDecimal(loca.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                    navi_location = lat + "," + lng;//出入口坐标
                    para.put("address", formatted_address);
                    para.put("com_duplex", navi_location);
                    System.out.println(para);
                }
            } else {
                para.put("address", "");
                para.put("com_duplex", "");
            }
            System.out.println("结束");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return para;
    }

    public static void main(String[] args) {
        String path = "F:\\district\\airplanecode.xls";
        JSONObject jo = new AirportInfoController().getGlobalNationInfo(path);
        /*String region = "阿克苏";
        String query = "阿克苏机场";
        JSONArray results = new AirportInfoController().getParkInfoUrl(region, query);
        System.out.println(results);
        JSONObject jo = JSONObject.parseObject(results.get(0).toString());
        if(jo.containsKey("detail_info")) {
            System.out.println(JSONObject.parseObject(jo.get("detail_info").toString()).get("navi_location"));
            JSONObject navi_loca = JSONObject.parseObject(JSONObject.parseObject(jo.get("detail_info").toString()).get("navi_location").toString());
            String com_duplex = navi_loca.get("lng") + "," + navi_loca.get("lat");
            System.out.println("出入口坐标：" + com_duplex);
            BigDecimal lng = new BigDecimal(navi_loca.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
            BigDecimal lat = new BigDecimal(navi_loca.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
            System.out.println(lng + "," + lat);
            if(JSONObject.parseObject(jo.get("detail_info").toString()).containsKey("children")) {
                JSONArray jas = new JSONArray();
                jas = JSONArray.parseArray(JSONObject.parseObject(jo.get("detail_info").toString()).get("children").toString());
                for (int j = 0; j < jas.size(); j++) {
                    JSONObject childrenJo = JSONObject.parseObject(jas.get(j).toString());
                    String tag = childrenJo.get("tag").toString();
                    JSONObject locaJo = JSONObject.parseObject(childrenJo.get("location").toString());

                    if(tag.trim().contains("入口")) {
                        String com_entrance = locaJo.get("lng") + "" + locaJo.get("lat");//入口坐标
                        System.out.println("入口坐标：" + com_entrance);
                    } else if(tag.trim().contains("出口")) {
                        String com_exit = locaJo.get("lng") + "" + locaJo.get("lat");//出口坐标
                        System.out.println("出口坐标：" + com_exit);
                    }
                }
            }
        }*/
    }
}
