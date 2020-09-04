package com.taikang.business.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 *@author itw_liwd01
 *@date  汇法的get请求
 */
public class HttpHuiFa {
    private static int connectTimeOut = 5000;


    private static int readTimeOut = 10000;


    private static String requestEncoding = "utf-8";



    public static int getConnectTimeOut() {
        return HttpHuiFa.connectTimeOut;
    }


    public static int getReadTimeOut() {
        return HttpHuiFa.readTimeOut;
    }

    public static String getRequestEncoding() {
        return requestEncoding;
    }


    public static void setConnectTimeOut(int connectTimeOut) {
        HttpHuiFa.connectTimeOut = connectTimeOut;
    }

    public static void setReadTimeOut(int readTimeOut) {
        HttpHuiFa.readTimeOut = readTimeOut;
    }


    public static void setRequestEncoding(String requestEncoding) {
        HttpHuiFa.requestEncoding = requestEncoding;
    }


    /**
     *
     * 发送带参数的GET的HTTP请求
     *

     *
     * @param reqUrl HTTP请求URL
     * @param parameters 参数映射表
     * @return HTTP响应的字符串
     */
    public static String doGet(String reqUrl, Map parameters, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            //把map里面的k,v使用迭代器追加到params
            for (Iterator iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Map.Entry element = (Map.Entry) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), HttpHuiFa.requestEncoding));
                params.append("&");
            }
            //删除最后面的&
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            //System.out.println(url);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpHuiFa.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpHuiFa.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时

            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in, recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            while (tempLine != null) {
                temp.append(tempLine);
                temp.append(crlf);
                tempLine = rd.readLine();
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }

        return responseContent;
    }

}
