package com.jackie0.showcase.react.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * React.js演示控制器
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-29 17:51
 */
@Controller
public class ReactController {
    /**
     * 首页
     *
     * @return 首页
     */
    @RequestMapping("/react")
    public String toIndex() {
        return "react/index.html";
    }

    /**
     * 登录页
     *
     * @return 登录视图
     */
    @RequestMapping("/react/login")
    public String toLogin() {
        return "react/login.html";
    }
}
