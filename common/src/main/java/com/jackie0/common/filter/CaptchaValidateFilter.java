package com.jackie0.common.filter;

import com.jackie0.common.utils.ValidatorUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 验证码验证过滤器
 * ClassName:CaptchaValidateFilter <br/>
 * Date:     2015年08月07日 16:24 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class CaptchaValidateFilter extends AccessControlFilter {

    static final Logger LOGGER = LoggerFactory.getLogger(CaptchaValidateFilter.class);
    public static final String CAPTCHA_ERROR = "captcha.error";

    private boolean captchaEbabled = true;//是否开启验证码支持
    private String captchaParam = "kaptcha";//前台提交的验证码参数名
    private String failureKeyAttribute = "shiroLoginFailure"; //验证失败后存储到的属性名

    public void setCaptchaEbabled(boolean captchaEbabled) {
        this.captchaEbabled = captchaEbabled;
    }

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

    public void setFailureKeyAttribute(String failureKeyAttribute) {
        this.failureKeyAttribute = failureKeyAttribute;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
        request.setAttribute("jcaptchaEbabled", captchaEbabled);
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //2、判断验证码是否禁用或不是表单提交（允许访问）
        if (!captchaEbabled || !RequestMethod.POST.name().equals(httpServletRequest.getMethod())) {
            return true;
        }
        //3、密码错误一定次数时，才效验 验证码

        //4、此时是表单提交，验证验证码是否正确
        return ValidatorUtils.validateCaptcha(httpServletRequest, httpServletRequest.getParameter(captchaParam));
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //如果验证码失败了，存储失败key属性
        request.setAttribute(failureKeyAttribute, "captcha.error");
        return true;
    }
}
