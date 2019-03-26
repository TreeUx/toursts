package com.bxly.toursts.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
  * @Author Breach
  * @Description 设置编码工具类
  * @Date 2018/11/1
  */
public class URLEncodeUtils {
    public static String urlEncode(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}

  