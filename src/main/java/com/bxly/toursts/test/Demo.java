package com.bxly.toursts.test;

/**
 * @Description TODO
 * @Author Breach
 * @Date 2018/11/19
 * @Version V1.0
 **/
public class Demo extends Thread {
        public void run() {
            for(int i = 0; i < 10; i++) {
                System.out.println(i);
            }
        }
}
