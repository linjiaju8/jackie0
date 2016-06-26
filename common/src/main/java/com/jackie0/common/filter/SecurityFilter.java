package com.jackie0.common.filter;

import com.jackie0.common.utils.CSRFTokenManager;
import com.jackie0.common.utils.I18nUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 安全过滤器，主要防止跨站脚本攻击（XXS）及跨站请求伪造（CSRF）
 * ClassName:SecurityFilter <br/>
 * Date:     2015年08月05日 9:53 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class SecurityFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        boolean checkScriptPass = checkScript(httpServletRequest);
        String url = httpServletRequest.getRequestURI();
        boolean checkForgeryPass = checkForgery(httpServletRequest, url);
        if (checkScriptPass && checkForgeryPass) {
            chain.doFilter(request, response);
        } else {
            String errMsg = !checkScriptPass ? I18nUtils.getMessage("km.ws.XXS") : I18nUtils.getMessage("km.ws.CSRF");
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, errMsg);
        }
    }

    // 验证XXS跨站脚本攻击
    private boolean checkScript(HttpServletRequest httpServletRequest) {
        boolean pass = true;
        Enumeration<String> paramEnum = httpServletRequest.getParameterNames();
        while (paramEnum != null && pass && paramEnum.hasMoreElements()) {
            String paramName = paramEnum.nextElement();
            String paramValue = httpServletRequest.getParameter(paramName);
            pass = paramValue.equals(new HTMLFilter().filter(paramValue));
        }
        return pass;
    }

    // 验证CSRF跨站请求伪造
    private boolean checkForgery(HttpServletRequest httpServletRequest,
                                 String url) {
        boolean pass = true;
        String checkForgeryUrl = ""; // TODO 放到系统参数配置中，不再在Constant常量类配置
        if (StringUtils.isNotBlank(checkForgeryUrl) && StringUtils.isNotBlank(url) && RequestMethod.POST.name().equals(httpServletRequest.getMethod())) {
            // This is a POST request - need to check the CSRF token
            String[] checkForgeryUrls = checkForgeryUrl.split(",");
            // 在application.properties配置了checkForgeryUrl的地址都通过Token验证
            if (ArrayUtils.contains(checkForgeryUrls, url)) {
                String sessionToken = CSRFTokenManager
                        .getTokenForSession(url);
                String requestToken = CSRFTokenManager
                        .getTokenFromRequest(httpServletRequest);
                pass = sessionToken.equals(requestToken);
                SecurityUtils.getSubject().getSession().removeAttribute(CSRFTokenManager.CSRF_TOKEN_FOR_SESSION_ATTR_NAME + url);
            }
        }
        return pass;
    }

    @Override
    public void destroy() {

    }
}
