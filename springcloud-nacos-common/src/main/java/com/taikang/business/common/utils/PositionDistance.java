package com.taikang.business.common.utils;

/**
 * 两点经纬度直线距离的计算
 * Created by baoznst on 2018/9/10.
 */
public class PositionDistance {
    private static double EARTH_RADIUS = 6378.137;// 单位千米


    /**
     * 角度弧度计算公式 rad:().
     * 360度=2π π=Math.PI
     * x度 = x*π/360 弧度
     *
     * @param degree
     * @return
     */
    private static double getRadian(double degree) {
        return degree * Math.PI / 180.0;
    }

    /**
     * 依据经纬度计算两点之间的距离，单位千米
     * @param lat1
     *             1点的纬度
     * @param lng1
     *             1点的经度
     * @param lat2
     *             2点的纬度
     * @param lng2
     *             2点的经度
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = getRadian(lat1);
        double radLat2 = getRadian(lat2);
        double a = radLat1 - radLat2;// 两点纬度差
        double b = getRadian(lng1) - getRadian(lng2);// 两点的经度差
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return s;
    }

    /**
     *
     * 122.28,43.61 通辽
     * 111.70,40.79 呼和浩特
     * 114.88,40.77 张家口
     * 116.37,39.92 北京市
     * 北京到通辽     639652
     * 北京到呼和浩特 407769
     * 北京到张家口   157902
     * @param ar
     */
    public static void main(String ar[]) {
        double dis = getDistance(40.77, 114.88, 39.92, 116.37);

        System.out.println("###################dis:"+dis);
    }

}
