package com.jackie0.showcase.ctrl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * showcase控制器类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-29 16:04
 */
@RestController
public class ShowcaseController {
    private static final List<Map<String, String>> COMMENT_LIST = new ArrayList<>();

    @RequestMapping(value = "/react/comment", method = RequestMethod.GET)
    public List<Map<String, String>> findReactComment() {
        if (CollectionUtils.isEmpty(COMMENT_LIST)) {
            Map<String, String> comment1 = new HashMap<>();
            comment1.put("author", "jackie0");
            comment1.put("text", "默认评论1。");
            Map<String, String> comment2 = new HashMap<>();
            comment2.put("author", "jackie1");
            comment2.put("text", "默认评论2。");
            COMMENT_LIST.add(comment1);
            COMMENT_LIST.add(comment2);
        }
        return COMMENT_LIST;
    }

    @RequestMapping(value = "/react/comment", method = RequestMethod.POST)
    public List<Map<String, String>> createReactComment(String author, String text) {
        if (StringUtils.isNotBlank(author) && StringUtils.isNotBlank(text)) {
            Map<String, String> comment = new HashMap<>();
            comment.put("author", author);
            comment.put("text", text);
            COMMENT_LIST.add(comment);
        }
        return COMMENT_LIST;
    }
}
