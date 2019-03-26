package com.bxly.toursts.common;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;


public class ReadExcel {
    /**
     * @Description 创建sheet读取Excel
     * @Author Breach
     * @Date 2018/12/7
     * @Version V1.0
     **/
    public static Sheet readExcelInfo(String path) {
        Sheet sheet = null;
        try {
            Workbook wb = WorkbookFactory.create(new FileInputStream(path));
            sheet = wb.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return sheet;
    }


    public static void main(String[] args) {
    }
}
