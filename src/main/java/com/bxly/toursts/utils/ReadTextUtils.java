package com.bxly.toursts.utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ReadTextUtils {
    /**
     * @Author Breach
     * @Description 读txt文件
     * @Date 2018/11/1
     * @Param filePath
     * @return void
     */
    public static JSONArray readText(String filePath) {
        String text = null;
        JSONArray jr = new JSONArray();
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                while((text = br.readLine()) != null) {
                    text = text.trim();
                    jr.fluentAdd(text);
                }
                /*System.out.println(jr);
                System.out.println(jr.size());
                System.out.println(jr.get(0));*/
                br.close();

            } else {
                System.out.println("文件不存在");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jr;
    }

    /**
      * @Author Breach
      * @Description 读取excel表格
      * @Date 2019/1/2
      * @Param path
      * @return com.alibaba.fastjson.JSONArray
      */
    public XSSFSheet readExcelInfo(String path) {
        XSSFSheet sheet = null;
        try {
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(path));
            sheet = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheet;
    }
}

  