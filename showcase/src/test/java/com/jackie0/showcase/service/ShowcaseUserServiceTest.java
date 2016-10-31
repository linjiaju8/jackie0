package com.jackie0.showcase.service;

import com.jackie0.showcase.BaseSprintTestCase;
import com.jackie0.showcase.entity.ShowcaseUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 演示用户service单元测试
 *
 * @author jackie0
 * @since Java8
 * date 2016-10-31 15:05
 */
public class ShowcaseUserServiceTest extends BaseSprintTestCase {

    @Autowired
    private ShowcaseUserService showcaseUserService;

    @Test
    public void testCreateShowcaseUser() {
        ShowcaseUser showcaseUser = new ShowcaseUser();
        showcaseUser.setUserName("单元测试演示用户");
        showcaseUser.setAge(30);
        showcaseUser.setMobilePhone("15002016820");
        showcaseUserService.createShowcaseUser(showcaseUser);
    }
}
