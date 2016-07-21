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
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
