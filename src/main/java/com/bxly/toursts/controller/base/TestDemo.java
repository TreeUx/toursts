package com.bxly.toursts.controller.base;

import java.util.*;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/12/12
 * @Version V1.0
 **/
public class TestDemo {
    public static void main(String[] args) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> para = new HashMap<>();
        para.put("sumprice", 20);
        para.put("stock", 15);
        list.add(para);
        para = new HashMap<>();
        para.put("sumprice", 15);
        para.put("stock", 10);
        list.add(para);
        para = new HashMap<>();
        para.put("sumprice", 30);
        para.put("stock", 25);
        list.add(para);
        System.out.println(list);
    }
}
