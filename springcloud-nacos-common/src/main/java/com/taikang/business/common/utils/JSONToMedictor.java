package com.taikang.business.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONToMedictor {
	/**
	 * 封装返回成功的 json
	 *
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String getObject(String code, String msg,String sendNum) {
		JSONObject object = new JSONObject();
		JSONObject jsonPackage = new JSONObject();
		JSONObject head = new JSONObject();
		// head
		head.put("busseID", "");
		head.put("sendTradeNum", sendNum);
		head.put("senderCode", "");
		head.put("senderName", "");
		head.put("receiverCode", "");
		head.put("receiverName", "");
		head.put("hosorgNum", "");
		head.put("hosorgName", "");
		head.put("systemType", "");
		head.put("busenissType", "");
		head.put("standardVersionCode", "");
		head.put("clientmacAddress ", "");
		// additionInfo
		JSONObject additionInfo = new JSONObject();
		additionInfo.put("errorCode", code);
		additionInfo.put("errorMsg", msg);
		additionInfo.put("receiverTradeNum", "");
		additionInfo.put("correlationId", "");
		additionInfo.put("asyncAsk", "");
		additionInfo.put("callback", "");
		additionInfo.put("curDllAddr", "");
		// body
		JSONArray body = new JSONArray();
		jsonPackage.put("head", head);
		jsonPackage.put("body", body);
		jsonPackage.put("additionInfo", additionInfo);
		object.put("package", jsonPackage);

		return object.toJSONString();
	}
}
