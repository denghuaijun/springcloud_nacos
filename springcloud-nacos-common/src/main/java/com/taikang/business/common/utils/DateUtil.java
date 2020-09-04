package com.taikang.business.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * 日期公共类
 * @author itw_zhaogq
 *
 */

public class DateUtil {

	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
	/**
	 * 字符串类型 转换为日期，指定日期格式
	 * @author itw_denghj
	 * @param dateTime
	 * @return
	 */
	public static Date stringToDate(String dateTime, String format)throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date result = null;
		if (dateTime != null && !"".equals(dateTime)) {
			result = sdf.parse(dateTime);
		}
		return result;
	}
	/**
	 * Date类型 转换为字符串，指定日期
	 * @author itw_denghj
	 * @param dateTime
	 * @return
	 */
	public static String dateToString(Date dateTime, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String result = "";
		if (dateTime != null) {
			result = sdf.format(dateTime);
		}
		return result;
	}
	/**
	 *格式化当前日期 eg:2018-9-14
	 * @author itw_denghj
	 */
	public  static String getCurrentDate(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date d = c.getTime();
		return  dateFormat.format(d);
	}
	/**
	 * 年月日时分秒+1位随机数    eg:1807151100181984
	 * @return
	 */
	public static String getYearToSSS() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String date = sp.format(new Date());
		String year = date.substring(2, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);

		String hh = date.substring(11, 13);
		String mm = date.substring(14, 16);
		String ss = date.substring(17, 19);

		String sss = date.substring(20, 23);
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		log.info(sb.append(year).append(month).append(day).append(hh).append(mm).append(ss).append(sss)
				.append(r.nextInt(9) + 1).toString());
		return sb.toString();
	}
	/**
	 * 年月日时分秒    eg:20180606075400
	 * @return
	 */
	public static Date getDate(String dateStr) {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		if (!CheckNull.checkNotNull(dateStr)){
			return null;
		}
		String year = dateStr.substring(0, 4);
		String month = dateStr.substring(4, 6);
		String day = dateStr.substring(6, 8);

		String hh = dateStr.substring(8, 10);
		String mm = dateStr.substring(10, 12);
		String ss = dateStr.substring(12, 14);
		StringBuffer sb = new StringBuffer();
		sb.append(year).append("-").append(month).append("-").append(day).append(" ").append(hh).append(":").append(mm).append(":").append(ss);
		try {
			 date = sp.parse(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 年月日 eg:20180715
	 * @return
	 */
	public static String get4YearToDay() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String date = sp.format(new Date());

		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);

		StringBuffer sb = new StringBuffer();
		log.info(sb.append(year).append(month).append(day).toString());

		return sb.toString();
	}

	/**
	 * 年月日 eg:180715
	 * @return
	 */
	public static String getYearToDay() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String date = sp.format(new Date());

		String year = date.substring(2, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);

		StringBuffer sb = new StringBuffer();
		log.info(sb.append(year).append(month).append(day).toString());

		return sb.toString();
	}

	/**
	 * 时分秒 eg:180505
	 * @return
	 */
	public static String getHHToSs() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String date = sp.format(new Date());
		String hh = date.substring(11, 13);
		String mm = date.substring(14, 16);
		String ss = date.substring(17, 19);

		StringBuffer sb = new StringBuffer();
		log.info(sb.append(hh).append(mm).append(ss).toString());
		return sb.toString();
	}

	/**
	 * 耗秒 eg:566
	 * @return
	 */
	public static String getSss() {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		String date = sp.format(new Date());
		String sss = date.substring(20, 23);

		StringBuffer sb = new StringBuffer();
		log.info(sb.append(sss).toString());
		return sb.toString();
	}

	/**
	 * 1位随机数
	 * @return
	 */
	public static String getRandom() {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		log.info(sb.append(r.nextInt(9) + 1).toString());

		return sb.toString();
	}

	public static String getSomeDate(String pattern, int dateNum){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -dateNum);
		Date date = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

	public static String getYYYYMMDD() {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		log.info(date);
		return date;
	}

	/**
	 * 比较max表数据是否有效数据
	 *
	 * @param max_time
	 * @param interval
	 * @return
	 */
	public static boolean getIntervalFalg(Date max_time, Integer interval, String serviceCode) {
		boolean falg = true;
		// 日期相减算出秒的算法
		try {
			// 今天的时间
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			log.info("当前日期>>>"+sdf.format(date));

			// 日期相减得到相差的日期天数
			long day = (date.getTime() - max_time.getTime()) / (24 * 60 * 60 * 1000);

			log.info("当前日期和 <<<"+serviceCode+">>>的hd_call_vendor表最大日期相差<<<"+ day+">>>天." );
			// 相减日期的数大于供应商频率返回true,说明库里数据不是最新的数据了，需要更新
			if (day > interval) {
				falg = false;
			}
		} catch (Exception e) {
			log.error("日期相减算法出问题了>>>", e);
		}
		return falg;
	}

	public static Date createExpireTime(Integer interval, String code) {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DAY_OF_MONTH, interval);
		Date expireTime = c.getTime();
		log.info("<<<"+code+">>>存放到hd_call_vendor的过期时间是>>>"+expireTime);
		return expireTime;
	}
	
	/**
	 * 年月日时分秒    eg:20180606075400
	 * @return
	 */
	public static String get4YDate(String dateStr) {
		if (!CheckNull.checkNotNull(dateStr)){
			return null;
		}
		String year = dateStr.substring(0, 4);
		String month = dateStr.substring(4, 6);
		String day = dateStr.substring(6, 8);
		StringBuffer sb = new StringBuffer();
		sb.append(year).append("-").append(month).append("-").append(day);
		return sb.toString();
	}
	
	/**
	 * 获取随机数
	 * @param length
	 * @return
	 */
	public static String getRandom(int length) {
		String fomat = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		StringBuilder stringBuilder = new StringBuilder();
		Random rm = new Random();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(rm.nextInt(9));
		}
		String random = fomat + stringBuilder.toString();
		return random;
	}
	
	/**
     * 获取时间戳
     *
     * @param str
     * @return
     */
    public static String getFormatDate(String str) {
        String format = "";
        if ((str != null) || (str != "")) {
            long date = Long.parseLong(str);
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(date));
        }
        return format;
    }
    
    /**
	 *格式化日期 eg:2018-9-14 成为 Wed Jun 06 00:00:00 CST 2018 （date格式）
	 * @author itw_denghj
     * @throws ParseException 
	 */
	public static Date getNewCurrentDate(String str) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(str);
	}
	
	/**
	 *格式化日期 eg:2018/9/14 成为 Wed Jun 06 00:00:00 CST 2018 （date格式）
	 * @author itw_denghj
     * @throws ParseException 
	 */
	public static Date get4NewCurrentDate(String str) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return dateFormat.parse(str);
	}
	
	/**
	 *格式化日期 eg:20180914121212 成为 Wed Jun 06 00:00:00 CST 2018 （date格式）
	 * @author itw_denghj
     * @throws ParseException 
	 */
	public static Date get2NewCurrentDate(String str) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		return dateFormat.parse(str);
	}

	/**
	 * 得到当前日期年月日
	 * 格式：yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static String getYYYYToDay() throws ParseException {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		String date = sp.format(new Date());
		return date;

	}

	/**
	 * 得到2年前日期年月日
	 * 格式：yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 */
	public static String getTwoYearAgo() throws ParseException {
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR,-2);
		Date y = c.getTime();
		String year = sp.format(y);
		return year;
	}

	/**
	 * 定义pNo(保单号)随机数
	 * @param length
	 * @return
	 */
	public static String getpNoRandom(int length) throws Exception  {
		String fomat = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		StringBuilder stringBuilder = new StringBuilder();
		Random rm = new Random();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(rm.nextInt(9));
		}
		String random = "TKJYL" + fomat + stringBuilder.toString();
		return random;
	}

	/**
	 * 海德供应商生成
	 * @param length
	 * @return
	 */
	public static String getHaiDeRandom(int length) {
		String fomat = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		StringBuilder stringBuilder = new StringBuilder();
		Random rm = new Random();
		for (int i = 0; i < length; i++) {
			stringBuilder.append(rm.nextInt(length));
		}
		String random = "TKCN"+fomat + stringBuilder.toString();
		return random;
	}

	/**
	 * interval 天数
	 * @param interval
	 * @return
	 */
	public static Date createExpireTime(Integer interval) {
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DAY_OF_MONTH, interval);
		Date expireTime = c.getTime();
		//log.info("存放到hd_comany_taxinfo的过期时间是>>>{}",expireTime);
		return expireTime;
	}

	/**
	 *  eg:20190129000100 转换成 2019-01-29 00:01:00
	 * @return
	 */
	public static String getYearToSS(String dateStr) {

		if (!CheckNull.checkNotNull(dateStr)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try{
			String year = dateStr.substring(0, 4);
			String month = dateStr.substring(4, 6);
			String day = dateStr.substring(6, 8);

			String hh = dateStr.substring(8, 10);
			String mm = dateStr.substring(10, 12);
			String ss = dateStr.substring(12, 14);
			sb.append(year).append("-").append(month).append("-").append(day).append(" ").append(hh).append(":").append(mm).append(":").append(ss);
			System.out.println("格式日期："+sb);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}

	/**
	 *  eg:2018-07-16 转换成 2019-01-29 00:01:00
	 * @return
	 */
	public static String addHHToSS(String dateStr) {

		if (!CheckNull.checkNotNull(dateStr)){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(dateStr).append(" ").append("00").append(":").append("00").append(":").append("00");
		System.out.println("格式日期："+sb);
		return sb.toString();
	}

	/**
	 *  eg:20180716 转换成 2019-01-29 00:01:00
	 * @return
	 */
	public static String addNewHHToSS(String dateStr) {

		if (!CheckNull.checkNotNull(dateStr)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			String year = dateStr.substring(0, 4);
			String month = dateStr.substring(4, 6);
			String day = dateStr.substring(6, 8);
			sb.append(year).append("-").append(month).append("-").append(day).append(" ").append("00").append(":").append("00").append(":").append("00");
			System.out.println("格式日期：" + sb);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return sb.toString();
	}

	/**
	 * 年月日时分秒    eg:20180606075400
	 * @return
	 */
	public static String getDate_String(String dateStr) {
		if (!CheckNull.checkNotNull(dateStr)){
			return null;
		}
		if (dateStr.length()<4){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String year = "00";
		String month = "00";
		String day = "00";

		String hh = "00";
		String mm = "00";
		String ss = "00";
		if (dateStr.length()>=4) {
			year = dateStr.substring(0, 4);
			sb.append(year);
		}
		if (dateStr.length()>=6){
			month = dateStr.substring(4, 6);
			sb.append("-").append(month);
		}
		if (dateStr.length()>=8){
			day = dateStr.substring(6, 8);
			sb.append("-").append(day);
		}

		if (dateStr.length()>=10){
			hh = dateStr.substring(8, 10);
			sb.append(" ").append(hh);
		}
		if (dateStr.length()>=12){
			mm = dateStr.substring(10, 12);
			sb.append(":").append(mm);
		}
		if (dateStr.length()>=14){
			ss = dateStr.substring(12, 14);
			sb.append(":").append(ss);
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		System.out.println(addNewHHToSS("20180716"));

	}
}
