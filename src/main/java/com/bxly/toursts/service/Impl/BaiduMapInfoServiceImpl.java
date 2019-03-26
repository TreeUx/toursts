package com.bxly.toursts.service.Impl;

import com.bxly.toursts.dao.BaiduMapInfoMapper;
import com.bxly.toursts.model.JourneyInfo;
import com.bxly.toursts.model.ParkInfo;
import com.bxly.toursts.model.TouristInfo;
import com.bxly.toursts.model.BxMerchantCo;
import com.bxly.toursts.service.BaiduMapInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/11/15
 * @Version V1.0
 *
 **/
@Service
public class BaiduMapInfoServiceImpl implements BaiduMapInfoService {

    @Resource
    private BaiduMapInfoMapper baiduMapInfoMapper;

    /**
      * @Author Breach
      * @Description 获取旅游信息插入相应数据库
      * @Date 2018/11/15
      * @Param tv
      * @return void
      */
    @Override
    public boolean getBaiduMapInfo(TouristInfo tv) {
        boolean bl = baiduMapInfoMapper.getBaiduMapInfo(tv);
        return bl;
    }

    /**
     * @Author Breach
     * @Description 过滤重复数据
     * @Date 2018/11/19
     * @Param uid
     * @return int
     */
    @Override
    public int findCountByUid(String uid) {
        int num = baiduMapInfoMapper.findCountByUid(uid);
        return num;
    }

    /**
      * @Author Breach
      * @Description 获取旅游景点相关出入口数据并插入表中
      * @Date 2018/11/19
      * @Param pi
      * @return java.lang.Boolean
      */
    @Override
    public Boolean insertTouristParkInfo(ParkInfo pi) {
        boolean bl = baiduMapInfoMapper.insertTouristParkInfo(pi);
        return bl;
    }

    /**
      * @Author Breach
      * @Description 保存百度旅游网站相关信息
      * @Date 2018/11/21
      * @Param jio
      * @return boolean
      */
    @Override
    public boolean addBaiduJourneyInfo(JourneyInfo jio) {
        return baiduMapInfoMapper.addBaiduJourneyInfo(jio);
    }

    /**
      * @Author Breach
      * @Description 向bx_region表中插入数据
      * @Date 2018/11/26
      * @Param para
      * @return boolean
      */
    @Override
    public boolean addTouristInfos(Map<String, Object> para) {
        return baiduMapInfoMapper.addTouristInfos(para);
    }

    /**w
      * @Author Breach
      * @Description 查询parent_id
      * @Date 2018/11/26
      * @Param str
      * @return java.lang.String
      */
    @Override
    public int findParentById(int str) {
        int parent_id = baiduMapInfoMapper.findParentById(str);
        return parent_id;
    }

    /**
      * @Author Breach
      * @Description 向bx_region表中插入相关数据
      * @Date 2018/11/27
      * @Param area_name
      * @return int
      */
    @Override
    public int findCountByName(String area_name) {
        return baiduMapInfoMapper.findCountByName(area_name);
    }

    /**
      * @Author Breach
      * @Description 向bx_commodity商品表中插入相关数据
      * @Date 2018/11/27
      * @Param str
      * @return int
      */
    @Override
    public int findParentByCode(String str) {
        return baiduMapInfoMapper.findParentByCode(str);
    }

    @Override
    public int findParentCode(String str) {
        return baiduMapInfoMapper.findParentCode(str);
    }

    @Override
    public boolean addTouristProductsInfos(Map<String, Object> para) {
        return baiduMapInfoMapper.addTouristProductsInfos(para);
    }

    @Override
    public int findParentIds(int relation) {
        return baiduMapInfoMapper.findParentIds(relation);
    }

    @Override
    public List<Map<String, Object>> queryCityInfos() {
        List<Map<String, Object>> list = baiduMapInfoMapper.queryCityInfos();
        return list;
    }

    @Override
    public int findTouristCityInfo(String area_name) {
        int result = baiduMapInfoMapper.findTouristCityInfo(area_name);
        return result;
    }

    /**
      * @Author Breach
      * @Description 修改city为id，以关联bx_region和商品表
      * @Date 2018/11/28
      * @Param para
      * @return boolean
      */
    @Override
    public int updateTouistCityInfo(Map<String, Object> para) {
        return baiduMapInfoMapper.updateTouistCityInfo(para);
    }
    @Override
    public int updateTouistCountryInfo(Map<String, Object> para) {
        return baiduMapInfoMapper.updateTouistCountryInfo(para);
    }

    @Override
    public List<Map<String, Object>> getLocationInfos() {
        return baiduMapInfoMapper.getLocationInfos();
    }

    @Override
    public int updateLocaInfos(List<Map<String, Object>> locaList) {
        return baiduMapInfoMapper.updateLocaInfos(locaList);
    }

    @Override
    public List<Map<String, Object>> getStateAndCityInfo(Map<String, Object> para) {
        return baiduMapInfoMapper.getStateAndCityInfo(para);
    }

    /**
      * @Author Breach
      * @Description 添加全球机场数据
      * @Date 2018/12/13
      * @Param params
      * @return int
      */
    @Override
    public int addAirportInfo(Map<String, Object> params) {
        return baiduMapInfoMapper.addAirportInfo(params);
    }

    /**
      * @Author Breach
      * @Description 添加全国火车站信息
      * @Date 2018/12/18
      * @Param railInfoList
      * @return
      */
    @Override
    public int addRailwayInfos(List<Map<String, Object>> railInfoList) {
        return baiduMapInfoMapper.addRailwayInfos(railInfoList);
    }

    /**
      * @Author Breach
      * @Description 查询省市县id
      * @Date 2018/12/18
      * @Param para
      * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
      */
    @Override
    public List<Map<String, Object>> getProvinceAndCityInfo(Map<String, Object> para) {
        return baiduMapInfoMapper.getProvinceAndCityInfo(para);
    }

    /**
      * @Author Breach
      * @Description 插入景区商品数据（，美国西海岸）
      * @Date 2019/1/2
      * @Param bmc
      * @return java.lang.Boolean
      */
    @Override
    public Boolean insertMerchantParkInfo(BxMerchantCo bmc) {
        return baiduMapInfoMapper.insertMerchantParkInfo(bmc);
    }

    /**
      * @Author Breach
      * @Description 查询城市id、时区id等
      * @Date 2019/1/2
      * @Param region
      * @return java.util.Map<java.lang.String,java.lang.Object>
      */
    @Override
    public Map<String, Object> findCityId(String region) {
        return baiduMapInfoMapper.findCityId(region);
    }


}
