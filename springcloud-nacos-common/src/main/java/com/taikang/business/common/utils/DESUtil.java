package com.taikang.business.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;

public class DESUtil {
    /**
     * 加密
     *
     * @param key
     * @param value
     * @return
     */
    public static String encryptECB(String key, String value) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] binaryData = cipher.doFinal(value.getBytes("UTF-8"));
            return Base64.encodeBase64String(binaryData);
        } catch (InvalidKeyException e) {
            e.printStackTrace();

            return null;
        } catch (Exception e1) {
            e1.printStackTrace();

            return null;
        }
    }

    /**
     * 解密
     *
     * @param key
     * @param value
     * @return
     */
    public static String decryptECB(String key, String value) {
        try {
            byte[] binaryValue = Base64.decodeBase64(value);
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
     * Description 加密方法  汇法风险信息精确查询接口  测试秘钥："huifa321"
     * @param data 需要加密的参数（姓名、身份证、全称、组织机构代码）
     * @param key  密钥（找汇法技术提供）
     * @return
     * @throws Exception
     */
    public static String encryptHuiFa(String data, String key) throws Exception {
        byte[] bt = encryptHuiFa(data.getBytes("UTF-8"), key.getBytes("UTF-8"));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }

    /**
     * Description 根据键值进行加密  汇法风险信息精确查询接口
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encryptHuiFa(byte[] data, byte[] key) throws Exception {

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);

        return cipher.doFinal(data);
    }


    /**
     * Description 解密方法  汇法风险信息精确查询接口   测试秘钥："huifa321"
     * @param data 需要解密的查询结果字符串
     * @param key  密钥（找汇法技术提供）
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decryptHuiFa(String data, String key) throws IOException, Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decryptHuiFa(buf,key.getBytes("UTF-8"));
        return new String(bt,"UTF-8");
    }

    /**
     * Description 根据键值进行解密   汇法风险信息精确查询接口
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decryptHuiFa(byte[] data, byte[] key) throws Exception {

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
        IvParameterSpec iv = new IvParameterSpec(key);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
        byte[] str = null;
        str = cipher.doFinal(data);
        return str;
    }
}
