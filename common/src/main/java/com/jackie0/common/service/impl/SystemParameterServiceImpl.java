package com.jackie0.common.service.impl;

import com.jackie0.common.dao.SystemParameterDao;
import com.jackie0.common.entity.SystemParameter;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.exception.BusinessException;
import com.jackie0.common.service.SystemParameterService;
import com.jackie0.common.utils.DataUtils;
import com.jackie0.common.utils.I18nUtils;
import com.jackie0.common.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统参数服务实现类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-13 10:57
 */
@Service("systemParameterService")
public class SystemParameterServiceImpl implements SystemParameterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemParameterServiceImpl.class);

    private static final String RESULT_PARAMETERKEY_CACHE_KEY = "'" + SystemParameter.DATA_SYSTEM_PARAMETER_KEY_PREFIX + "' + #result.parameterKey";
    private static final String PARAMETERKEY_CACHE_KEY = "'" + SystemParameter.DATA_SYSTEM_PARAMETER_KEY_PREFIX + "' + #systemParameterKey";

    @Autowired
    private SystemParameterDao systemParameterDao;

    @Override
    @CachePut(cacheNames = {"systemParameterCache"}, key = RESULT_PARAMETERKEY_CACHE_KEY, condition = "#result != null")
    public SystemParameter createSystemParameter(SystemParameter systemParameter) {
        validSystemParameter(systemParameter, OperationType.CREATE);
        DataUtils.setBaseEntityField(systemParameter, OperationType.CREATE);
        return systemParameterDao.save(systemParameter);
    }

    @Override
    @CachePut(cacheNames = {"systemParameterCache"}, key = RESULT_PARAMETERKEY_CACHE_KEY, condition = "#result != null")
    public SystemParameter updateSystemParameter(SystemParameter systemParameter) {
        validSystemParameter(systemParameter, OperationType.UPDATE);
        DataUtils.setBaseEntityField(systemParameter, OperationType.UPDATE);
        return systemParameterDao.save(systemParameter);
    }

    @Override
    @CacheEvict(value = {"systemParameterCache"}, key = RESULT_PARAMETERKEY_CACHE_KEY)
    public SystemParameter deleteSystemParameterById(String systemParameterId) {
        if (StringUtils.isBlank(systemParameterId)) {
            throw new BusinessException("jackie0.common.parameter.canNotBeNull", OperationType.DELETE, I18nUtils.getMessage("jackie0.common.systemParameter.primaryKey"));
        }
        SystemParameter systemParameter = systemParameterDao.findOne(systemParameterId);
        if (systemParameter == null) {
            throw new BusinessException("jackie0.common.systemParameter.notExists");
        }
        DataUtils.setBaseEntityField(systemParameter, OperationType.DELETE);
        return systemParameterDao.save(systemParameter);
    }

    @Override
    public Page<SystemParameter> findSystemParameterByPage(SystemParameter systemParameter) {
        if (systemParameter == null) {
            LOGGER.info("分页查询系统参数方法接收参数为空，返回null！");
            return null;
        }
        return systemParameterDao.findAll(getSystemParameterWhereClause(systemParameter), systemParameter.getPageRequest());
    }

    @Override
    @Cacheable(cacheNames = {"systemParameterCache"}, key = PARAMETERKEY_CACHE_KEY)
    public SystemParameter findSystemParameterByKey(String systemParameterKey) {
        if (StringUtils.isBlank(systemParameterKey)) {
            LOGGER.info("根据键值查询系统参数方法接收参数为空，返回null！");
            return null;
        }
        SystemParameter systemParameterCondition = new SystemParameter();
        systemParameterCondition.setParameterKey(systemParameterKey);
        return systemParameterDao.findOne(getSystemParameterWhereClause(systemParameterCondition));
    }

    @Override
    public String findSystemParameterValueByKey(String systemParameterKey) {
        SystemParameter systemParameter = findSystemParameterByKey(systemParameterKey);
        String systemParameterValue = null;
        if (systemParameter != null) {
            systemParameterValue = systemParameter.getParameterValue();
        }
        return systemParameterValue;
    }

    private void validSystemParameter(SystemParameter systemParameter, OperationType operationType) {
        if (systemParameter == null) {
            throw new BusinessException("jackie0.common.systemParameter.canNotBeNull");
        }
        ValidatorUtils.validate(systemParameter);
        SystemParameter systemParameterTest = findSystemParameterByKey(systemParameter.getParameterKey());
        if (operationType == OperationType.CREATE && systemParameterTest != null) {
            throw new BusinessException("jackie0.common.systemParameter.alreadyExists", systemParameter.getParameterKey());
        }
        if (operationType == OperationType.UPDATE && systemParameterTest == null) {
            throw new BusinessException("jackie0.common.systemParameter.notExists", systemParameter.getParameterKey());
        }
    }

    private Specification<SystemParameter> getSystemParameterWhereClause(final SystemParameter systemParameter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deletedFlag"), DeleteTag.IS_NOT_DELETED.getValue()));
            if (StringUtils.isNotBlank(systemParameter.getParameterKey())) {
                predicates.add(cb.equal(root.get("parameterKey"), systemParameter.getParameterKey()));
            }
            if (StringUtils.isNotBlank(systemParameter.getDescription())) {
                predicates.add(cb.like(root.get("description").as(String.class), "%" + systemParameter.getDescription() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
