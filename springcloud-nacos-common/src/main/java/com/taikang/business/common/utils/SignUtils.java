package com.taikang.business.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by itw_liucl
 * Date: 2018/6/19
 * Time: 15:06
 */
public class SignUtils {
    static Logger logger = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 生成签名的算法
     *
     * @param paramsMap 生成签名的数据
     * @param signKey   签名key
     * @return
     */
    public static String assembleSign(final Map<String, String> paramsMap, final String signKey) {
        List<String> keys = new ArrayList<String>(paramsMap.keySet());
        Collections.sort(keys);
        StringBuffer paramStr = new StringBuffer();
        for (int i = 0, size = keys.size(); i < size; i++) {
            if (i != size - 1) {
                if (StringUtils.isNotEmpty(paramsMap.get(keys.get(i)))) {
                    paramStr.append(keys.get(i)).append("=").append(paramsMap.get(keys.get(i))).append("&");
                }
            } else {
                if (StringUtils.isNotEmpty(paramsMap.get(keys.get(i)))) {
                    paramStr.append(keys.get(i)).append("=").append(paramsMap.get(keys.get(i)));
                }
            }
        }
        paramStr.append(signKey);
//        System.out.println(paramStr);
        String sign = sha1(paramStr.toString());
        return sign;
    }


    /**
     * SHA1
     *
     * @param str 待签名字符串
     * @return SHA1签名后字符串
     */
    public static String sha1(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(str.getBytes());
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SHA1算法失败");
        }
        return null;
    }

    /**
     * byte to hex
     *
     * @param digest 待转换字节数组
     * @return 转换后字符串
     */
    public static String toHexString(byte[] digest) {
        String str = "";
        String tempStr = "";
        for (int i = 0; i < digest.length; i++) {
            tempStr = (Integer.toHexString(digest[i] & 0xff));
            if (tempStr.length() == 1)
                str = str + "0" + tempStr;
            else
                str = str + tempStr;
        }
        return str.toLowerCase();
    }
    /**
     * 使用Md5Encrypt生成签名的算法
     *
     * @param Map 生成签名的数据
     * @return
     */
    public static String assembleMd5Sign(final Map<String, String> Map) {
        List<String> keys = new ArrayList<String>(Map.keySet());
        Collections.sort(keys);
        StringBuffer paramStr = new StringBuffer();
        for (int i = 0, size = keys.size(); i < size; i++) {
            if (i != size - 1) {
                if (StringUtils.isNotEmpty(Map.get(keys.get(i)))) {
                    paramStr.append(keys.get(i)).append("=").append(Map.get(keys.get(i))).append("&");
                }
            } else {
                if (StringUtils.isNotEmpty(Map.get(keys.get(i)))) {
                    paramStr.append(keys.get(i)).append("=").append(Map.get(keys.get(i)));
                }
            }
        }
        String sign = Md5Encrypt.getMD5Mac(paramStr.toString(),"utf-8").toUpperCase();
        return sign;
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("out_order_no","200011553156800");
        //gdfg34545765dsfsg564
        //7770294f0a4ea4be8aa687c2b8a683a6304cf17c
        //ec841369ff4540bf24ce8cd830f4701a47b80db7
        System.out.println(SignUtils.assembleSign(map, "gdfg34545765dsfsg564"));
    }
}
