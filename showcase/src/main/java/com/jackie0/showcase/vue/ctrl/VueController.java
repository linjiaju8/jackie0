package com.jackie0.showcase.vue.ctrl;

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
public class VueController {
    /**
     * 首页
     *
     * @return 首页
     */
    @RequestMapping("/vue")
    public String toIndex() {
        return "vue/index.html";
    }
}
