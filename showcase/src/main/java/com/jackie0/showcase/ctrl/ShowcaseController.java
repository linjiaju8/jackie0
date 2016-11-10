package com.jackie0.showcase.ctrl;

import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.exception.BusinessException;
import com.jackie0.common.utils.I18nUtils;
import com.jackie0.common.vo.ResultVO;
import com.jackie0.showcase.entity.ShowcaseUser;
import com.jackie0.showcase.service.ShowcaseUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * showcase控制器类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-29 16:04
 */
@RestController
public class ShowcaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowcaseController.class);

    @Autowired
    private ShowcaseUserService showcaseUserService;

    /**
     * 创建演示用户
     *
     * @param showcaseUser 要创建的演示用户信息
     * @return 操作结果
     */
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public ResultVO<ShowcaseUser> createShowcaseUser(ShowcaseUser showcaseUser) {
        ResultVO<ShowcaseUser> createUserResult = new ResultVO<>(ResultVO.SUCCESS, I18nUtils.getMessage("jackie0.showcase.showcaseUser.success.tips", OperationType.CREATE.getName()));
        try {
            ShowcaseUser createdShowcaseUser = showcaseUserService.createShowcaseUser(showcaseUser);
            createUserResult.setResult(createdShowcaseUser);
        } catch (BusinessException e) {
            createUserResult.setErrorCode(ResultVO.FAIL);
            createUserResult.setErrorMsg(I18nUtils.getMessage("jackie0.showcase.showcaseUser.fail.tips", OperationType.CREATE.getName()) + e.getErrorMsg());
            createUserResult.setResult(showcaseUser);
            LOGGER.error("新增用户信息失败！", e);
        }
        return createUserResult;
    }

    /**
     * 根据条件分页查询演示用户信息
     *
     * @param showcaseUser 查询条件
     * @return 符合条件的演示用户分页信息
     */
    @RequestMapping(value = "/user/findByPage")
    public Page<ShowcaseUser> findShowcaseUserByPage(ShowcaseUser showcaseUser) {
        return showcaseUserService.findShowcaseUsersByPage(showcaseUser);
    }

    /**
     * 根据主键查询演示用户信息
     *
     * @param showcaseUserId 要查询的演示用户主键值
     * @return 符合条件的演示用户信息
     */
    @RequestMapping(value = "/user/{showcaseUserId}")
    public ShowcaseUser findShowcaseUserById(@PathVariable("showcaseUserId") String showcaseUserId) {
        return showcaseUserService.findShowcaseUserById(showcaseUserId);
    }

    /**
     * 更新演示用户信息
     *
     * @param showcaseUser 需要更新的演示用户信息
     * @return 操作结果
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public ResultVO<ShowcaseUser> updateShowcaseUser(ShowcaseUser showcaseUser) {
        ResultVO<ShowcaseUser> updateUserResult = new ResultVO<>(ResultVO.SUCCESS, I18nUtils.getMessage("jackie0.showcase.showcaseUser.success.tips", OperationType.UPDATE.getName()));
        try {
            ShowcaseUser updateShowcaseUser = showcaseUserService.updateShowcaseUser(showcaseUser);
            updateUserResult.setResult(updateShowcaseUser);
        } catch (BusinessException e) {
            updateUserResult.setErrorCode(ResultVO.FAIL);
            updateUserResult.setErrorMsg(I18nUtils.getMessage("jackie0.showcase.showcaseUser.fail.tips", OperationType.UPDATE.getName()) + e.getErrorMsg());
            updateUserResult.setResult(showcaseUser);
            LOGGER.error("修改用户信息失败！", e);
        }
        return updateUserResult;
    }

    /**
     * 逻辑删除演示用户信息
     *
     * @param showcaseUser 要删除的演示用户信息
     * @return 操作结果
     */
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public ResultVO<ShowcaseUser> deleteShowcaseUser(ShowcaseUser showcaseUser) {
        ResultVO<ShowcaseUser> deleteUserResult = new ResultVO<>(ResultVO.SUCCESS, I18nUtils.getMessage("jackie0.showcase.showcaseUser.success.tips", OperationType.DELETE.getName()));
        try {
            ShowcaseUser deleteShowcaseUser = showcaseUserService.deleteShowcaseUser(showcaseUser);
            deleteUserResult.setResult(deleteShowcaseUser);
        } catch (BusinessException e) {
            deleteUserResult.setErrorCode(ResultVO.FAIL);
            deleteUserResult.setErrorMsg(I18nUtils.getMessage("jackie0.showcase.showcaseUser.fail.tips", OperationType.DELETE.getName()) + e.getErrorMsg());
            deleteUserResult.setResult(showcaseUser);
            LOGGER.error("删除用户信息失败！", e);
        }
        return deleteUserResult;
    }
}
