package com.pat.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * Project Name: pat-foodie-dev
 * File Name: MD5Utils
 * Package Name: com.pat.utils
 * Author: elisha
 * Date: 2020/5/8 23:22
 * Copyright (c) 2020,All Rights Reserved.
 * Description：MD5加密工具类
 */
public class MD5Utils {

    /**
     *
     * @Title: MD5Utils.java
     * @Package com.pat.utils
     * @Description: 对字符串进行md5加密
     */
    public static String getMD5Str(String strValue) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newstr;
    }

    public static void main(String[] args) {
        try {
            String md5 = getMD5Str("pat");
            System.out.println(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
