package com.jackie0.showcase.service;

import com.jackie0.showcase.entity.ShowcaseUser;
import org.springframework.data.domain.Page;

/**
 * 演示用户service接口
 *
 * @author jackie0
 * @since Java8
 * date 2016-10-26 16:42
 */
public interface ShowcaseUserService {
    /**
     * 新增演示用户
     *
     * @param showcaseUser 演示用户实体
     * @return 新增成功后的演示用户实体信息
     */
    ShowcaseUser createShowcaseUser(ShowcaseUser showcaseUser);

    /**
     * 分页查询演示用户信息
     *
     * @param showcaseUserCondition 查询条件
     * @return 分页演示用户信息
     */
    Page<ShowcaseUser> findShowcaseUsersByPage(ShowcaseUser showcaseUserCondition);

    /**
     * 更新演示用户信息
     *
     * @param showcaseUser 需要更新的演示用户实体信息
     * @return 更新后的演示用户实体信息
     */
    ShowcaseUser updateShowcaseUser(ShowcaseUser showcaseUser);

    /**
     * 删除演示用户信息，逻辑删除，即把删除标识{@link com.jackie0.common.entity.BaseEntity#deletedFlag}置为1
     *
     * @param showcaseUser 要删除的演示用户实体
     * @return 删除后的演示用户实体信息
     */
    ShowcaseUser deleteShowcaseUser(ShowcaseUser showcaseUser);

    /**
     * 根据主键查找演示用户信息
     *
     * @param showcaseUserId 演示用户ID
     * @return 符合条件的演示用户信息
     */
    ShowcaseUser findShowcaseUserById(String showcaseUserId);
}
