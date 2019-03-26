package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bxly.toursts.common.CommodityConstant;
import com.bxly.toursts.common.MapConstant;
import com.bxly.toursts.service.BaiduMapInfoService;
import com.bxly.toursts.utils.ReadTextUtils;
import com.bxly.toursts.utils.RequestUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.transaction.Transactional;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description 读取excel表格内容
 * @Author Breach
 * @Date 2018/11/26
 * @Version V1.0
 **/
@RestController
@Log4j2
@RequestMapping("/bxly")
public class ReadExcelINfo {
    @Resource
    BaiduMapInfoService baiduMapInfoService;

    @RequestMapping("/addTouristInfos")
    @Transactional
    public void readExcelInfos() {
//        File file = new File("F:\\伴行\\全国行政区划1.xlsx");
        File file = new File("F:\\伴行\\全球国家城市1.xls");
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Workbook wb;
        {
            try {
                wb = WorkbookFactory.create(new FileInputStream(file));
                Sheet sheet = wb.getSheetAt(0);
                int rowNums = sheet.getPhysicalNumberOfRows();//获取当前sheet的总行数
                String com_level = "";
                //获取第一行的列数
//                int colNums = sheet.getRow(0).getLastCellNum();
                /*for (int j = 0; j < rowNums; j++) { //录入国内行政区划信息
                    Map<String, Object> para = new HashMap<>();
                    Row row = sheet.getRow(j);
                    row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    String str = row.getCell(0).getStringCellValue();
                    String area_name = row.getCell(1).getStringCellValue();
                    System.out.println(str);
                    if (str.startsWith("00")) {
                        para.put("parent_id", 0);
                        para.put("com_level", 0);
                    } else {
                        com_level = str;
                        if (!str.startsWith("00") && str.endsWith("0000")) {
                            str = "0";
                        } else if (!str.startsWith("00") && !str.endsWith("0000")) {
                            str = str.substring(0, 2) + "0000";
                        }
                        int parent_id = baiduMapInfoService.findParentById(Integer.parseInt(str));//查询父类id
                        if (parent_id != -1) {
                            para.put("parent_id", parent_id);//父类id
                        } else {
                            para.put("parent_id", 33);
                        }
                        para.put("com_level", com_level);
                    }
                    para.put("com_level", Integer.parseInt(com_level));//层级（暂时插入地区代码）
                    para.put("area_name", area_name);//地区名称
                    String com_central = getLocationInfo(area_name);//中心点坐标
                    para.put("com_central", com_central);

                    para.put("com_best", 2880);
                    para.put("com_shortest", 240);
                    para.put("com_longest", 4320);
                    para.put("returnto", 1);
                    para.put("destination", 1);
                    para.put("departure", 1);
//                    list.add(para);
                    boolean bl = baiduMapInfoService.addTouristInfos(para);//向bx_region表中插入数据
                    if (bl) {
                        result.put("status", "success");
                        result.put("msg", "添加成功");
                    } else {
                        result.put("status", "error");
                        result.put("msg", "添加失败");
                    }
                }*/

                for (int i = 0; i < rowNums; i++) { //录入全球行政区划信息
                    Map<String, Object> para = new HashMap<>();
                    Row row = sheet.getRow(i);
                    for(int j = 0; j < 4; j++) {
                        row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    }
                    String str = row.getCell(0).getStringCellValue();
                    String area_name = row.getCell(1).getStringCellValue();//国家城市名称
                    String relation = row.getCell(3).getStringCellValue();//关联父级
                    System.out.println(area_name);
                    if("0".equals(relation)) {
                        para.put("parent_id", 0);
                    } else {
                        int parentId = baiduMapInfoService.findParentIds(Integer.parseInt(relation));//查找父级id
                        para.put("parent_id", parentId);
                    }

                    para.put("com_level", Integer.parseInt(str));
                    para.put("area_name", area_name);//地区名称
//                    String com_central = getLocationInfo(area_name);//中心点坐标
                    para.put("com_central", "");//google地图位置坐标

                    para.put("com_best", 2880);
                    para.put("com_shortest", 240);
                    para.put("com_longest", 4320);
                    para.put("returnto", 1);
                    para.put("destination", 1);
                    para.put("departure", 1);

                    boolean bl = baiduMapInfoService.addTouristInfos(para);//向bx_region表中插入数据
                    if (bl) {
                        result.put("status", "success");
                        result.put("msg", "添加成功");
                    } else {
                        result.put("status", "error");
                        result.put("msg", "添加失败");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
        }
    }

    /**
      * @Author Breach
      * @Description 获取百度/谷歌地图位置坐标
      * @Date 2018/11/27
      * @Param query
      * @return java.lang.String
      */
    public static String getLocationInfo(String query) {

        JSONArray results = null;
        JSONObject jo = null;
        String location = "";
        //baidu请求地址
        String requestUrl = MapConstant.BD_PREFIX_URL + MapConstant.BD_SUFFIX_URL
                + MapConstant.BD_QUERY + query + MapConstant.BD_REGION + query
                + MapConstant.BD_OUTPUT_AND_AK + MapConstant.BROWSER_AK;

        /*String googleUrl = MapConstant.GOOGLE_PREFIX_URL + MapConstant.JSON_URL + query //google请求地址
                + MapConstant.GOOGLE_SUFFIX_URL + MapConstant.API_KEY;*/
        System.out.println(requestUrl);
        jo = RequestUtils.sendPost(requestUrl);
        JSONObject locaJa = JSONObject.parseObject(JSONObject.parseObject(JSONArray.parseArray(jo.get("results").toString()).get(0).toString()).get("location").toString());
        location = locaJa.get("lng") + "," + locaJa.get("lat");
        return location;
    }

    /**
      * @Author Breach
      * @Description 向bx_commodity商品表中插入商品数据
      * @Date 2018/11/27
      * @Param
      * @return void
      */
    @RequestMapping("/addProductInfos")
    @Transactional
    public Map<String, Object> readExcelInfosWriteToTable() {
        File file = new File("F:\\伴行\\全国行政区划2.xlsx");
        Map<String, Object> result = new HashMap<>();
        Workbook wb;
        int nationid = 0;
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        {
            try {
                wb = WorkbookFactory.create(new FileInputStream(file));
                Sheet sheet = wb.getSheetAt(0);
                int rowNums = sheet.getPhysicalNumberOfRows();//获取当前sheet的总行数
                for (int j = 0; j < rowNums; j++) {
                    Map<String, Object> para = new HashMap<>();
                    Row row = sheet.getRow(j);
                    row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    String str = row.getCell(0).getStringCellValue();
                    String com_name = row.getCell(1).getStringCellValue();
                    System.out.println(str);
                    if (str.startsWith("00")) {
                        para.put("parentid", 0);
                        para.put("com_code", CommodityConstant.NATION_CODE + "000000");//商品编码
                    } else {
                        String nationCode = CommodityConstant.NATION_CODE + "000000";//中国编码
                        nationid = baiduMapInfoService.findParentCode(nationCode);
                        if (!str.startsWith("00") && str.endsWith("0000")) {
                            para.put("com_code", CommodityConstant.NATION_CODE + str);//商品编码
                            para.put("parentid", nationid);
                        } else if (!str.startsWith("00") && !str.endsWith("0000")) {
                            str = CommodityConstant.NATION_CODE + str.substring(0, 2) + "0000";
                            int parentid = baiduMapInfoService.findParentByCode(str);//查询父类id
                            if (parentid != -1) {
                                para.put("parentid", parentid);//父类id
                            } else {
                                para.put("parentid", nationid);//中国id
                            }
                        }
                    }
                    para.put("com_name", com_name);//商品名称
                    String com_central = getLocationInfo(com_name);//中心点坐标
                    para.put("com_central", com_central);//中心点坐标

                    para.put("mer_code", CommodityConstant.MER_CODE);//商家编码
                    para.put("mer_agency", CommodityConstant.AGENCY_CODE);//代理编码
                    para.put("com_passport", CommodityConstant.NATION_CODE);//国家编码
                    para.put("bx_op_deptid", CommodityConstant.OP_DEPT_ID);//定制游运营部id
                    para.put("op_deptName", CommodityConstant.OP_DEPT_NAME);//运营部名称
                    para.put("com_best", 600);//最佳时间
                    para.put("com_shortest", 600);//最短游玩时间
                    para.put("com_longest", 800);//最长游玩时间
                    para.put("com_type", 4);//商品类型，4：旅游
                    para.put("com_level", CommodityConstant.COM_LEVEL);//商品层级

                    para.put("com_begining", (Date)sf.parseObject("00:00:00"));
                    para.put("com_moment", (Date)sf.parse("23:59:00"));

                    boolean bl = baiduMapInfoService.addTouristProductsInfos(para);//向bx_Community商品表中插入商品数据
                    if (bl) {
                        result.put("status", "success");
                        result.put("msg", "添加成功");
                    } else {
                        result.put("status", "error");
                        result.put("msg", "添加失败");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static void main(String[] args) {
//        new ReadExcelINfo().readExcelInfos();
        JSONArray ja = ReadTextUtils.readText("F:\\district\\next\\products.txt");
        for (int i = 0; i < ja.size(); i++) {
            String query = ja.getString(i);
            String localtion = getLocationInfo(query);
            System.out.println(localtion);
        }
    }

}
