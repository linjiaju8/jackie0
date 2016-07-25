package com.jackie0.platform.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 平台基础控制器类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-20 15:25
 */
@Controller
public class PlatformController {
    /*
    测试jsp作为视图，spring-boot默认不再支持jsp，需要使用jsp参考：welcome.jsp注释部分
    @RequestMapping("/jsp")
    public String index() {
        return "welcome";
    }*/
}
