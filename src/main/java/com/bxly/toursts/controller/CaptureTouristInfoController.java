package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;

/**
 * @Description 使用Jsoup方式请求解析HTML文档,详情见：https://www.cnblogs.com/zhangyinhua/p/8037599.html
 * @Author Breach
 */
public class CaptureTouristInfoController {

    /**
      * @Author Breach
      * @Description 请求地址获取页面数据
      * @Date 2018/11/13
      * @Param url 请求地址
      * @return com.alibaba.fastjson.JSONArray
      */
    public static JSONArray getDocInfo(String url) {
        JSONArray ja = new JSONArray();
//        url = "https://lvyou.baidu.com/guangdong";//请求网站地址
        String photos = "";
        Document doc;
        {
            try {
                doc = Jsoup.connect(url).get();//从网站上解析HTML文档
                System.out.println(doc.body().select("script").last().data());
//                System.out.println(doc.getElementById("content"));
                System.out.println("*************** 分割线 *****************");
                /*//获取所有图片地址
                Elements photoE = doc.body().getElementById("J_pic-slider").getElementsByTag("li").select("img[src$=.jpg]");
                int photoNum = photoE.size();
                System.out.println("图片个数为：" + photoNum);
                for (int i = 0; i < photoNum; i++) {
                    String photoSrc = photoE.get(i).attr("abs:src");//获取单个图片地址
                    photoSrc = photoSrc.substring(photoSrc.indexOf("src=") + 4, photoSrc.length());//截取图片地址
                    if(i != photoNum - 1) {
                        photos += photoSrc + ",";
                    } else {
                        photos += photoSrc;//拼接图片地址成字符串
                    }
                }
                System.out.println(photos);
//                System.out.println(doc.body().getElementById("J_pic-slider").getElementsByTag("li").select("img[src$=.jpg]").get(0).attr("abs:src"));
                System.err.println(doc.body().getElementsByClass("main-info-wrap").select(".remark-count").remove());
                String rating_acore = doc.body().getElementsByClass("main-info-wrap").select(".main-score").text().trim();//评分
                System.out.println(rating_acore);//获取评分
                String name = doc.body().getElementsByClass("main-name").text().trim() + "省";//名称
                System.out.println(name);
                String detail = doc.body().getElementsByClass("main-desc-p").text().trim();//简介
                System.out.println(detail);
                String best_season = doc.body().getElementsByClass("main-besttime").text().trim();//最佳季节
                System.out.println(best_season);
                String days = doc.body().getElementsByClass("main-dcnt").text().trim();//建议游玩天数
                System.out.println(days);
                ja.fluentAdd(0, name);//名称（城市、景点）
                ja.fluentAdd(1, detail);//简介
                ja.fluentAdd(2, best_season);//最佳游玩季节
                ja.fluentAdd(3, days);//建议游玩天数
                ja.fluentAdd(4, rating_acore);//评分
                ja.fluentAdd(5, photos);//景点图片地址*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ja;
    }

    /**
      * @Author Breach
      * @Description 向Excel表格中写入数据
      * @Date 2018/11/13
      * @Param excelPath 要写入的excel表格的本地路径
      * @param ja 要写入的数据
      * @return void
      */
    public static void writeDataToExcel(String excelPath, JSONArray ja, int index) {
        XSSFWorkbook wb = null;
        FileInputStream in = null;
        FileOutputStream out = null;
        XSSFSheet sheet = null;
        try {
            File file  = new File(excelPath);
            if(!file.exists()) {
                file.createNewFile();//如果文件不存在，则创建文件
                in = new FileInputStream(file);
                wb = new XSSFWorkbook();
                sheet = wb.createSheet("tourist");
            } else {
                wb = new XSSFWorkbook(new FileInputStream(excelPath));
                sheet = wb.getSheet("tourist");//获取指定名称的页脚sheet
            }
            XSSFFont font = wb.createFont();//设置字体
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//字体加粗
            font.setFontName("仿宋_GB2312");
//            font.setFontHeightInPoints((short) 14);

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
            style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//设置单元格颜色
            style.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);
            style.setBorderLeft(XSSFCellStyle.BORDER_THIN);//设置左边框
            style.setFont(font);//设置字体style

            XSSFCellStyle style1 = wb.createCellStyle();
//            style1.setWrapText(true);//设置单元格自动换行
            style1.setShrinkToFit(true);//设置单元格缩进（超出部分不显示）
            for (int j = 0; j < 2; j++) {
                XSSFRow row = sheet.createRow(index);//创建行
                for(int i = 0; i < ja.size(); i++) {
                    Cell cell = row.getCell(i);//获取指定行下的指定列
                    if(cell ==null) {//如果列为空，则创建指定列
                        cell = row.createCell(i);
                    }
                    if(j == 0) {
                        JSONArray jas = getExcelHeading();
                        cell.setCellValue(jas.get(i).toString());
                        sheet.setColumnWidth(i, jas.get(i).toString().getBytes().length * 2 * 256);//根据单元格元素的字节长度设置单元格列宽
                        cell.setCellStyle(style);
                    } else {
                        cell.setCellValue(ja.get(i).toString());//向指定单元格中写入相应数据
                        cell.setCellStyle(style1);
                    }
                }
            }
            out = new FileOutputStream(excelPath);
            wb.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(wb != null) {
                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
      * @Author Breach
      * @Description 设置excel表头字段
      * @Date 2018/11/13
      * @Param
      * @return com.alibaba.fastjson.JSONObject
      */
    public static JSONArray getExcelHeading() {
        JSONArray jo = new JSONArray();
        jo.fluentAdd(0, "名称");
        jo.fluentAdd(1, "简介");
        jo.fluentAdd(2, "最佳游玩季节");
        jo.fluentAdd(3, "建议游玩天数");
        jo.fluentAdd(4, "旅游地评分");
        jo.fluentAdd(5, "景点图片地址");

        return jo;
    }

    public static void getPageInfo(String url) {
        WebClient browser = new WebClient();//创建一个浏览器
        browser.getOptions().setCssEnabled(false);//设置不加载css
        browser.getOptions().setJavaScriptEnabled(true);//设置加载js
        browser.getOptions().setThrowExceptionOnScriptError(false);//设置js运行报错时不抛出异常
        browser.getOptions().setUseInsecureSSL(true);

        try {
            HtmlPage page = browser.getPage(url);
            //等待js加载完成
            browser.waitForBackgroundJavaScript(1000);
//            Document doc = Jsoup.parse(page.asText());
            System.out.println(page);
            browser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//        int index = 3;//控制从单元格的第几行写入数据
//        String url = "https://lvyou.baidu.com/guangdong";//请求地址
        String url = "https://lvyou.baidu.com/guangzhou";//请求地址
//        String excelPath = "F:\\common\\tourist.xls";//excel表格本地路径
        JSONArray ja = getDocInfo(url);
//        System.out.println(ja);
//        writeDataToExcel(excelPath, ja, index);
        getPageInfo(url);
    }
}
