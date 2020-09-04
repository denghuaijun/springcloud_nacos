package com.taikang.business.common.utils.order;

import org.apache.http.client.utils.DateUtils;

import java.util.Date;

/**
 * @author : itw_xieyy
 * @date : Created in 2019/4/23
 * @description :
 */
public class OrderIdUtils {

    /**
     * 体检服务前缀
     */
    public static final String TJ_PREFIX = "TJ";

    /**
     * 视频问诊前缀
     */
    public static final String SP_PREFIX = "SP";

    /**
     * 就医服务前缀
     */
    public static final String JY_PREFIX = "JY";

    /**
     * 齿科服务前缀
     */
    public static final String CK_PREFIX = "CK";

    /**
     * 生成orderId
     *
     * @param prefix        服务前缀
     * @param date          日期
     * @param vendorId      供应商id
     * @param reserDetailId 预约详情表id
     * @return
     */
    public static String generatorOrderId(String prefix, Date date, Long vendorId, Long reserDetailId) {
        String formatDate = DateUtils.formatDate(date, "yyyyMMdd");
        String vendorIdStr = joint(4, String.valueOf(vendorId));
        String reserDetailIdStr = joint(14, String.valueOf(reserDetailId));
        return prefix + formatDate + vendorIdStr + reserDetailIdStr;
    }

    private static String joint(Integer length, String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - str.length(); i++) {
            sb.append("0");
        }
        return sb.append(str).toString();
    }
}
