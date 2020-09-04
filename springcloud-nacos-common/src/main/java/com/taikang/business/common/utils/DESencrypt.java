package com.taikang.business.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import org.apache.commons.codec.binary.Base64;


/**
 * @author itw_xieyy
 */
public class DESencrypt {

    private static final Logger logger = LoggerFactory.getLogger(DESencrypt.class);

    /**
     * 功能描述：<br>
     * 加密方法
     *
     * @param key　加密的ｋｅｙ值
     * @param value　需加密的字符串
     * @author itw_wangyc01
     * @date created in 10:54 2018/7/27
     */
    public static String encryptECB(String key, String value) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] binaryData = cipher.doFinal(value.getBytes("UTF-8"));
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(binaryData);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            logger.debug("Invalid DES key, not encrypting");
            return null;
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.debug("Error in encryption, not encrypting");
            return null;
        }
    }

    /**
     * 功能描述：<br>
     * 解密方法
     *
     * @param key　加密的ｋｅｙ值
     * @param value　需解密的字符串
     * @author itw_wangyc01
     * @date created in 10:54 2018/7/27
     */
    public static String decryptECB(String key, String value) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] binaryValue = decoder.decodeBuffer(value);
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] data = cipher.doFinal(binaryValue);
            return new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 测试
     */
   /*
   public static void main(String[] args) {
        String key1 = "tkcwuhan";
        JSONObject map = new JSONObject();
        map.put("policyNo", "6420110113020170000001");
        map.put("status", "1");
        String md5json = Md5Encrypt.getMD5Mac(key1 + map.toString());
        System.out.println("md5json:" + md5json);
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("token", md5json);
        jsonobj.put("req", map.toString());

        String encryptText = encryptECB(key1, jsonobj.toString());

        //解密
        String originData = decryptECB("tkcwuhan", encryptText);
        System.out.println(encryptText + " : " + originData);
    }*/
    /**
     * 解密DES
     * @param logKey
     * @param key
     * @param
     * @return
     */
    public static String decryptECB(String logKey,String key, String reqStr) {
        reqStr = "null".equals(reqStr.subSequence(0, 4))?reqStr.substring(4):reqStr.replace(" ", "+");
        String result = null;
        try {
            byte[] binaryValue = Base64.decodeBase64(reqStr.getBytes("UTF-8"));
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] data = cipher.doFinal(binaryValue);
            result = new String(data, "UTF-8");
        } catch (Exception e) {
           // CommonParamUtil.common_log.error("{},DES解密异常,DES解密前:{}",logKey,reqStr,e);
            return result = null;
        }
        //CommonParamUtil.common_log.info("{},DES解密前:{},DES解密后:{}",logKey,reqStr,result);
        return result;
    }
}

