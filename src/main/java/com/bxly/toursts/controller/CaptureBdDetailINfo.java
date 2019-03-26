package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description 测试收集百度旅游网站相关数据
 * @Author Breach
 * @Date 2018/11/23
 * @Version V1.0
 **/
public class CaptureBdDetailINfo {
    private static final String bdUrl = "https://lvyou.baidu.com";


    public static void main(String[] args) {
        JSONArray ja = new JSONArray();
        /**HtmlUnit请求web页面*/
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setJavaScriptEnabled(true); //启用JS解释器，默认为true
        wc.getOptions().setCssEnabled(false); //禁用css支持
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(30000); //设置连接超时时间 ，这里是30S。如果为0，则无限期等待
        wc.getOptions().setUseInsecureSSL(true);//允许绕过SSL认证
        wc.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持ajax异步
//        wc.getOptions().setActiveXNative(true);//允许启动注册主键
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);//屏蔽日志输出
        java.util.logging.Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);

        HtmlPage page = null;
        try {
            String url = bdUrl + "/guangdong/jingdian";
            page = wc.getPage(url);
            wc.waitForBackgroundJavaScript(60 * 1000);//等待JS驱动dom完成获得还原后的网页
            String pageXml = page.asXml(); //以xml的形式获取响应文本
//            System.out.println(pageXml);
            /**jsoup解析文档*/
            Document doc = Jsoup.parse(pageXml);
            System.out.println("***************** li标签开始 *******************");
//            System.out.println(url);
//            System.out.println(doc.body().getElementsByClass("view-list-container"));
            int li_size = doc.body().getElementsByClass("view-list-container").select("li").size();
            for (int i = 0; i < li_size; i++) {
                String temp_url = doc.body().getElementsByClass("view-list-container").select("li").get(i).select("a").attr("href");
                ja.add(bdUrl + temp_url);
            }

            page.getElementById("J-view-selector").getElementsByTagName("div").get(2).getElementsByTagName("a").get(0).setAttribute("id", "capture1");
//            System.out.println(page.asXml());
            System.out.println("***************** li标签结束 *******************");
            HtmlPage page1 = page.getElementById("capture1").click();
            System.out.println(page1.asXml());
            System.out.println(ja);
            System.out.println("***************** page结束 *******************");

            wc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
