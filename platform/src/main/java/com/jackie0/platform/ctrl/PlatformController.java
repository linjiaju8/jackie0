package com.jackie0.platform.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台基础控制器类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-20 15:25
 */
@RestController
public class PlatformController {
    @RequestMapping("/test")
    public String index() {
        return "test";
    }
}
