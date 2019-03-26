package com.bxly.toursts.service;

import com.bxly.toursts.model.JourneyInfo;
import com.bxly.toursts.model.ParkInfo;
import com.bxly.toursts.model.TouristInfo;
import com.bxly.toursts.model.BxMerchantCo;

import java.util.List;
import java.util.Map;

public interface BaiduMapInfoService {
    boolean getBaiduMapInfo(TouristInfo tv);

    int findCountByUid(String uid);

    Boolean insertTouristParkInfo(ParkInfo pi);

    boolean addBaiduJourneyInfo(JourneyInfo jio);

    boolean addTouristInfos(Map<String, Object> para);

    int findParentById(int str);

    int findCountByName(String area_name);

    int findParentByCode(String str);

    int findParentCode(String str);

    boolean addTouristProductsInfos(Map<String, Object> para);

    int findParentIds(int relation);

    List<Map<String, Object>> queryCityInfos();

    int findTouristCityInfo(String area_name);

    int updateTouistCityInfo(Map<String, Object> para);

    int updateTouistCountryInfo(Map<String, Object> para);

    List<Map<String, Object>> getLocationInfos();

    int updateLocaInfos(List<Map<String, Object>> locaList);

    List<Map<String, Object>> getStateAndCityInfo(Map<String, Object> para);

    int addAirportInfo(Map<String, Object> params);

    int addRailwayInfos(List<Map<String, Object>> railInfoList);

    List<Map<String, Object>> getProvinceAndCityInfo(Map<String, Object> para);

    Boolean insertMerchantParkInfo(BxMerchantCo bmc);

    Map<String, Object> findCityId(String region);
}
