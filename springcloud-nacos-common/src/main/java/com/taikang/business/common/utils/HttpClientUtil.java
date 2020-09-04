package com.taikang.business.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class HttpClientUtil {
    private static Log log = LogFactory.getLog(HttpClientUtil.class);

    //	ConnectTimeout： 链接建立的超时时间；
//	SocketTimeout：响应超时时间，超过此时间不再读取响应；
//	ConnectionRequestTimeout： http clilent中从connetcion pool中获得一个connection的超时时间；
    private final static int CONNECTTIMEOUT = 2000; //链接建立的超时时间
    private final static int SOCKETTIMEOUT = 4000; //响应超时时间，超过此时间不再读取响应；
    private final static int ConnectionRequestTimeout = 500;//从连接池获取的链接的超时时间

    private static PoolingHttpClientConnectionManager cm = null;

    static {//初始化缓存
        try {
            LayeredConnectionSocketFactory sslsf = null;
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslsf)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(400);//最大并发数
            cm.setDefaultMaxPerRoute(400);//单个域名的最大并发数
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
        }
    }

    private static void config(HttpRequestBase method) {
        // 创建默认的httpClient实例.
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTTIMEOUT).setConnectionRequestTimeout(ConnectionRequestTimeout)
                .setSocketTimeout(SOCKETTIMEOUT).build();
        method.setConfig(requestConfig);
    }

    public static CloseableHttpClient getHttpClient() {
        /*CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, true))
                .build();*/

        CloseableHttpClient httpClient = HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接
        return httpClient;
    }

    public static String post(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = getHttpClient();
        String body = null;
        HttpPost post = postForm(url, params);
        post.setConfig(createConfig(7000, false));
        body = invoke(httpClient, post);
        try {
            httpClient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return body;
    }

    public static String postJson(String url, JSONObject json) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost post = new HttpPost(url);
        String result = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = httpClient.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(res.getEntity());
            }
            httpClient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return result;
    }

    public static String post(String url, Map<String, String> params, Header[] headers) {
        CloseableHttpClient httpClient = getHttpClient();
        String body = null;
        HttpPost post = postForm(url, params);
        for (Header header : headers) {
            post.addHeader(header);
        }
        body = invoke(httpClient, post);
        try {
            httpClient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return body;
    }

    public static String get(String url, Map<String, String> headers) {
        CloseableHttpClient httpclient = getHttpClient();
        String body = null;

        HttpGet get = new HttpGet(url);
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }

        }

        body = invoke(httpclient, get);
        try {
            httpclient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return body;
    }

    public static Header[] getResponseHeader(String url, Map<String, String> headers) {
        CloseableHttpClient httpclient = getHttpClient();
        String body = null;

        HttpGet get = new HttpGet(url);
        config(get);
//        get.setConfig(createConfig(5000, false));
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }
        }

        Header[] reqHeaders = invokeResponse(httpclient, get);

        try {
            httpclient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return reqHeaders;
    }

    private static Header[] invokeResponse(CloseableHttpClient httpclient, HttpRequestBase httpost) {
        CloseableHttpResponse response = sendRequest(httpclient, httpost);

        Header[] headers = response.getAllHeaders();

        return headers;
    }


    public static byte[] getByte(String url, Map<String, String> headers) {
        CloseableHttpClient httpclient = getHttpClient();
        byte[] body = null;
        HttpGet get = new HttpGet(url);
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }
        }
        body = invokeByte(httpclient, get);
        try {
            httpclient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return body;
    }

    public static byte[] getByteProxy(String url, Map<String, String> headers, String host, Integer port) {
        CloseableHttpClient httpclient = getHttpClient();
        byte[] body = null;
        HttpGet get = new HttpGet(url);
        config(get);
        get.setConfig(createConfigProxy(5000, true, host, port));
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }
        }
        CloseableHttpResponse response = null;
        int i = 0;
        boolean success = false;
        do {
            response = sendRequest(httpclient, get);
            if (response != null) {
                success = response.getStatusLine().getStatusCode() != 404;
                if (!success) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        log.error("", e);
                    }
                }
            }
            i++;
        } while (i < 3 && !success);

        HttpEntity entity = response.getEntity();
        try {
            body = EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            log.error("", e);
        }
        try {
            response.close();
        } catch (IOException e) {
            log.error("", e);
        }
        try {
            httpclient.close();
        } catch (IOException e) {
            log.error("", e);
        }
        return body;
    }

    public static RequestConfig createConfig(int timeout, boolean redirectsEnabled) {
        return RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setRedirectsEnabled(redirectsEnabled).build();
    }

    public static RequestConfig createConfigProxy(int timeout, boolean redirectsEnabled, String host, Integer port) {
        //代理配置
        HttpHost httpHost = new HttpHost(host, port);
        return RequestConfig.custom().setProxy(httpHost).setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setRedirectsEnabled(redirectsEnabled).build();
    }


    private static String invoke(CloseableHttpClient httpclient, HttpRequestBase method) {
        config(method);
        CloseableHttpResponse response = sendRequest(httpclient, method);
        HttpEntity entity = response.getEntity();

        String body = null;
        try {
            body = EntityUtils.toString(entity);
        } catch (ParseException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }

        return body;
    }

    private static byte[] invokeByte(CloseableHttpClient httpclient, HttpRequestBase method) {
        config(method);
        CloseableHttpResponse response = sendRequest(httpclient, method);
        HttpEntity entity = response.getEntity();
        byte[] body = null;
        try {
            body = EntityUtils.toByteArray(entity);
        } catch (ParseException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return body;
    }

    public static CloseableHttpResponse sendRequest(CloseableHttpClient httpclient, HttpRequestBase method) {

        CloseableHttpResponse response = null;

        try {
            response = httpclient.execute(method);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
        return response;
    }

    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        if (params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }

        return httpost;
    }

    public static void printHeader() {

    }

    public static void main(String[] args) {
//        HttpHost httpHost = new HttpHost("220.88.36.150", 80);
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        httpClientBuilder.setProxy(httpHost);
//        CloseableHttpClient httpclient = httpClientBuilder.build();
//        CookieStore cookieStore = new BasicCookieStore();
        // System.out.println(new String(HttpClientUtil.getByteByProxy("http://1212.ip138.com/ic.asp", HeaderUtil.getMobile("http://www.ip138.com/"), cookieStore)));

        System.out.println(post("http://www.baidu.com", new HashMap<>()));
    }


}