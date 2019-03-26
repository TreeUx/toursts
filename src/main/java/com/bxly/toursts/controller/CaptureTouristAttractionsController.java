package com.bxly.toursts.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @Description 使用HttpURLConnection方式请求页面抓取页面数据
 * @Author Breach
 */
public class CaptureTouristAttractionsController {

    public static void main(String[] args) throws MalformedURLException {
        String data = "";
        try {
            //建立连接
            URL url = new URL("https://lvyou.baidu.com/guangdong");//测试请求地址

            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("Content-Type", "text/javascript;charset=UTF-8");

            //为字符输入流添加缓冲
            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream(), "utf-8"));
            // 读取返回结果
            String line = "";
            while ((line = br.readLine()) != null) {
                data += line;
            }
            System.out.println(data);
            if (data != null) {
                //Jsoup详解见地址：https://www.cnblogs.com/zhangyinhua/p/8037599.html
                Document doc = Jsoup.parseBodyFragment(data);//解析HTML页面
//                System.out.println(doc);
                System.out.println("***************** 三八线 *****************");
//                System.out.println(doc.body().getElementById("J_pic-slider"));
//                System.out.println(doc.body().getElementById("J_pic-slider").getElementsByTag("li"));
//                System.out.println(doc.body().getElementById("J_pic-slider").getElementsByTag("li").size());
//                System.out.println(doc.body().getElementById("J_pic-slider").getElementsByTag("li").select("img").tagName("src"));
//                System.out.println(doc.body().getElementsByClass("main-info-wrap"));
//                System.out.println(doc.body().getElementsByClass("main-info-wrap").select(".remark-count").remove());
//                System.out.println(doc.body().getElementsByClass("main-info-wrap").select(".main-score").text());//获取评分
//                System.out.println(doc.body());
//                System.out.println(doc.body().getElementById("ui-id-5").text());
                // 释放资源
                br.close();
                httpUrlConn.disconnect();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    }


