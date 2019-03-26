package com.bxly.toursts.controller;

import com.bxly.toursts.common.StringRandom;
import com.bxly.toursts.service.BaiduMapInfoService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bxly.toursts.common.ReadExcel.readExcelInfo;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/18
 * @Version V1.0
 **/
@RestController
@RequestMapping("/bx")
public class RailwayInfoController {
    @Autowired
    BaiduMapInfoService baiduMapInfoService;

    @RequestMapping("/addRailwayInfo")
    public Map<String, Object> addRailwayInfos() {
        Sheet sheet;
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> railInfoList = new ArrayList<>();//需要存放的数据集合
        String filePath = "F:\\district\\railwayinfo.xls";
        sheet = readExcelInfo(filePath);
        int rowNums = sheet.getPhysicalNumberOfRows();//获取当前sheet的总行数
        for (int i = 1; i < rowNums; i++) {//从第二行开始读 （标题栏数据不读）
            Map<String, Object> para = new HashMap<>();
            Row row = sheet.getRow(i);
            String com_code = new StringRandom().getStringRandom(8);//获取八位随机商品编码
            String com_name = row.getCell(0).getStringCellValue();//火车站名称
            String com_address = row.getCell(1).getStringCellValue();//详细地址
            String lng = row.getCell(11).toString();//坐标（经度）
            String lat = row.getCell(12).toString();//坐标（纬度）
            BigDecimal lngBd = new BigDecimal(lng).setScale(6, BigDecimal.ROUND_HALF_UP);
            BigDecimal latBd = new BigDecimal(lat).setScale(6, BigDecimal.ROUND_HALF_UP);
            String com_duplex = lngBd + "," + latBd;//出入口坐标
            String state = "33";//中国
            int com_type = 3;//商品类型（吃住行...）
            int com_best = 120;//最佳游玩时长
            String province = row.getCell(5).getStringCellValue();//省
            String city = row.getCell(6).getStringCellValue();//市区县
            para.put("province", province+"%");
            para.put("city", city+"%");
            System.out.println(para);
            //获取省id和城市id
            List<Map<String, Object>> list = baiduMapInfoService.getProvinceAndCityInfo(para);
            Map<String, Object> params = new HashMap<>();
            System.out.println("list = " + list);
            if (!list.isEmpty() && list != null && list.size() > 0) {
                String provinceId = list.get(0).get("povinceId") == null ? "" : list.get(0).get("povinceId").toString();//省
                String cityId = list.get(0).get("cityId") == null ? "" : list.get(0).get("cityId").toString();//市县
                params.put("province", provinceId);//国家
                params.put("city", cityId);//城市
            }
            int parentid = 0;//父指针
            params.put("com_code", com_code);
            params.put("com_name", com_name);
            params.put("com_address", com_address);
            params.put("com_duplex", com_duplex);
            params.put("state", state);
            params.put("com_type", com_type);
            params.put("com_best", com_best);
            params.put("parentid", parentid);
            railInfoList.add(params);
        }
        int num = baiduMapInfoService.addRailwayInfos(railInfoList);//插入到数据库
        if(num != 0) {
            result.put("status", "success");
        } else {
            result.put("status", "error");
        }
        return result;
    }
}
