package com.taikang.business.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author itw_wangjian05
 */
public class MD5Tools {
    private static final Logger logger = LoggerFactory.getLogger(MD5Tools.class);

    /**
     * 既往病史MD5生成签名字符串
     *
     * @param map 需签名参数
     * @param key MD5key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String MD5sign(Map<String, Object> map, String key) throws NoSuchAlgorithmException {
        String genSign = "";
        String[] signFields = new String[2];
        signFields[0] = "userName";
        signFields[1] = "userId";
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        // 生成签名原文
        String signSrc = orgSignSrc(signFields, param);
        // MD5的方式签名
        signSrc += "&KEY=" + key;
        // MessageDigest md5 = MessageDigest.getInstance(signSrc, "MD5");
        genSign = MD5(signSrc);
        return genSign;
    }

    /**
     * 既往病史MD5验证签名
     *
     * @param map
     * @param key
     * @param sign
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean vlidateMD5signForMedical(Map<String, Object> map, String key, String sign) throws NoSuchAlgorithmException {
        String vsign = MD5sign(map, key);
        boolean equals = vsign.equals(sign);
        logger.info("MD5验证签名生成的签名：{}", vsign);
        logger.info("MD5验证签名生成的签名与原签名是否一致：{}", (equals));
        return equals;
    }

    /**
     * 构建签名原文
     *
     * @param signFields 参数列表
     * @param param      参数与值的jsonbject
     * @return
     */
    private static String orgSignSrc(String[] signFields, JSONObject param) {
        if (signFields != null) {
            Arrays.sort(signFields); // 对key按照 字典顺序排序
        }
        StringBuffer signSrc = new StringBuffer("");
        int i = 0;
        for (String field : signFields) {
            signSrc.append(field);
            signSrc.append("=");
            signSrc.append((StringUtils.isEmpty(param.getString(field)) ? "" : param.getString(field)));
            // 最后一个元素后面不加&
            if (i < (signFields.length - 1)) {
                signSrc.append("&");
            }
            i++;
        }
        return signSrc.toString();
    }

    public static String MD5(String pwd) throws NoSuchAlgorithmException {
        // 用于加密的字符
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        // 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
        byte[] btInput = pwd.getBytes();
        // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
        mdInst.update(btInput);
        // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) { // i = 0
            byte byte0 = md[i]; // 95
            str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
            str[k++] = md5String[byte0 & 0xf]; // F
        }
        // 返回经过加密后的字符串
        return new String(str);
    }

    /**
     * 通用MD5验证签名
     *
     * @param map
     * @param key
     * @param sign
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean vlidateMD5sign(Map<String, Object> map, String key, String sign) throws NoSuchAlgorithmException {
        String vsign = MD5signUtil(map, key);
        boolean equals = vsign.equals(sign);
        logger.info("MD5验证签名生成的签名：{}", vsign);
        logger.info("MD5验证签名生成的签名与原签名是否一致：{}", (equals));
        return equals;
    }

    /**
     * MD5加密验签
     *
     * @param map
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String MD5signUtil(Map<String, Object> map, String key) throws NoSuchAlgorithmException {
        String genSign = "";
        String[] signFields = null;
        int i = 0;
        if (!map.isEmpty()) {
            signFields = new String[map.size()];
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String entryKey = entry.getKey();
                signFields[i] = entryKey;
                i++;
            }
        }
        if (signFields == null) {
            return null;
        }
        JSONObject param = (JSONObject) JSONObject.toJSON(map);
        // 生成签名原文
        String signSrc = orgSignSrc(signFields, param);
        // MD5的方式签名
        signSrc += "&KEY=" + key;
        genSign = MD5(signSrc);
        return genSign;
    }

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userId", "2321011993126261610");
        paramMap.put("userName", "wang");
        logger.info("{}", paramMap);
        /*** MD5签名与验签 **/
        String key = "4f89ca756b2a2942acbc9eb9bd39adf7";
        String sign;
        try {
            sign = MD5signUtil(paramMap, key);
            logger.info("生成的MD5签名：", sign);
        } catch (NoSuchAlgorithmException e) {
            logger.error("[-main-]MD5Tools", e);
            e.printStackTrace();
        }
    }

}
