package com.taikang.business.common.utils;/*
 *create by itw_zhaogq on 2019/2/19
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * ID生成器
 *
 *  @author liuyf104
 *
 */
public final class IdHelper {

    /**
     * UUID生成(32位)
     *
     * @return
     */
    public static String generateLongUUID() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
    /**
     * 获取随机数
     *
     * eg:TK2018121314385815
     */
    public static String getRandom(int length) {
        String fomat = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        Random rm = new Random();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(rm.nextInt(9));
        }
        String random = fomat + stringBuilder.toString();
        StringBuilder sb = new StringBuilder();
        String newRandom = sb.append("TK").append(random).toString();
        return newRandom;
    }

    /**
     * UUID生成(16位)
     * @return
     */
    public static String generateShortUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(16);
    }

    /**
     * 生成六位验证码
     *
     * @return
     */
    public static String getRamdPw6() {
        String ret = "";
        int[] randArr = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = randArr[index];
            randArr[index] = randArr[i - 1];
            randArr[i - 1] = tmp;
        }

        int result = 0;
        for (int i = 0; i < 6; i++) {
            result = result * 10 + randArr[i];
        }
        if (String.valueOf(result).length() == 5) {
            ret = (new StringBuilder("0")).append(result).toString();
        } else {
            ret = String.valueOf(result);
        }
        return ret;
    }


    private static String randmonString(int length) {
        Random random = new Random();
        String ssource = "0123456789";
        char[] src = ssource.toCharArray();
        char[] buf = new char[length];
        /*    */
        for (int i = 0; i < length; i++) {
            int rnd = Math.abs(random.nextInt()) % src.length;
            buf[i] = src[rnd];
        }
        return new String(buf);
    }

    /**
     * 获取多位随机数
     * @param i
     * @return
     */
    public static String runVerifyCode(int i) {
        String verify = randmonString(i);
        return verify;
    }
}
