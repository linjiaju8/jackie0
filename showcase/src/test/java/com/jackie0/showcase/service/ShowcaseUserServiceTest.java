package com.jackie0.showcase.service;

import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.exception.BusinessException;
import com.jackie0.showcase.BaseSprintTestCase;
import com.jackie0.showcase.dao.ShowcaseUserDao;
import com.jackie0.showcase.entity.ShowcaseUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 演示用户service单元测试
 *
 * @author jackie0
 * @since Java8
 * date 2016-10-31 15:05
 */
public class ShowcaseUserServiceTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowcaseUserServiceTest.class);

    @Autowired
    private ShowcaseUserService showcaseUserService;

    @Autowired
    private ShowcaseUserDao showcaseUserDao;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreateShowcaseUser() {
        ShowcaseUser showcaseUser = new ShowcaseUser();
        showcaseUser.setAge(30);
        showcaseUser.setMobilePhone("15002016820");
        expectedException.expect(BusinessException.class); // 验证用户名不能为空
        showcaseUserService.createShowcaseUser(showcaseUser);
        showcaseUser.setUserName("单元测试演示用户");
        ShowcaseUser showcaseUserSuccess = showcaseUserService.createShowcaseUser(showcaseUser);
        Assert.assertTrue(showcaseUserSuccess != null); // 新增成功
        expectedException.expect(BusinessException.class); // 验证用户名不能重复
        showcaseUserService.createShowcaseUser(showcaseUser);
    }

    @Test
    public void testFindShowcaseUsersByPage() {
        for (int i = 0; i < 20; i++) {
            ShowcaseUser showcaseUser = new ShowcaseUser();
            showcaseUser.setAge(30 + i);
            showcaseUser.setMobilePhone("15002016820");
            showcaseUser.setUserName("单元测试演示用户" + i);
            showcaseUserService.createShowcaseUser(showcaseUser);
        }
        Page<ShowcaseUser> showcaseUserPage = showcaseUserService.findShowcaseUsersByPage(new ShowcaseUser());
        Assert.assertTrue(showcaseUserPage != null && showcaseUserPage.getTotalPages() == 2);
    }

    @Test
    public void testUpdateShowcaseUser() {
        ShowcaseUser showcaseUser = new ShowcaseUser();
        showcaseUser.setAge(30);
        showcaseUser.setMobilePhone("15002016820");
        showcaseUser.setUserName("单元测试演示用户");
        ShowcaseUser showcaseUserSuccess = showcaseUserService.createShowcaseUser(showcaseUser);
        showcaseUserSuccess.setMobilePhone("15002016821");
        showcaseUserSuccess = showcaseUserService.updateShowcaseUser(showcaseUser);
        Assert.assertTrue(showcaseUserSuccess != null && "15002016821".equals(showcaseUserSuccess.getMobilePhone()));
    }

    @Test
    public void testDeleteShowcaseUser() {
        ShowcaseUser showcaseUser = new ShowcaseUser();
        showcaseUser.setAge(30);
        showcaseUser.setMobilePhone("15002016820");
        showcaseUser.setUserName("单元测试演示用户");
        ShowcaseUser showcaseUserSuccess = showcaseUserService.createShowcaseUser(showcaseUser);
        showcaseUserSuccess = showcaseUserService.deleteShowcaseUser(showcaseUserSuccess);
        Assert.assertTrue(showcaseUserSuccess != null && DeleteTag.IS_DELETED.getValue().equals(showcaseUserSuccess.getDeletedFlag()));
    }

    @Test
    public void testFindShowcaseUserById() {
        ShowcaseUser showcaseUser = new ShowcaseUser();
        showcaseUser.setAge(30);
        showcaseUser.setMobilePhone("15002016820");
        showcaseUser.setUserName("单元测试演示用户");
        ShowcaseUser showcaseUserSuccess = showcaseUserService.createShowcaseUser(showcaseUser);
        ShowcaseUser showcaseUserTest = showcaseUserService.findShowcaseUserById(showcaseUserSuccess.getShowcaseUserId());
        Assert.assertTrue(showcaseUserTest != null);
    }

    /**
     * 清理数据
     */
    @After
    public void clear() {
        List<ShowcaseUser> testShowcaseUsers = showcaseUserDao.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(root.get("userName").as(String.class), "单元测试%"));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (CollectionUtils.isNotEmpty(testShowcaseUsers)) {
            showcaseUserDao.delete(testShowcaseUsers);
        }
        LOGGER.debug("DataDictServiceTest清理单元测试数据成功！{}", ArrayUtils.toString(testShowcaseUsers));
    }
}
