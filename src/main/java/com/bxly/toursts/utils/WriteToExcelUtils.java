package com.bxly.toursts.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WriteToExcelUtils {
    public static int index = 0;
    //excel列表头字段
    private static final String[] strs = {"province", "city", "area", "name", "location", "navi_location", "address", "uid", "street_id"
            , "telephone", "type", "tag", "price", "shop_hours", "overall_rating", "taste_rating", "service_rating", "environment_rating"
            , "facility_rating", "hygiene_rating", "technology_rating", "groupon_num", "discount_num", "image_num", "comment_num"
            , "favorite_num", "checkin_num", "detail_url"};

    /**
     * @param textPath 读取文本文件路径
     * @return void
     * @Author Breach
     * @Description 向excel中写入数据（坐标）
     * @Date 2018/11/2
     * @Param filePath 要写入数据的excel表格的路径
     */
    public static void writeToExcel(String filePath, String textPath) {
//        filePath = "F:\\xlsx\\全国行政区划.xls";
//        textPath = "F:\\district\\district1.txt";
//        int index = 0;
        XSSFSheet sheet = null;
        Map<String, Object> param = new HashMap<>();
        JSONArray ja = ReadTextUtils.readText(textPath);
        try {
            FileInputStream fs = new FileInputStream(filePath);
//            POIFSFileSystem ps = new POIFSFileSystem(fs);//使用poi提供的方法获取excel表信息
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            sheet = wb.getSheetAt(0);//获取到工作表
            XSSFRow row = sheet.getRow(0);//获取excel表格第一行
            int trLength = sheet.getLastRowNum();//获取单元格总行数
            int tdLength = row.getLastCellNum();//获取excel表格总列数
            //分别获取最后一行的行号，以及一行记录中的最后一条数据的单元格号
            System.out.println("trLength= " + trLength);
            System.out.println("tdLength= " + tdLength);
//            XSSFCell cell = row.getCell((short) 3);//获取excel表格第3格
//            XSSFCellStyle xcs = cell.getCellStyle();//得到单元格样式
            for (int i = 2; i < trLength + 1; i++) {//行
                Row row1 = sheet.getRow(i);//得到excel表格的指定行
                for (int j = 2; j < 4; j++) {//列
                    sheet.setColumnWidth(j, (short) 7 * 1024);//设置单元格宽度
                    Cell cell1 = row1.getCell(j);//得到excel表格指定行及指定列的单元格
//                    System.out.println(i + "：" + cell1 + " | " + j);
//                    System.out.println(cell1 == null);
                    for (int k = 0; k < 1; k++) {
                        String jo = ja.get(index).toString();
                        param.put("query", jo);
                        param.put("region", jo);
                        JSONArray jot = MapResultUtils.getMapResult(param);
                        String uid = JSONObject.parseObject(jot.get(0).toString()).get("uid").toString();
                        String coord = JSONObject.parseObject(jot.get(0).toString()).get("coord").toString();
                        if (j == 2) {
                            if (cell1 == null) {
                                cell1 = row1.createCell(j);
                                cell1.setCellValue(uid);
                                System.out.println(i + " | " + jo + " | uid | " + uid);
                            } else {
                                System.out.println(i + " | " + jo + " | uid | " + uid);
                                cell1.setCellValue(uid);
                            }
                        } else if (j == 3) {
                            if (cell1 == null) {
                                cell1 = row1.createCell(j);
                                cell1.setCellValue(coord);
                                System.out.println(i + " | " + jo + " | coord | " + coord);
                            } else {
                                cell1.setCellValue(coord);
                                System.out.println(i + " | " + jo + " | coord | " + coord);
                            }
                        }
                    }
                }
                index += 1;
                if (i - 2 == ja.size() - 1) {
                    break;
                }
            }

            FileOutputStream out = new FileOutputStream(filePath);
            wb.write(out);//向对应地址下的表格中写入数据
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param textPath 读取的txt文本文件的路径
     * @param region   城市搜索条件（如：北京市）
     * @param tag      配合query搜索条件的标签条件（如：美食、酒店等）
     * @param num      列总数
     * @param strs     根据不同tag查询时所返回数据中的数据字段
     * @param tempNum  表格中坐标字段值所在的列数（表格列数起始值从0开始）
     * @return void
     * @Author Breach
     * @Description 写入excel表格数据公共类
     * @Date 2018/11/5
     * @Param filePath 要写入的excel表格的路径
     */
    public static void writeToExcelCommon(String filePath, String textPath, String region
            , String tag, String[] strs, int num, int tempNum, int tempNum1, int tempNum2) {
        FileOutputStream out = null;
        Workbook wb;
        try {
            Map<String, Object> param = new HashMap<>();
            FileInputStream fs = new FileInputStream(filePath);
            JSONArray jas = new JSONArray();
            JSONArray ja = ReadTextUtils.readText(textPath);
//            XSSFWorkbook wb = new XSSFWorkbook(fs);
            wb = WorkbookFactory.create(fs);
            Sheet sheet = wb.getSheetAt(0);//获取到工作表
            int trLength = sheet.getLastRowNum();//获取单元格总行数
            System.out.println(new File(filePath).getName() + "表格的单元格总行数为：" + trLength);

            param.put("scope", 2);
            param.put("page_size", 20);//一页查询的数据条数
            param.put("region", region);//北京市、广州市
            param.put("tag", tag);//酒店
            for (int j = 0; j < ja.size(); j++) {//二级分类列表字段集合
                param.put("query", ja.get(j).toString());
                for (int i = 0; i < 20; i++) {
                    param.put("page_num", i);//第几页（页数从0开始）
                    jas = MapResultUtils.getMapResult(param);
                    System.out.println("jas == null：" + (jas == null));
                    if (jas != null && !jas.isEmpty()) {//根据获取的数据是否为空，设置跳出循环
                        for (int l = 0; l < jas.size(); l++) {//此处根据一页读取的条数设置遍历


                            /*sheet.autoSizeColumn(index, true);//设置excel单元格宽度自适应
                            Row row = sheet.getRow(index + 2);//得到excel表格的指定行
                            System.out.println(jas.get(l).toString());
                            for (int k = 0; k < num; k++) {
                                if (row == null) {
                                    row = sheet.createRow(index + 2);
                                    System.out.println("创建指定行：" + row);
                                }
                                Cell cell = row.getCell(k);//获取指定列
                                if (cell == null) {
                                    cell = row.createCell(k);
                                }

                                if (k == tempNum) {
                                    JSONObject location = JSONObject.parseObject(JSONObject.parseObject(jas.get(l).toString()).get(strs[4]).toString());
                                    String str = location.get("lng") + "," + location.get("lat");
                                    cell.setCellValue(str);
                                } else if (k == tempNum1 || k >= tempNum2) {
                                    JSONObject detail_info = JSONObject.parseObject(JSONObject.parseObject(jas.get(l).toString()).get("detail_info").toString());
                                    String tempStr = "";
                                    if (k == tempNum1) {
                                        if (detail_info.containsKey(strs[5])) {
                                            JSONObject navi_location = JSONObject.parseObject(detail_info.get(strs[5]).toString());
                                            tempStr = new BigDecimal(navi_location.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP) + "," + new BigDecimal(navi_location.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                        } else {
                                            JSONObject location = JSONObject.parseObject(JSONObject.parseObject(jas.get(l).toString()).get(strs[4]).toString());
                                            tempStr = location.get("lng") + "," + location.get("lat");
                                        }
                                    } else if (k >= tempNum2) {
                                        if (detail_info.containsKey(strs[k])) {
                                            tempStr = detail_info.get(strs[k]).toString();
                                        }
                                    }
                                    cell.setCellValue(tempStr);
                                } else {
//                                    System.out.println(strs[k]);
//                                    System.out.println(jas.get(l).toString());
                                    if (JSONObject.parseObject(jas.get(l).toString()).containsKey(strs[k])) {
//                                        System.out.println(JSONObject.parseObject(jas.get(l).toString()).get(strs[k]).toString());
                                        cell.setCellValue(JSONObject.parseObject(jas.get(l).toString()).get(strs[k]).toString());
                                    } else {
                                        cell.setCellValue("");
                                }
                                }
                            }
                            index++;
                            System.out.println("index==" + index);*/
                        }
                    } else {
                        System.out.println("*********");
                        System.out.println("break");
                        System.out.println("*********");
                        break;
                    }
                }
//                System.out.println("*************");
//                System.out.println(region + " | " + ja.get(j).toString() + "：遍历完成");
//                System.out.println("*************");
            }
//            out = new FileOutputStream(filePath);
//            System.out.println("wb" + wb);
//            wb.write(out);//向对应地址下的表格中写入数据
//            out.close();
//            wb = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } /*finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    /**
     * @param textPath 读取的txt文本文件的路径
     * @param region   城市搜索条件（如：北京市）
     * @param tag      配合query搜索条件的标签条件（如：美食、酒店等）
     * @return void
     * @Author Breach
     * @Description 全国地点详情信息-美食
     * @Date 2018/11/5
     * @Param filePath 要写入的excel表格的路径
     */
    public static void writeToExcelCater(String filePath, String textPath, String region, String tag) {
        String[] caters = {"province", "city", "area", "name", "location", "navi_location", "address", "uid", "street_id"
                , "telephone", "type", "tag", "price", "overall_rating", "comment_num", "detail_url"};//美食
//        filePath = "E:\\xlsx\\全国行政区划.xls";
        try {
            int index = 0;
            Map<String, Object> param = new HashMap<>();
            FileInputStream fs = new FileInputStream(filePath);
            JSONArray jas = new JSONArray();
            JSONArray ja = ReadTextUtils.readText(textPath);
//            POIFSFileSystem ps = new POIFSFileSystem(fs);//使用poi提供的方法获取excel表信息
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);//获取到工作表
//            XSSFRow row = sheet.getRow(0);//获取excel表格第一行
//            int trLength = sheet.getLastRowNum();//获取单元格总行数
//            int tdLength = row.getLastCellNum();//获取excel表格总列数
            //分别获取最后一行的行号，以及一行记录中的最后一条数据的单元格号
            param.put("scope", 2);
            param.put("page_size", 1);
            param.put("region", region);//北京市
            param.put("tag", tag);//美食
            for (int j = 0; j < ja.size(); j++) {
                param.put("query", ja.get(j).toString());
                for (int i = 0; i < 400; i++) {
                    param.put("page_num", i);
                    jas = MapResultUtils.getMapResult(param);
                    Row row = sheet.getRow(index + 2);//得到excel表格的指定行
                    sheet.setColumnWidth(i, (short) 7 * 1024);//设置单元格宽度
                    for (int k = 0; k < 16; k++) {
                        Cell cell = row.getCell(k);//获取指定列
                        if (cell == null) {//如果指定列为null，则创建指定列
                            cell = row.createCell(k);
                        }
                        if (k == 4) {
                            JSONObject location = JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get(caters[4]).toString());
                            String str = location.get("lng") + "," + location.get("lat");
                            cell.setCellValue(str);
                        } else if (k == 5 || k == 10 || k > 11) {
                            JSONObject detail_info = JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get("detail_info").toString());
                            JSONObject navi_location = JSONObject.parseObject("");
                            String tempStr = "";
                            if (k == 5) {
                                if (detail_info.containsKey(caters[5])) {
                                    navi_location = JSONObject.parseObject(detail_info.get(caters[5]).toString());
                                    tempStr = new BigDecimal(navi_location.get("lng").toString()).setScale(6, BigDecimal.ROUND_HALF_UP) + "," + new BigDecimal(navi_location.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                } else {
                                    JSONObject location = JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get(caters[4]).toString());
                                    tempStr = location.get("lng") + "," + location.get("lat");
                                }
                            } else if (k == 10 || k > 11) {
                                if (detail_info.containsKey(caters[k])) {
                                    tempStr = detail_info.get(caters[k]).toString();
                                }
                            }
                            cell.setCellValue(tempStr);
                        } else if (k == 11) {
                            cell.setCellValue(JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get("detail_info").toString()).get(caters[11]).toString());
                        } else {
                            System.out.println(caters[k]);
                            System.out.println(jas.get(0).toString());
                            if (JSONObject.parseObject(jas.get(0).toString()).containsKey(caters[k])) {
                                System.out.println(JSONObject.parseObject(jas.get(0).toString()).get(caters[k]).toString());
                                cell.setCellValue(JSONObject.parseObject(jas.get(0).toString()).get(caters[k]).toString());
                            } else {
                                cell.setCellValue("");
                            }
                        }
                    }
                    index++;
                }
            }
            FileOutputStream out = new FileOutputStream(filePath);
            wb.write(out);//向对应地址下的表格中写入数据
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param textPath
     * @param region
     * @param tag
     * @return void
     * @Author Breach
     * @Description 全国地点详情信息-酒店
     * @Date 2018/11/5
     * @Param filePath
     */
    /*public static void writeToExcelHotel(String filePath, String textPath, String region, String tag) {
        String[] strs = {"province", "city", "area", "name", "location", "navi_location", "address", "uid"
                , "street_id", "telephone", "type", "tag", "price", "overall_rating", "service_rating", "facility_rating"
                , "hygiene_rating", "image_num", "comment_num", "favorite_num", "checkin_num", "detail_url"};//酒店
        writeToExcelCommon(filePath, textPath, region, tag, strs, 22, 4, 5, 10);
        *//*try {
            int index = 0;
            Map<String, Object> param = new HashMap<>();
            FileInputStream fs = new FileInputStream(filePath);
            JSONArray jas = new JSONArray();
            JSONArray ja = ReadTextUtils.readText(textPath);
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);//获取到工作表

            param.put("scope", 2);
            param.put("page_size", 1);
            param.put("region", region);//北京市
            param.put("tag", tag);//酒店
            for(int j = 0; j < ja.size(); j++) {
                param.put("query", ja.get(j).toString());
                for(int i = 0 ; i < 400; i++) {
                    param.put("page_num", i);
                    jas = MapResultUtils.getMapResult(param);
                    sheet.autoSizeColumn(i, true);//设置excel单元格宽度自适应
                    Row row = sheet.getRow(index + 2);//得到excel表格的指定行
                    for(int k = 0; k < 22; k++) {
                        Cell cell = row.getCell(k);//获取指定列
                        if(cell == null) {
                            cell = row.createCell(k);
                        }
                        if(k == 4) {
                            JSONObject location = JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get(caters[4]).toString());
                            String str = location.get("lng") + "," + location.get("lat");
                            cell.setCellValue(str);
                        } else if(k == 5 || k >= 10) {
                            JSONObject detail_info = JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get("detail_info").toString());
                            String tempStr = "";
                            if(k == 5) {
                                if(detail_info.containsKey(caters[5])) {
                                    JSONObject navi_location = JSONObject.parseObject(detail_info.get(caters[5]).toString());
                                    tempStr = new BigDecimal(navi_location.get("lng").toString()) .setScale(6, BigDecimal.ROUND_HALF_UP)+ "," + new BigDecimal(navi_location.get("lat").toString()).setScale(6, BigDecimal.ROUND_HALF_UP);
                                } else {
                                    JSONObject location = JSONObject.parseObject(JSONObject.parseObject(jas.get(0).toString()).get(caters[4]).toString());
                                    tempStr = location.get("lng") + "," + location.get("lat");
                                }
                            } else if(k >= 10) {
                                if(detail_info.containsKey(caters[k])) {
                                    tempStr = detail_info.get(caters[k]).toString();
                                }
                            }
                            cell.setCellValue(tempStr);
                        } else {
                            System.out.println(caters[k]);
                            System.out.println(jas.get(0).toString());
                            if(JSONObject.parseObject(jas.get(0).toString()).containsKey(caters[k])) {
                                System.out.println(JSONObject.parseObject(jas.get(0).toString()).get(caters[k]).toString());
                                cell.setCellValue(JSONObject.parseObject(jas.get(0).toString()).get(caters[k]).toString());
                            } else {
                                cell.setCellValue("");
                            }
                        }
                    }
                    index++;
                }
            }

            FileOutputStream out = new FileOutputStream(filePath);
            wb.write(out);//向对应地址下的表格中写入数据
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*//*
    }*/



    /**
     * @param textPath
     * @param region
     * @param tag
     * @return void
     * @Author Breach
     * @Description 全国地点详情信息-通用
     * @Date 2018/11/5
     * @Param filePath
     */
    public static void writeToExcel(String filePath, String textPath, String region, String tag) {
        String[] strs = WriteToExcelUtils.strs;
        writeToExcelCommon(filePath, textPath, region, tag, strs, 28, 4, 5, 10);
    }


}

  