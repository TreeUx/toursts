package com.bxly.toursts.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bxly.toursts.model.JourneyInfo;
import com.bxly.toursts.service.BaiduMapInfoService;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/11/21
 * @Version V1.0
 **/
@RestController
@RequestMapping("/bxly")
public class CaptureBdJouneryController {
    private static final String bdUrl = "https://lvyou.baidu.com";

    @Autowired
    BaiduMapInfoService baiduMapInfoService;
    private static final String baiduUrl = "https://lvyou.baidu.com";

    @RequestMapping("/addJourneyInfo")
    public Map<String, Object> addBaiduJouneryInfo(String url) {
        Map<String, Object> result = new HashMap<>();
        JourneyInfo jio = new JourneyInfo();
        JSONObject jo = getLvyouInfo(url);
        if(!jo.isEmpty() && jo != null) {
            String province = jo.get("province").toString();
            String city = jo.get("city").toString();
            String mainScore = jo.get("main_score").toString();
            String impression = jo.get("impression").toString();
            String moreDesc = jo.get("more_desc").toString();
            String mapInfo = jo.get("map_info").toString();
            String href = jo.get("href").toString();//跳转页面地址后缀
            String trafficInfo = jo.get("traffic_info").toString();
            String bestSeason = jo.get("best_season").toString();
            String suggestPlayTime = jo.get("suggest_play_time").toString();
            String ticketInfo = jo.get("ticketInfo").toString();
            String openTime = jo.get("open_time").toString();
            String address = jo.get("address").toString();
            String jouneryRank = jo.get("jounery_rank").toString();
            String avg_cost = jo.get("avg_cost").toString();
            String going_count = jo.get("going_count").toString();
            String phone = jo.get("phone").toString();
            String webSite = jo.get("web_site").toString();
            String friendshipTips = jo.get("friendship_tips").toString();
            jio.setNation("中国");//国家
            jio.setProvince(province);//省
            jio.setCity(city);//市
            jio.setMainScore(mainScore);//评分
            jio.setImpression(impression);//大家印象
            jio.setMoreDesc(moreDesc);//更多描述
            jio.setMapInfo(mapInfo);//地图坐标
            jio.setTrafficInfo(trafficInfo);//交通出行方式
            jio.setBestSeason(bestSeason);//最佳旅游季节
            jio.setSuggestPlayTime(suggestPlayTime);//最佳游玩时间
            jio.setTicketInfo(ticketInfo);//门票价格
            jio.setOpenTime(openTime);//开放时间
            jio.setAddress(address);//地址
            jio.setJouneryRank(jouneryRank);//景点排名
            jio.setAvgCost(avg_cost);//人均消费
            jio.setGoingCount(going_count);//访问人数
            jio.setPhone(phone);//联系电话
            jio.setWebSite(webSite);//官网
            jio.setFriendshipTips(friendshipTips);//友情贴士
            boolean bl = baiduMapInfoService.addBaiduJourneyInfo(jio);
            if(bl) {
                result.put("status", "success");
                result.put("msg", "添加成功");
                System.out.println(province + city + "：添加成功");
            } else {
                result.put("status", "error");
                result.put("msg", "添加失败");
                System.out.println(province + city + "：添加失败");

            }
            /*if(href != "" && href != null) {
                System.out.println("******** 跳转页添加 Start ********");
                String next_url = url + href;
                addBaiduJouneryInfo(next_url);
            } else {
                return result;
            }*/
        }
        return result;
    }

    /**
      * @Author Breach
      * @Description 收集百度旅游网站页面相关数据专用(公共)
      * @Date 2018/11/21
      * @Param url
      * @return com.alibaba.fastjson.JSONObject
      */
    public static JSONObject getLvyouInfo(String url) {
        JSONObject jo = new JSONObject();
//        String url = "https://lvyou.baidu.com/guangzhou/";
        String imgs = "";//图片拼接
        try {
            Document doc = Jsoup.connect(url).get();//解析页面
//            System.out.println(doc.body());
            Elements a_s = doc.body().getElementsByClass("dest-crumbs").get(0).select("a");//标题处
        Elements li_s = doc.body().getElementById("J_pic-slider").select("li");//图片处
        int a_size = a_s.size();
        int li_size = li_s.size();
        for(int i = 0; i < li_size; i++) {
            String img = li_s.get(i).select("img").attr("src");
            int imgLenth = img.length();
            int index = img.indexOf("src=");
            img = img.substring(index + 4, imgLenth);
            imgs += img + (i==li_size-1?"":",");//图片
        }
        String province = "广东";//省
        String city = "";//市
        String href = "";//跳转城市地址后缀
        if(a_size > 4) {
            province = a_s.get(a_size - 2).text().trim();
            city = a_s.get(a_size - 1).text().trim();
            href = a_s.get(a_size - 1).attr("href");
        } else {
            city = "广东";
        }
        doc.getElementsByClass("remark-count").remove();//删除不需要采集的a标签text元素
        String main_score = doc.getElementsByClass("main-score").text().toString();//评分
        String jouneryRank = doc.body().getElementsByClass("point-rank").text();//景点排名
        String data = doc.body().select("script").toString();
        int startSub = data.indexOf("ext:{");
        int endSub = data.indexOf("});", startSub);
        JSONObject dataJo = JSONObject.parseObject(data.substring(startSub+4,endSub).trim().toString());//获取存放描述的信息集合
        String impression = dataJo.get("impression").toString().trim();//大家印象
        String more_desc = dataJo.get("more_desc").toString().trim();//更多描述
        String map_info = dataJo.get("map_info").toString().trim();//地图坐标
        String address = dataJo.get("address").toString().trim();//地址
        String phone = dataJo.get("phone").toString().trim();//联系电话
        String web_site = "";
        if(dataJo.containsKey("website")) {
            web_site = dataJo.get("website").toString().trim();//官网
        }
        String avg_cost = "";
        if(dataJo.containsKey("avg_cost")) {
            avg_cost = dataJo.get("avg_cost").toString().trim();//人均消费
        }
        String going_count = "";
        if(dataJo.containsKey("going_count")) {
            going_count = dataJo.get("going_count").toString().trim();//访问人数
        }


        System.out.println(doc.getElementsByClass("main-point-guide-wrap"));
        System.out.println(doc.getElementsByClass("main-point-guide-wrap").select("clearfix").text());
        String friendshipTips = "";//友情贴士
        if(doc.body().getElementsByClass("main-point-guide-wrap").toString().length() != 0) {
//                System.out.println(doc.body().getElementsByClass("main-point-guide-wrap").select(".list-content").get(1).text());
            friendshipTips = doc.body().getElementsByClass("main-point-guide-wrap").select(".list-content").get(1).text().trim();
        }
        int scen_start = data.indexOf("define('open_time_desc',{text:\"\"});");
        int scen_start_len = "define('open_time_desc',{text:\"\"});".trim().length();
        int scen_temp_len = data.indexOf("define('open_time_desc',{", scen_start + scen_start_len);
        int scen_end = data.indexOf("});", scen_temp_len);
        String scenStr = data.substring(scen_start + scen_start_len, scen_end + 4).trim();
//            System.out.println(scenStr);
        int ticket_index = scenStr.indexOf("define('ticket_info',{text:");//门票下标
        ticket_index = ticket_index + "define('ticket_info',{text:".length();
        int traffic_index = scenStr.indexOf("define('traffic',{text:");//交通下标
        traffic_index = traffic_index + "define('traffic',{text:".length();
        int bestvisittime_index = scenStr.indexOf("define('bestvisittime',{text:");//游玩时间下标
        bestvisittime_index = bestvisittime_index + "define('bestvisittime',{text:".length();
        int besttime_index = scenStr.indexOf("define('besttime',{text:");//最佳季节下标
        besttime_index = besttime_index + "define('besttime',{text:".length();
        int open_time_desc_index = scenStr.indexOf("define('open_time_desc',{text:");//开放时间下标
        open_time_desc_index = open_time_desc_index + "define('open_time_desc',{text:".length();

        String ticket_info = scenStr.substring(ticket_index, scenStr.indexOf("});", ticket_index)).trim();//门票信息
        ticket_info = "null".equals(ticket_info) ? "" : ticket_info.substring(1, ticket_info.length() - 1);
        JSONObject ticketJo = JSONObject.parseObject("{\"ticket_info\":\""+ ticket_info + "\"}");
        String trafficInfo = scenStr.substring(traffic_index, scenStr.indexOf("});", traffic_index)).trim();//交通出行路线
        trafficInfo = "null".equals(trafficInfo) ? "" : trafficInfo.substring(1, trafficInfo.length() - 1);
        JSONObject trafficJo = JSONObject.parseObject("{\"trafficInfo\":\""+ trafficInfo + "\"}");
        String suggestPlayTime = scenStr.substring(bestvisittime_index, scenStr.indexOf("});", bestvisittime_index));//建议游玩时间
        suggestPlayTime = "null".equals(suggestPlayTime ) ? "" : suggestPlayTime.substring(1, suggestPlayTime.length() - 1);
        JSONObject suggestJo = JSONObject.parseObject("{\"suggestPlayTime\":\""+ suggestPlayTime + "\"}");
        String bestSeason = scenStr.substring(besttime_index, scenStr.indexOf("});", besttime_index));//最佳季节
        bestSeason = "null".equals(bestSeason) ? "" : bestSeason.substring(1, bestSeason.length() - 1);
        JSONObject seasonJo = JSONObject.parseObject("{\"bestSeason\":\""+ bestSeason + "\"}");//防止单斜杠被转译成双斜杠
        String open_time_desc = scenStr.substring(open_time_desc_index, scenStr.indexOf("});", open_time_desc_index));//开放时间
        open_time_desc = "null".equals(open_time_desc) ? "" : open_time_desc.substring(1, open_time_desc.length() - 1);
        JSONObject openJo = JSONObject.parseObject("{\"open_time_desc\":\""+ open_time_desc + "\"}");
        jo.put("province", province);//省
        jo.put("city", city);//市
        jo.put("main_score", main_score);//评分
        jo.put("impression", impression);//大家印象评价
        jo.put("more_desc", more_desc);//更多描述
        jo.put("map_info", map_info);//地图坐标
        jo.put("traffic_info", trafficJo.get("trafficInfo"));//交通出行方式
        jo.put("best_season", seasonJo.get("bestSeason"));//最佳季节
        jo.put("suggest_play_time", suggestJo.get("suggestPlayTime"));//建议游玩时间
        jo.put("ticketInfo", ticketJo.get("ticket_info"));//门票信息
        jo.put("open_time", openJo.get("open_time_desc"));//开放时间
        jo.put("address", address);//景点地址
        jo.put("avg_cost", avg_cost);
        jo.put("going_count", going_count);
        jo.put("jounery_rank", jouneryRank);//景点排名
        jo.put("phone", phone);//联系电话
        jo.put("web_site", web_site);//官网
        jo.put("friendship_tips", friendshipTips);//友情贴士
        jo.put("href", href);//要跳转的城市地址后缀
        System.out.println(jo);
        System.out.println("*************** 分割线 *****************");
    } catch (IOException e) {
        e.printStackTrace();
    }
        return jo;
    }

    public static JSONArray getBdJouneryUrls() {

        String url = "https://lvyou.baidu.com/guangdong";
        JSONArray ja = new JSONArray();
        ja.add(url);
        try {
            Document doc = Jsoup.connect(url).get();//解析页面
//            System.out.println(doc);
            System.out.println(doc.getElementsByClass("main-scene").select("a").first().attr("href"));
            url = baiduUrl + doc.getElementsByClass("main-scene").select("a").first().attr("href");
            System.out.println(url);
            doc = Jsoup.connect(url).get();
            System.out.println(doc);
            System.out.println(doc.getElementsByClass("view-list-container").select("li"));
            int li_size = doc.body().getElementsByClass("view-list-container").select("li").size();
            for (int i = 0; i < li_size; i++) {
                String temp_url = doc.body().getElementsByClass("view-list-container").select("li").get(i).select("a").attr("href");
                ja.add(bdUrl + temp_url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ja;
    }

    public static void main(String[] args) {
//        String url = "https://lvyou.baidu.com/guangdong/";
//        String url = "https://lvyou.baidu.com/shamian?innerfr_pg=destinationDetailPg&accur_thirdpar=destination";
//        getLvyouInfo(url);

        JSONArray ja = getBdJouneryUrls();
        for(int i = 0; i < ja.size(); i++) {
            String url = ja.get(i).toString();
            new CaptureBdJouneryController().addBaiduJouneryInfo(url);
        }
    }
}
