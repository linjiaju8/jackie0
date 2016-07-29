package com.jackie0.showcase.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * React.js演示控制器
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-29 17:51
 */
@RestController
public class ReactShowcaseController {
    @RequestMapping("/react/comment")
    public List<Map<String, String>> reactComment() {
        List<Map<String, String>> commentList = new ArrayList<>();
        Map<String, String> comment1 = new HashMap<>();
        comment1.put("author", "jackie0");
        comment1.put("text", "这是第一个组件。");
        Map<String, String> comment2 = new HashMap<>();
        comment2.put("author", "jackie1");
        comment2.put("text", "这是另一个组件。");
        commentList.add(comment1);
        commentList.add(comment2);
        return commentList;
    }
}
