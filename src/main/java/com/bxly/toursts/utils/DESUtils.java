package com.bxly.toursts.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.SecureRandom;

/**
 * @author: kevin
 * @date: 2018/10/23 15:24
 */
public class DESUtils {

    /**
     * 加密
     * @param datasource
     * @param password
     * @return
     */
    public static String encrypt(String datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            String result = parseByte2HexStr(cipher.doFinal(datasource.getBytes("utf-8")));
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for(byte b : bytes) {
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return buf.toString();
    }

    /**
     * 解密
     * @param src
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String src, String sKey) throws Exception {
        byte[] key = sKey.getBytes();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(key);
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        // 真正开始解密操作
        byte[] decryptData = cipher.doFinal(parseHexStr2Byte(src));
        return new String(decryptData, "utf-8");
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
      * @Author Breach
      * @Description
      * @Date 2018/11/21
      * @Param str
      * @return java.lang.String
      */
    public static String decodeStr(String str) {
        String temp = "";
        String result = "";
        if(str == null) {
            return "";
        }
        if(str.length() % 6 != 0) {//unicode字符串有误
            return "";
        } else {
            try {
                for(int i = 0; i < str.length() / 6; i++) {
                    temp = str.substring(6*i, 6*(i+1));
                    result = URLDecoder.decode(temp, "gb2312");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        String json = "{\"airportCode\":\"LGW\",\"arrAddressPoi\":\"51.4681393,-0.3578945\",\"depAddressPoi\":\"51.153588,-0.182063\",\"serviceTime\":\"2018-11-01 12:00:00\"}";
        String key = "12345678";
        String encryptString = encrypt(json, key).toUpperCase();
        System.out.println("加密后：\n" + encryptString);
        String decryptString = decrypt(encryptString, key);
        System.out.println("解密后：\n" + decryptString);

    }
}
