package com.bxly.toursts.controller;

import com.bxly.toursts.common.MapConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bxly.toursts.controller.MyX509TrustManager.httpsRequest;


@RestController
@RequestMapping("/bxly")
@Log4j2
public class MapController {

    /**
      * @Author Breach
      * @Description
      * @Date 2018/10/31
      * @Param 
      * @return java.lang.String
      */
    public static String getMapCoordinates() {
        String url = MapConstant.GOOGLE_PREFIX_URL;
        String coStr = httpsRequest(url, "GET", null);
        return coStr;
    }


}
