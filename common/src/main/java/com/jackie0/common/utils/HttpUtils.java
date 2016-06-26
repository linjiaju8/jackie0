package com.jackie0.common.utils;

import com.jackie0.common.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HTTP接口请求封装
 * ClassName:HttpUtils <br/>
 * Date:     2015年09月06日 15:24 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

    /**
     * POST请求
     *
     * @param url   请求地址
     * @param param 参数
     * @return 结果
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> param) throws IOException {
        return handleRequest(url, param, RequestMethod.POST);

    }

    private static String handleRequest(String url, Map<String, String> param, RequestMethod requestMethod) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod(requestMethod.name());
        // 解决乱码问题
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");

        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
        String content = getContentByParam(param);
        /*"userID=" + URLEncoder.encode("一个大肥人", "UTF-8");
        content += "&pswd=" + URLEncoder.encode("两个个大肥人", "UTF-8");*/
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
        if (StringUtils.isNotBlank(content)) {
            out.writeBytes(content);
        }
        out.flush();
        out.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Constant.DEF_ENC));
        String line;
        StringBuilder httpResult = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            httpResult.append(line);
        }
        LOGGER.info("Http接口:{}，返回值-->{}", url, httpResult.toString());
        reader.close();
        connection.disconnect();
        return StringUtils.isBlank(httpResult) ? null : httpResult.toString();
    }

    /**
     * POST请求
     *
     * @param url   请求地址
     * @param param 参数
     * @return 结果
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> param) throws IOException {
        return handleRequest(url, param, RequestMethod.GET);
    }

    private static String getContentByParam(Map<String, String> param) throws UnsupportedEncodingException {
        if (param != null && param.size() > 0) {
            StringBuilder content = new StringBuilder();
            int index = 0;
            for (Map.Entry<String, String> entry : param.entrySet()) {
                if (index > 0) {
                    content.append("&");
                }
                content.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), Constant.DEF_ENC));
                index++;
            }
            return content.toString();
        } else {
            return null;
        }
    }
}
