/**
 * 
 */
package com.taikang.business.common.utils;

import java.math.BigDecimal;

/** 
 * @ProjectName:tmgsp
 * @User:Picc&&Sinosoft
 * @Title: CheckNull.java 
 * @Package cn.com.sinosoft.common.util.common 
 * @Description: 检查String类型Null值类 
 * @author itw_denghj
 * @date 2018年11月12日20:09:37
 * @version V1.0 
 */
public class CheckNull {

	/**
	 * 将String的Null值转换为字符串空.
	 * 
	 * @param value
	 *            要检查的字符串值
	 * @return 处理后的字符串
	 */
	public static String check(String value) {
		if (value != null) {
			return value;
		} else {
			return "";
		}
	}
	/**
	 * 判断字符串是否是空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNullOfStr(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 *  itw_denghj
	 * @param value
	 * @return
	 */
	public static BigDecimal checkBigDecimal(BigDecimal value) {
		if (value != null) {
			return value.setScale(2, BigDecimal.ROUND_HALF_UP);
		} else {
			return new BigDecimal(0.00);
		}
	}

	/**
	 * BigDecimal类型转换为字符串
	 * @param value
	 * @return
	 */
	public static String bigDecimalToString(BigDecimal value) {
		if (value != null) {
			return value.toString();
		} else {
			return "";
		}
	}
	
	
	/**
	 *  sunjie
	 * @param value
	 * @return
	 */
	public static int checkInteger(Integer value) {
		if (value != null) {
			return value;
		} else {
			return 0;
		}
	}
	/**
	 * 将String的Null值转换为0.
	 * 
	 * @param value
	 *            要检查的字符串值
	 * @return 处理后的字符串
	 */
	public static double checkDouble(String value) {
		if (value != null && value.length()>0) {
			return Double.parseDouble(value);
		} else {
			return 0.00;
		}
	}

	/**
	 * 将String的Null值转换为0.
	 * 
	 * @param value
	 *            要检查的字符串值
	 * @return 处理后的字符串
	 */
	public static int checkInt(String value) {
		if (value != null && (value.trim().length() > 0)) {
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	/**
	 * 判断字符串是否为null并对其作trim处理
	 * */
	public static String checkNull(String s) {
		s = (s == null || s.equalsIgnoreCase("null")) ? "" : s.trim();
		return s;
	}
	
	public static String checkInput(String value) {
		if (value == null) {
			return value;
		} else {
			return "'" + value + "'";
		}
	}
   /**
    * 判断是否为空如是null 返回false  如不是返回turn
    * @param s
    * @return
    */
	public static boolean checkNotNull(String s) {
		boolean b = false;
		if (s == null || s.length() < 1) {
			b = false;
		} else {
			b = true;
		}
		return b;
	}

	public static String checkIsNullReturn0(String s) {
		if (s != null && s.trim().length() > 0) {
			return s;
		} else {
			return "0";
		}
	}

	public static String checkIsNANReturn0(String s) {
		if (checkIsNullReturn0(s).equals(s) && !"NAN".equals(s) && !"NaN".equals(s)) {
			return s;
		} else {
			return "0";
		}
	}

	public static String checkIsNullNew(String s) {
		if (s == null || s.equals("null")) {
			return "";
		} else {
			return s;
		}
	}
	
	/**
    * 判断是否为空如是null 返回新对象  如不是返回object本身
    * @param object 需要判断的对象  对象的Class
    * @return object
    * @author itw_denghj
    */
	public static Object checkIsNullObJect(Object object, Class c){
		if (object == null) {
			try {
				object = c.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return object;
	}
	
	/**
    * 判断是否为空如是null或空，返回0
    * @param String 需要校验的值
    * @return String
    * @author itw_denghj
    */	
    public static String nulltoZeroString(String strnew) {
   	 	String str = strnew;
   	 	if(str == null || str.equals("")){
   	 		str = "0";
   	 	}
   	 	return str;
   }
    
	/**
     * 判断是否为空如是null或空，返回0(int),否则直接转换为int类型
     * @param String 需要校验的值
     * @return int
     * @author itw_denghj
     */	
	public static int nulltoZeroInt(String strnew) {
		int str = Integer.parseInt(nulltoZeroString(strnew));
		return str;
	}
     
 	/**
      * 判断是否为空如是null或空，返回0(double),否则直接转换为double类型
      * @param String 需要校验的值
      * @return double
      * @author itw_denghj
      */	
	public static double nulltoZeroDouble(String strnew) {
		double str = Integer.parseInt(nulltoZeroString(strnew));
		return str;
	}

	public static String nullToString(String iString) {
		return (iString == null || iString.equals("") || iString.equals("null")) ? "" : iString;
	}

	public static String nullToLongZero(String iString) {
		return (iString == null || iString.equals("") || iString.equals("null")) ? "0" : iString;
	}
}
