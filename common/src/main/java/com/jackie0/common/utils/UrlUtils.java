package com.jackie0.common.utils;


import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 项目URL工具类，提供获取资源文件及各种路径的方法
 *
 * @author zhouxf
 * @version 1.0.0
 * @date 2014/12/29 18:44
 */
public final class UrlUtils {

    private UrlUtils() {
    }

    /**
     * 获取客户端真实Ip
     *
     * @param request 请求对象
     * @return 客户端真实ip
     */
    public static String getRemoteIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 获取指定资源文件的绝对路径
     *
     * @param resourceName 资源文件名称
     * @return 资源文件绝对路径
     */
    public static URL getResourceURL(String resourceName) {
        return UrlUtils.class.getClassLoader().getResource(resourceName);
    }

    /**
     * 获取指定资源文件流
     *
     * @param resourceName 资源文件名称
     * @return 资源文件输入流
     */
    public static InputStream getResourceStream(String resourceName) {
        return UrlUtils.class.getClassLoader().getResourceAsStream(resourceName);
    }

    /**
     * 获取Scheme http://
     *
     * @param request HttpServletRequest对象
     * @return http://
     */
    public static String getScheme(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getScheme() + "://";
    }

    /**
     * 获取Header localhost:8080
     *
     * @param request HttpServletRequest对象
     * @return header host如：localhost:8080
     */
    public static String getHeader(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader("host");
    }

    /**
     * 获取ContextPath /jn-b2c
     *
     * @param request HttpServletRequest对象
     * @return ContextPath /jn-b2c
     */
    public static String getContextPath(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getContextPath();
    }


    /**
     * 获取请求地址 /jn-b2c-resource/bootstrap/demo/index.html
     *
     * @param request HttpServletRequest对象
     * @return 请求地址如：/jn-b2c-resource/bootstrap/demo/index.html
     */
    public static String getRequestURI(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getRequestURI();
    }

    /**
     * 获取访问项目上下文 http://localhost:8080/jn-b2c/
     *
     * @return 上下文如：http://localhost:8080/jn-b2c/
     */
    public static String getCurUrl(HttpServletRequest request) {
        return getScheme(request) + getHeader(request) + getContextPath(request);
    }

    /**
     * 获取真实地址 http://localhost:8080/jn-b2c/bootstrap/demo/index.html
     *
     * @return 真实地址如：http://localhost:8080/jn-b2c/bootstrap/demo/index.html
     */
    public static String getRealPath(HttpServletRequest request) {
        return getScheme(request) + getHeader(request) + getContextPath(request) + getRequestURI(request);
    }

    /**
     * 替换queryString中指定key的值为新值，如果queryString不包含key则把replaceKey、newValue追加到queryString末尾
     *
     * @param queryString request的queryString
     * @param replaceKey  要替换的参数的key
     * @param newValue    新的参数值
     * @return 替换值后的queryString
     */
    public static String replaceOrAppendQueryStringValue(String queryString, String replaceKey, String newValue) {
        String newQueryString = queryString;
        if (StringUtils.isNotBlank(queryString) && StringUtils.isNotBlank(replaceKey) && queryString.contains(replaceKey)) {
            int replaceKeyIndex = queryString.lastIndexOf(replaceKey);
            String subQueryString = queryString.substring(replaceKeyIndex);
            String replaceQueryString = subQueryString;
            if (subQueryString.contains("&")) {
                replaceQueryString = subQueryString.substring(0, subQueryString.indexOf("&"));
            }
            String newValueString = replaceKey + "=" + newValue;
            newQueryString = queryString.replace(replaceQueryString, newValueString);
        } else if (StringUtils.isNotBlank(replaceKey)) {
            if (StringUtils.isBlank(queryString)) {
                newQueryString = replaceKey + "=" + newValue;
            } else {
                newQueryString = queryString + "&" + replaceKey + "=" + newValue;
            }
        }
        return newQueryString;
    }

    /**
     * request的参数转换成Map
     *
     * @param request {@link ServletRequest}
     * @return 转换后的参数
     */
    public static Map<String, String> requestParam2Map(ServletRequest request) {
        Map<String, String> paramMap = null;
        Enumeration<String> paramNames = request.getParameterNames();
        if (paramNames != null && paramNames.hasMoreElements()) {
            paramMap = new HashMap<>();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                paramMap.put(paramName, request.getParameter(paramName));
            }
        }
        return paramMap;
    }
}
