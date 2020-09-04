package com.taikang.business.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * @author : itw_gaofan
 * @date : Created in 2019/07/15
 * @description :
 */
public class SignUtil {

    //获取签名
    public static String getSign(String uid,String authCode,
                                 String api,String time,String args){
        String signKey =
                uid+"@"+authCode+"@"+api+"@"+time+"@"+args;
        return MD5(signKey);
    }
    //获取签名
    public static String getSign(String authCode, String time){
        String signKey = authCode+time;
        return MD5(signKey);
    }
    public static String MD5(String source){
        if (StringUtils.isEmpty(source)) {
            return "";
        }
        return DigestUtils.md5Hex(source);
    }
    }
