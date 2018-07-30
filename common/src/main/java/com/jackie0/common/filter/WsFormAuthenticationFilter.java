package com.jackie0.common.filter;

import com.jackie0.common.utils.UrlUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义form表单验证过滤器,先通过验证码验证过滤器，如果验证码错误就不再验证用户名密码
 * ClassName:WsFormAuthenticationFilter
 * Date:     2015年08月07日 16:50
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class WsFormAuthenticationFilter extends FormAuthenticationFilter {


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return request.getAttribute(getFailureKeyAttribute()) != null || super.onAccessDenied(request, response, mappedValue);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) throws Exception {
        String successUrl = getSuccessUrl();
        boolean contextRelative = true;
        SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
            successUrl = savedRequest.getRequestUrl();
            contextRelative = false;
        }

        if (successUrl == null) {
            throw new IllegalStateException("Success URL not available via saved request or via the " +
                    "successUrlFallback method parameter. One of these must be non-null for " +
                    "issueSuccessRedirect() to work.");
        }

        WebUtils.issueRedirect(request, response, successUrl, UrlUtils.requestParam2Map(request), contextRelative);
        return false;
    }
}
