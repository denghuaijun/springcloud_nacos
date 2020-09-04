package com.taikang.business.common.utils;


import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okhttp3.FormBody.Builder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * create by itw_wangjian05
 * <p>
 * on 2018/10/25
 */
public class HttpUtil {
    public static final MediaType MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
    protected static Logger log = LoggerFactory.getLogger(HttpUtil.class);
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000)
            .setConnectionRequestTimeout(60000).build();
    private static OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).build();
    private static HttpURLConnection conn;

    public static String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    public static String post(String url) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, "");
        Request request = new Request.Builder().url(url).post(body).build();
        return execute(request);
    }

    public static String post(String data, String url) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, data);
        log.info("body:{}", body.toString());
        Request request = new Request.Builder().url(url).post(body).build();
        String temp = "";
        try {
            temp = execute(request);
        } catch (IOException e) {
            temp = execute(request);
        }


        return temp;
    }

    public static String postText(String data, String url) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_TEXT, data);
        Request request = new Request.Builder().url(url).post(body).build();
        return execute(request);
    }

    /**
     * post请求 设置请求头参数
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, JSONObject param) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, "");
        Request request = new Request.Builder().addHeader("Timestamp", (String) param.get("Timestamp"))
                .addHeader("appId", "5").addHeader("Authorization", (String) param.get("Authorization")).url(url)
                .post(body).build();
        return execute(request);
    }

    /* 车险上传图片，图片内容为byte数组，data过大不做记录 */
    public static String postWithoutParamLog(String data, String url) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, data);
        Request request = new Request.Builder().url(url).post(body).build();
        return execute(request);
    }

    private static String execute(Request request) throws IOException {
        Response response = client.newCall(request).execute();
//        log.info("response:{}",response);
        if (!response.isSuccessful()) {
            throw new IOException("error code:" + response);
        }
        ResponseBody responseBody = response.body();

        String result = responseBody.string();
//        log.info("result:{}",result);
        responseBody.close();
        return result;
    }

    /**
     * post提交表单
     *
     * @param map
     * @param url
     * @return
     * @throws Exception
     */
    public static String postForm(Map<String, String> map, String url) throws Exception {
        RequestBody body = FormBody.create(MEDIA_TYPE_FORM, map.toString());
        Builder builder = new FormBody.Builder();
        // 模拟表单
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("无法解析的code===========" + response);
        ResponseBody responseBody = response.body();
        String result = responseBody.string();
        responseBody.close();
        return result;
    }


    /**
     * 好医通 post提交表单
     *
     * @param map
     * @param url
     * @return
     * @throws Exception
     */
    public static String postForm(Map<String, String> map, String url, String JWTToken) throws Exception {
        // RequestBody body = FormBody.create(MEDIA_TYPE_FORM, map.toString());
        Builder builder = new FormBody.Builder();
        // 模拟表单
        for (String key : map.keySet()) {
            if (key != null && map.get(key) != null)
                builder.add(key, map.get(key));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).addHeader("token", JWTToken).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("无法解析的code===========" + response);
        }
        ResponseBody responseBody = response.body();
        String result = responseBody.string();
        responseBody.close();
        return result;
    }

    /* *
     * https 请求
     *
     * @param url
     * @param
     * @return
     * @throws IOException
     */
    public static String sendHttpsPost(String url, String jsonObject) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000).build();
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = null;
        String responseContent = null;
        //log.info("要访问的url是：{},post方式提交的json为：{}", url, jsonObject);
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity = new StringEntity(jsonObject, "utf-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory())
                    .setDefaultRequestConfig(requestConfig).build();
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                responseContent = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            }
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                httpResponse.close();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
        //log.info("调用接口返回结果为：{}", responseContent);
        return responseContent;
    }

    // 创建SSL安全连接
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }

    /**
     * post请求 设置请求头参数(专门对泰康集团的HTTP方法)
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String sendPostAddHeader(String url, String token, JSONObject param) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, param.toJSONString());
        Request request = new Request.Builder().addHeader("Authorization", token).url(url).post(body).build();
        return execute(request);
    }

    /**
     * post请求 设置请求头参数(专门对泰康集团的HTTP方法)
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String sendCiitcPost(String url, JSONObject headerParam, JSONObject param) throws IOException {
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, param.toJSONString());
        Request request = new Request.Builder().addHeader("Authorization", headerParam.getString("token"))
                .addHeader("Appkey", headerParam.getString("appkey"))
                .addHeader("insurerUuid", headerParam.getString("insurerUuid")).url(url).post(body).build();
        return execute(request);
    }

    /**
     * post请求 设置请求头参数(专门对趣医的HTTP方法)
     *
     * @param httpUrl
     * @param key
     * @param param
     * @return
     */
    public static String doPost(String httpUrl, String key, String param) {
        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;

        try {
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            // 设置通用参数
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            connection.setRequestProperty("Content-Type", "application/json");
            // 设置授权
            connection.addRequestProperty("Authorization", key);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            os.write(param.getBytes());// 把需要传送的参数发送给远程url
            // 通过连接对象获取一个输入流，向远程读取
            is = connection.getInputStream();
            // 对输入流对象进行包装
            br = new BufferedReader(new InputStreamReader(is, "gbk"));

            StringBuffer sbf = new StringBuffer();
            String temp = null;
            // 循环遍历一行一行读取数据
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
            log.info("logKey:{}发送请求成功:{},入参:{},返回值:{}", "趣医", httpUrl, param, result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.info("logKey:{},请求状态异常url:{},入参:{}", "趣医", httpUrl, param, e);
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }

    /**
     * 数联易康指定post
     * <p>
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            log.info("要访问的URL:{},Param:{},conn:{}", url, param, conn);
//            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(60000);
            // conn.connect();
//            conn.setRequestMethod("POST");
//            conn.setUseCaches(false);
//            conn.setInstanceFollowRedirects(true);
//            conn.setRequestProperty("connection", "Keep-Alive");
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 POST 请求出现异常:{}", e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url         发送请求的URL
     * @param param       请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @param ContentType application/x-www-form-urlencoded
     * @return URL所代表远程资源的响应
     */
    public static String sendPostZFB(String url, String param, String ContentType) {
        String result = null;// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        try {
            // 创建URL对象
            URL connURL = new URL(url);
            // 打开URL连接
            HttpURLConnection httpConn = (HttpURLConnection) connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            httpConn.setRequestProperty("Content-Type", ContentType);
            // 设置POST方式
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            httpConn.setConnectTimeout(60000);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), StandardCharsets.UTF_8));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
            log.info("调用接口成功url:{},入参:{},返回值:{}", url, param, result);
        } catch (Exception e) {
            result = null;
            log.info("调用接口异常url:{},入参:{}", url, param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 用于测试调用新架构的Http方法
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, Map<String, String> param) throws IOException {
        System.out.println("要访问的url是：" + url);
        System.out.println("post方式提交的json为：" + param);
        Builder builder = new Builder();
        for (String key : param.keySet()) {
            builder.add(key, param.get(key));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return execute(request);
    }

    /**
     * 至诚阿福风险Http
     *
     * @param url
     * @param param
     * @return
     */
    public static String doPost(String url, List<NameValuePair> param, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            httpPost.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 同盾信贷保镖Http
     *
     * @param params
     * @return
     */

    public static String httpTongDunMap(Map<String, Object> params) {
        try {
            String urlString = new StringBuilder().append(params.get("apiUrl")).append("?partner_code=").append(params.get("partner_code")).append("&partner_key=").append(params.get("partner_key")).append("&app_name=").append(params.get("app_name")).toString();
            params.remove("apiUrl");
            params.remove("partner_code");
            params.remove("partner_key");
            params.remove("app_name");
            log.info("同盾拼接的请求地址{}",urlString);
            URL url = new URL(urlString);
            // 组织请求参数
            StringBuilder postBody = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() == null) continue;
                postBody.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),
                        "utf-8")).append("&");
            }

            if (!params.isEmpty()) {
                postBody.deleteCharAt(postBody.length() - 1);
            }

            conn = (HttpURLConnection) url.openConnection();
            // 设置长链接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置连接超时
            conn.setConnectTimeout(1000);
            // 设置读取超时
            conn.setReadTimeout(3000);
            // 提交参数
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(postBody.toString().getBytes(StandardCharsets.UTF_8));
            conn.getOutputStream().flush();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log.warn("[HttpUtil--httpTongDunMap] invoke failed, response status:" + responseCode);
                return null;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line).append("\n");
            }
            return result.toString().trim();
        } catch (Exception e) {
            log.error("[HttpUtil--httpTongDunMap] invoke throw exception, details: " + e);
        }

        return null;
    }

    /**
     * 法海接口Http
     *
     * @param url
     * @return
     */

    public static String queryGET(String url) throws Exception {
        log.info("进入法海请求!");
        StringBuilder sb = new StringBuilder();
            URL url1 = new URL(url);
//            URI uri = new URI(url1.getProtocol(), url1.getHost(),url1.getPath(), url1.getQuery(), null);
            URI uri = new  URI(url1.getProtocol(),"", url1.getHost(), url1.getPort(),url1.getPath(), url1.getQuery(), null);
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uri);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instreams = entity.getContent();
                BufferedReader bf = new BufferedReader(new InputStreamReader(instreams,"utf-8"));
                // 文件处理方式
                String line = null;
                while ((line = bf.readLine()) != null) {
                    sb.append(line + "\n");
                }
                httpGet.abort();
            }
        log.info("法海请求完成!");
        return sb.toString();
    }

    /**
     * post提交表单
     *
     * @param requestBody
     * @param url
     * @return
     * @throws Exception
     */
    public String postForm(RequestBody requestBody, String url) throws Exception {
        Request request = new Request.Builder().url(url).post(requestBody).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("无法解析的code===========" + response);
        ResponseBody responseBody = response.body();
        String result = responseBody.string();
        log.info("调用接口返回结果为：{}", result);
        responseBody.close();
        return result;
    }

}



