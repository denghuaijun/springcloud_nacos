/**
 * @(#)SecureUtils.java 2016-11-28
 *
 * Copyright (c) wayyue Corporation. All Rights Reserved.
 */
package com.taikang.business.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 安全工具类
 * @author Junbo.Zhang
 * @version 1.0 2017-3-7
 */
public final class SecureUtils {
    private static Logger logger = LoggerFactory.getLogger(SecureUtils.class);
    private static final String S = "1234567890abcdefghijklmnopqrstuvwxyz,.`-=/ABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+";
    
    /**
     * AES加密
     * @param content 待加密的内容
     * @param encryptKey 加密密钥
     * @param charset 编码
     * @return 加密后字符串
     */
    public static String aesEncrypt(String content, String encryptKey, String charset) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encryptKey.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes(charset);
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | UnsupportedEncodingException
                | IllegalBlockSizeException | BadPaddingException e) {
            logger.error("AES加密失败.", e);
        }
        return null;
    }
    
    /**
     * AES解密
     * @param content 待解密内容
     * @param encryptKey 解密密钥
     * @return 解密后字符串
     */
    public static String aesDecrypt(String content, String encryptKey,String charset) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(encryptKey.getBytes());
            kgen.init(128, secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(toByteArray(content));
            return new String(result,charset); // 加密
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            logger.error("AES解密失败.", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("AES解密失败.", e);
        }
        return null;
    }
    
    /**
     * 将二进制转换成16进制
     * @param buf 待转换的字节数组
     * @return 转换后的字符串
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1)
                hex = '0' + hex;
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }
    
    /**
     * SHA1
     * @param str 待签名字符串
     * @return SHA1签名后字符串
     */
    public static String sha1(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest( str.getBytes());
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA1签名失败.", e);
        }
        return null;
    }

    /**
     * MD5
     * @param str 待加密字符串
     * @return MD5加密后字符串
     */
    public static String md5(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest( str.getBytes());
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5签名失败.", e);
        }
        return null;
    }
    
    /**
     * 生成密钥
     * @param length 生成密钥的长度
     * @return 生成的密钥字符串
     */
    public static String generateSecretKey(int length) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        final int keyLen = S.length();
        for (int i = 0; i < length; i++)
            sb.append(S.charAt(rnd.nextInt(keyLen)));
        return sb.toString();
    }

    /**
     * byte to hex
     * @param digest 待转换字节数组
     * @return 转换后字符串
     */
    private static String toHexString(byte[] digest) {
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
     * 将16进制转换为二进制
     * @param str 待转换字符串
     * @return 转换后字符串
     */
    private static byte[] toByteArray(String str) {
        if (str.length() < 1)
            return null;
        byte[] result = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            int high = Integer.parseInt(str.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(str.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
