package com.jackie0.showcase.service.impl;

import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.exception.BusinessException;
import com.jackie0.common.utils.DataUtils;
import com.jackie0.common.utils.ValidatorUtils;
import com.jackie0.showcase.dao.ShowcaseUserDao;
import com.jackie0.showcase.entity.ShowcaseUser;
import com.jackie0.showcase.service.ShowcaseUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 演示用户service类
 *
 * @author jackie0
 * @since Java8
 * date 2016-10-26 16:44
 */
@Service("showcaseUserService")
public class ShowcaseUserServiceImpl implements ShowcaseUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowcaseUserServiceImpl.class);

    @Autowired
    private ShowcaseUserDao showcaseUserDao;

    @Override
    public ShowcaseUser createShowcaseUser(ShowcaseUser showcaseUser) {
        validShowcaseUser(showcaseUser, OperationType.CREATE);
        DataUtils.setBaseEntityField(showcaseUser, OperationType.CREATE);
        return showcaseUserDao.save(showcaseUser);
    }

    @Override
    public Page<ShowcaseUser> findShowcaseUsersByPage(ShowcaseUser showcaseUserCondition) {
        if (showcaseUserCondition == null) {
            LOGGER.info("分页查询数据字典方法接收参数为空，返回null！");
            return null;
        }
        return showcaseUserDao.findAll(getShowcaseUserWhereClause(showcaseUserCondition), showcaseUserCondition.getPageRequest());
    }

    @Override
    public ShowcaseUser updateShowcaseUser(ShowcaseUser showcaseUser) {
        validShowcaseUser(showcaseUser, OperationType.UPDATE);
        DataUtils.setBaseEntityField(showcaseUser, OperationType.UPDATE);
        return showcaseUserDao.save(showcaseUser);
    }

    @Override
    public ShowcaseUser deleteShowcaseUser(ShowcaseUser showcaseUser) {
        validShowcaseUser(showcaseUser, OperationType.UPDATE);
        DataUtils.setBaseEntityField(showcaseUser, OperationType.DELETE);
        return showcaseUserDao.save(showcaseUser);
    }

    @Override
    public ShowcaseUser findShowcaseUserById(String showcaseUserId) {
        if (StringUtils.isBlank(showcaseUserId)) {
            return null;
        }
        ShowcaseUser showcaseUserCondition = new ShowcaseUser();
        showcaseUserCondition.setShowcaseUserId(showcaseUserId);
        return showcaseUserDao.findOne(getShowcaseUserWhereClause(showcaseUserCondition));
    }

    private void validShowcaseUser(ShowcaseUser showcaseUser, OperationType operationType) {
        if (showcaseUser == null) {
            throw new BusinessException("jackie0.showcase.showcaseUser.NotBlank");
        }
        ValidatorUtils.validate(showcaseUser);
        ShowcaseUser showcaseUserCondition = new ShowcaseUser();
        showcaseUserCondition.setUserName(showcaseUser.getUserName());
        ShowcaseUser showcaseUserTest = showcaseUserDao.findOne(getShowcaseUserWhereClause(showcaseUserCondition));
        if (OperationType.CREATE == operationType && showcaseUserTest != null) {
            throw new BusinessException("jackie0.showcase.showcaseUser.alreadyExists");
        }
        if (OperationType.UPDATE == operationType && showcaseUserTest == null) {
            throw new BusinessException("jackie0.showcase.showcaseUser.notExists", showcaseUser.getUserName());
        }
    }

    private Specification<ShowcaseUser> getShowcaseUserWhereClause(final ShowcaseUser showcaseUserCondition) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deletedFlag"), DeleteTag.IS_NOT_DELETED.getValue()));
            if (StringUtils.isNotBlank(showcaseUserCondition.getMobilePhone())) {
                predicates.add(cb.equal(root.get("mobilePhone"), showcaseUserCondition.getMobilePhone()));
            }
            if (showcaseUserCondition.getAge() != null) {
                predicates.add(cb.equal(root.get("age"), showcaseUserCondition.getAge()));
            }
            if (StringUtils.isNotBlank(showcaseUserCondition.getUserName())) {
                predicates.add(cb.like(root.get("userName").as(String.class), "%" + showcaseUserCondition.getUserName() + "%"));
            }
            if (StringUtils.isNotBlank(showcaseUserCondition.getShowcaseUserId())) {
                predicates.add(cb.equal(root.get("showcaseUserId"), showcaseUserCondition.getShowcaseUserId()));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
