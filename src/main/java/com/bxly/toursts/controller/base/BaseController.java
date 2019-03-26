package com.bxly.toursts.controller.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class BaseController {
    protected  Logger logger = LogManager.getLogger(getClass());

    /**得到ModelAndView
     * @return
     */
    public ModelAndView getModelAndView() {
        return new ModelAndView();
    }

    /**
     * 中文传值解码
     */
    public String decodeStr(String str) {
        try {
            str = URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 解决导出excel文件,文件名出现中文乱码问题(浏览器不兼容问题)
     */
    public String decodeFileName(HttpServletRequest request, String fileName) {
        try {
            String userAgent = request.getHeader("user-agent");
            if(userAgent != null && userAgent.indexOf("Firefox") >= 0) {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}



