package com.bxly.toursts.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    public static int num = 0;
    public static void testWriteToExcel(String filePath, JSONObject ja) {
        try {
            File file = new File(filePath);
//            XSSFWorkbook wb = null;
            Workbook wb = null;
            Sheet sheet = null;
            if (file.isFile() && file.exists()) {
                System.out.println("读取");
                FileInputStream in = new FileInputStream(filePath);
//                wb = new XSSFWorkbook(in);
                wb = WorkbookFactory.create(in);
                sheet = wb.getSheetAt(0);
            } else {
                System.out.println("创建");
//                wb = new XSSFWorkbook();
//                sheet = wb.createSheet("公园出入口");
            }
//            HSSFCellStyle style = wb.createCellStyle();
//            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//设置单元格样式
//            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//设置单元格背景色

            if (!ja.isEmpty()) {
                JSONArray results = JSONArray.parseArray(ja.get("results").toString());
//                System.out.println(results);
                if (!results.isEmpty()) {
                    String city = JSONObject.parseObject(results.get(0).toString()).getString("city");
                    String parkname = JSONObject.parseObject(results.get(0).toString()).getString("name");
                    String parentUid = JSONObject.parseObject(results.get(0).toString()).getString("uid");
                    if (JSONObject.parseObject(results.get(0).toString()).containsKey("detail_info")) {
                        JSONArray children = JSONArray.parseArray(JSONObject.parseObject(JSONObject.parseObject(results.get(0).toString()).get("detail_info").toString()).get("children").toString());
                        System.out.println(children);
                        for (int i = 0; i < children.size(); i++) {
                            Row row = sheet.getRow(num); //获取指定行
                            if (row == null) {
                                row = sheet.createRow(num);
                            }
                            int lastColNum = 0;
                            if (String.valueOf(row.getLastCellNum()) != null) {
                                lastColNum = row.getLastCellNum();
                            }
                            if (lastColNum == -1) {
                                lastColNum = 0;
                            }
                            if (row == null) {
                                row = sheet.createRow(0);//创建指定行
                            }
                            JSONArray tempJa = new JSONArray();
                            JSONObject child = JSONObject.parseObject(children.get(i).toString());
                            JSONObject location = JSONObject.parseObject(child.get("location").toString());
                            tempJa.fluentAdd(city + parkname);//区域名称
                            tempJa.fluentAdd(parentUid);//父类uid
                            tempJa.fluentAdd(child.get("name"));//出入口名称
                            tempJa.fluentAdd(child.get("address"));//出入口地址
                            tempJa.fluentAdd(child.get("uid"));//出入口uid
                            tempJa.fluentAdd(child.get("show_name"));//出入口展示名称
                            tempJa.fluentAdd(location.get("lng") + "," + location.get("lat"));//出入口坐标
                            tempJa.fluentAdd(child.get("tag"));//标签
                            for (int j = 0; j < 8; j++) {
//                                Cell cell = row.getCell(lastColNum + j);//获取指定列
                                Cell cell = row.getCell(j);//获取指定列
//                        Cell cell = row.getCell(j);//获取指定列
                                if (cell == null) {
//                                    cell = row.createCell(lastColNum + j);//创建指定列
                                    cell = row.createCell(j);//创建指定列
//                            cell = row.createCell(j);//创建指定列
                                }
                                /*if (i == children.size() - 1 && j == 5) {
                                    cell.setCellValue(tempJa.get(j).toString());
                                    cell = row.createCell(lastColNum + j + 1);
                                    cell.setCellValue(tempJa.get(6).toString());
                                } else {
                                    cell.setCellValue(tempJa.get(j).toString());
                                }*/
                                cell.setCellValue(tempJa.get(j).toString());
//                        cell.setCellValue(tempJa.get(j).toString());
                                if (tempJa.get(j).toString().length() < 7) {
                                    sheet.setColumnWidth(lastColNum + j, 1024 * 6);
                                } else {
                                    sheet.autoSizeColumn(lastColNum + j, true);//设置单元格宽度
                                }
                            }
                            num++;
                        }
                    }
                }

            } else {
                System.out.println("无数据，跳过");
            }

            FileOutputStream out = new FileOutputStream(filePath);//根据excel路径filePath创建excel表格
            wb.write(out);//写入数据
            out.close();
            System.out.println("Excel表格数据终止于" + num + "行。");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        String  filePath = "E:\\全国地点详情信息-旅游景点.xlsx";
        String filePath = "E:\\parkDetail.xlsx";
//        testWriteToExcel(filePath);
    }
}

  