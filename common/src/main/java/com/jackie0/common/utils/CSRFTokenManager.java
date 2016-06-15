package com.jackie0.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;

/**
 * A manager for the CSRF token for a given session. The {@link #getTokenForSession(String)} should used to obtain
 * the token value for the current session (and this should be the only way to obtain the token value).
 */
public final class CSRFTokenManager {

    /**
     * The token parameter name
     */
    static final String CSRF_PARAM_NAME = "csrfRequestToken";

    /**
     * The location on the session which stores the token
     */
    public final static String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CSRFTokenManager.class.getName() + ".tokenval";

    /**
     * 加密方式
     */
    @Value("${shiro.password.algorithmName}")
    private static String algorithmName = "md5";

    /**
     * 迭代深度
     */
    @Value("${shiro.password.hashIterations}")
    private static int hashIterations = 2;

    /**
     * 获取session(Shiro的Session而非HttpSession)的Token值，支持每个页面存的Token值不同
     *
     * @param page 配置在app.properties中checkForgeryUrl的某个页面
     * @return session的Token值
     */
    public static String getTokenForSession(String page) {
        return getTokenForSession(page, false);
    }

    /**
     * 获取session(Shiro的Session而非HttpSession)的Token值，支持每个页面存的Token值不同
     * flushTag为true时不管有没有值都刷新Token值
     *
     * @param page     配置在app.properties中checkForgeryUrl的某个页面
     * @param flushTag 是否刷新Session中的Token值，true刷新，false不刷新，同时Token为空时一定刷新
     * @return session的Token值
     */
    public static String getTokenForSession(String page, boolean flushTag) {
        RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
        // init the token concurrently
        Session session = SecurityUtils.getSubject().getSession();
        String token = (String) session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME + page);
        if (null == token || flushTag) {
            String random = String.valueOf(new SecureRandom().nextLong());
            token = new SimpleHash(
                    algorithmName,
                    random,
                    ByteSource.Util.bytes(randomNumberGenerator.nextBytes().toHex()),
                    hashIterations).toHex();
            session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME + page, token);
        }
        return token;
    }

    /**
     * Extracts the token value from the session
     *
     * @param request http请求
     * @return Token值
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getParameter(CSRF_PARAM_NAME);
        if (StringUtils.isBlank(token)) {
            token = request.getHeader(CSRF_PARAM_NAME);
        }
        return token;
    }

}
