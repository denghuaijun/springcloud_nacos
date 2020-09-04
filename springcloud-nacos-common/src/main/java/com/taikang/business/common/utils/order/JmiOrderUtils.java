package com.taikang.business.common.utils.order;

/**
 *@author itw_denghj
 * Date:2018/9/27
 * Version:1.0
 */
public abstract class JmiOrderUtils {
    private static Integer prevIndex = 0;
    private static Long currentSecond = System.currentTimeMillis() / 1000;

    private JmiOrderUtils() {
    }

    /**
     * 获取不重复的订单ID号
     *
     * @param jvmIndex jvm序列号，可以通过IP来获得
     * @return
     */
    public synchronized static Long generateOrderId(Integer jvmIndex) {
        Long nowSecond = System.currentTimeMillis() / 1000;
        if (nowSecond > currentSecond) {//是否进行变秒操作
            currentSecond = nowSecond;
            prevIndex = 0;
        }

        //序号进行自增
        ++prevIndex;

        StringBuilder result = new StringBuilder(String.format("%02d", jvmIndex));
        result.append(String.format("%04d", prevIndex));
        result.append(currentSecond);
        return Long.valueOf(result.toString());
    }


//    public static String hitDatabase(List<JmiOrderVO> jmiOrderInfoList) {
//        StringBuilder stringBuffer = new StringBuilder();
//        for (JmiOrderVO jmiOrderInfo : jmiOrderInfoList) {
//            stringBuffer.append(" database：").append(new HashRouterFunction().apply(jmiOrderInfo.getErpOrderId()));
//        }
//        return stringBuffer.toString();
//    }
//
//
//    public static void main(String[] args) throws InterruptedException {
//        final Set tempSet = new HashSet();
//        Long date = System.currentTimeMillis();
////        for (int i = 0; i < 10; i++) {
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    for (int j = 0; j < 1000; j++) {
////                        tempSet.add(JmiOrderUtils.generateOrderId(11));
////                    }
////                }
////            }).start();
////        }
//        for (int j = 0; j < 10000; j++) {
//            System.out.println(generateOrderId(11));
//        }
////        Thread.sleep(2000);
////        System.out.println(System.currentTimeMillis() - date);
////        System.out.println(tempSet.size());
//    }
//
//
//    /**
//     * JmiOrder list TOBE JmiOrderDetail list
//     *
//     * @param esOrderList
//     * @return
//     */
//    public static List<JmiOrderDetail> reSetDetail(List<JmiOrder> esOrderList) {
//
//        List<JmiOrderDetail> detailList = new ArrayList<JmiOrderDetail>();
//
//        if (CollectionUtils.isEmpty(esOrderList)) {
//            return detailList;
//        }
//        JmiOrderDetail detail;
//
//        JmiOrderStatus orderStatus;
//
//        for (JmiOrder esOrder : esOrderList) {
//
//            List<JmiPayMsg> payList = new ArrayList<JmiPayMsg>();
//
//            detail = new JmiOrderDetail();
//
//            orderStatus = new JmiOrderStatus();
//
//            BeanUtils.copyProperties(esOrder, detail);
//            // 支付信息转换
//            payInfoConvert(detail, esOrder, payList);
//            // 订单状态
//            orderStatus.setBusinessOrderStatus(esOrder.getBusinessOrderStatus());
//            orderStatus.setBusinessOrderStatusName(esOrder.getBusinessOrderStatusName());
//            orderStatus.setJmiOrderStatus(esOrder.getJmiOrderStatus());
//            orderStatus.setOrderCenterStatusName(esOrder.getOrderCenterStatusName());
//            detail.setOrderStatus(orderStatus);
//            detailList.add(detail);
//        }
//        return detailList;
//    }
//
//    private static void payInfoConvert(JmiOrderDetail detail, JmiOrder esOrder, List<JmiPayMsg> payList) {
//        if (StringUtils.isNotEmpty(esOrder.getPayInfos())) {
//            Map<Integer, Integer> payMap = JSONArray.parseObject(esOrder.getPayInfos(), HashMap.class);
//            if (!payMap.isEmpty()) {
//                JmiPayMsg payInfo;
//                for (Map.Entry ent : payMap.entrySet()) {
//                    payInfo = new JmiPayMsg();
//                    payInfo.setPayMoney(Integer.valueOf(ent.getValue().toString()));
//                    payInfo.setPayType(Integer.valueOf(ent.getKey().toString()));
//                    payList.add(payInfo);
//                }
//                detail.setPayMsgs(payList);
//            }
//        }
//    }
//
//    /**
//     * JmiOrder TOBE JmiOrderDetail
//     *
//     * @param esOrder
//     * @return
//     */
//    public static JmiOrderDetail reSetDetail(JmiOrder esOrder) {
//
//
//        JmiOrderDetail detail = new JmiOrderDetail();
//
//        JmiOrderStatus orderStatus = new JmiOrderStatus();
//
//        List<JmiPayMsg> payList = new ArrayList<JmiPayMsg>();
//
//        BeanUtils.copyProperties(esOrder, detail);
//        // 支付信息转换
//        payInfoConvert(detail, esOrder, payList);
//
//        orderStatus.setBusinessOrderStatus(esOrder.getBusinessOrderStatus());
//        orderStatus.setBusinessOrderStatusName(esOrder.getBusinessOrderStatusName());
//        orderStatus.setJmiOrderStatus(esOrder.getJmiOrderStatus());
//        orderStatus.setOrderCenterStatusName(esOrder.getOrderCenterStatusName());
//        detail.setOrderStatus(orderStatus);
//
//        return detail;
//    }
//
//
//    /**
//     * 订单对象赋值
//     *
//     * @param jmiOrder
//     * @param orderPayList
//     * @param features
//     */
//    public static void setValues(JmiOrder jmiOrder, List<JmiOrderPay> orderPayList, JSONObject features) {
//        if (null != orderPayList) {
//            Map<Integer, Integer> payMap = new HashMap<Integer, Integer>();
//            for (JmiOrderPay orderPay : orderPayList) {
//                payMap.put(orderPay.getPayType(), orderPay.getPayMoney());
//            }
//            jmiOrder.setPayInfos(JSON.toJSONString(payMap));
//        }
//        if (null != features) {
//            jmiOrder.setFeatures(JSON.toJSONString(features));
//        }
//    }
}
