package com.jackie0.showcase.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * showcase视图演示控制器类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-29 16:04
 */
@Controller
public class ViewTesterController {
    /**
     * 测试jsp作为视图，spring-boot默认不再支持jsp，需要使用jsp参考：welcome.jsp注释部分
     */
    @RequestMapping("/jsp")
    public String toJspView() {
        return "jsp-view.jsp";
    }

    /**
     * 测试html视图
     *
     * @return
     */
    @RequestMapping("/html")
    public String toHtmlView() {
        return "html-view.html";
    }
}
