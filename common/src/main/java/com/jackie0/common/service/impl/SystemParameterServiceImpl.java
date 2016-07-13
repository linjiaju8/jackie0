package com.jackie0.common.service.impl;

import com.jackie0.common.dao.SystemParameterDao;
import com.jackie0.common.entity.SystemParameter;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.service.SystemParameterService;
import com.jackie0.common.utils.DataUtils;
import com.jackie0.common.utils.I18nUtils;
import com.jackie0.common.utils.ValidatorUtils;
import com.jackie0.common.vo.ResultVO;
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
 * 系统参数服务实现类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-13 10:57
 */
@Service("systemParameterService")
public class SystemParameterServiceImpl implements SystemParameterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemParameterServiceImpl.class);

    @Autowired
    private SystemParameterDao systemParameterDao;

    @Override
    public ResultVO createSystemParameter(SystemParameter systemParameter) {
        ResultVO createResult = validSystemParameter(systemParameter, OperationType.CREATE);
        LOGGER.debug("新增系统参数验证结果-->{}", createResult);
        if (ResultVO.FAIL.equals(createResult.getErrorCode())) {
            return createResult;
        }
        DataUtils.setBaseEntityField(systemParameter, OperationType.CREATE);
        SystemParameter systemParameterCreate = systemParameterDao.save(systemParameter);
        createResult.setResult(systemParameterCreate);
        return createResult;
    }

    @Override
    public ResultVO updateSystemParameter(SystemParameter systemParameter) {
        ResultVO updateResult = validSystemParameter(systemParameter, OperationType.UPDATE);
        LOGGER.debug("修改系统参数验证结果-->{}", updateResult);
        if (ResultVO.FAIL.equals(updateResult.getErrorCode())) {
            return updateResult;
        }
        DataUtils.setBaseEntityField(systemParameter, OperationType.UPDATE);
        SystemParameter systemParameterUpdate = systemParameterDao.save(systemParameter);
        updateResult.setResult(systemParameterUpdate);
        return updateResult;
    }

    @Override
    public ResultVO deleteSystemParameterById(String systemParameterId) {
        ResultVO validResult = new ResultVO(ResultVO.SUCCESS, I18nUtils.getMessage("jackie0.common.systemParameter.success.tips", OperationType.DELETE.getName()));
        if (StringUtils.isBlank(systemParameterId)) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.parameter.canNotBeNull", OperationType.DELETE, I18nUtils.getMessage("jackie0.common.systemParameter.primaryKey")));
            return validResult;
        }
        SystemParameter systemParameter = systemParameterDao.findOne(systemParameterId);
        if (systemParameter == null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.systemParameter.notExists"));
            return validResult;
        }
        DataUtils.setBaseEntityField(systemParameter, OperationType.DELETE);
        SystemParameter systemParameterDelete = systemParameterDao.save(systemParameter);
        validResult.setResult(systemParameterDelete);
        return validResult;
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
    public <T> T findSystemParameterValueByKey(String systemParameterKey, Class<T> clazz) {
        if (StringUtils.isBlank(systemParameterKey) || clazz == null) {
            LOGGER.info("根据键值查询系统参数值方法有入参为空，返回null！入参systemParameterKey-->{},clazz-->{}", systemParameterKey, clazz);
            return null;
        }
        SystemParameter systemParameterCondition = new SystemParameter();
        systemParameterCondition.setParameterKey(systemParameterKey);
        SystemParameter systemParameter = systemParameterDao.findOne(getSystemParameterWhereClause(systemParameterCondition));
        T systemParameterValue = null;
        if (systemParameter == null) {
            LOGGER.debug("根据键值查询系统参数值为null，键值为{}", systemParameterKey);
        } else {
            systemParameterValue = clazz.cast(systemParameter.getParameterValue());
        }
        return systemParameterValue;
    }

    private ResultVO validSystemParameter(SystemParameter systemParameter, OperationType operationType) {
        ResultVO validResult = new ResultVO(ResultVO.SUCCESS, I18nUtils.getMessage("jackie0.common.systemParameter.success.tips", operationType.getName()));
        if (systemParameter == null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.systemParameter.canNotBeNull"));
            return validResult;
        }
        validResult = ValidatorUtils.validateData(systemParameter, true);
        if (ResultVO.FAIL.equals(validResult.getErrorCode())) {
            return validResult;
        }
        SystemParameter systemParameterTest = findSystemParameterByKey(systemParameter.getParameterKey());
        if (operationType == OperationType.CREATE && systemParameterTest != null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.systemParameter.alreadyExists", systemParameter.getParameterKey()));
            return validResult;
        }
        if (operationType == OperationType.UPDATE && systemParameterTest == null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.systemParameter.notExists", systemParameter.getParameterKey()));
            return validResult;
        }
        return validResult;
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
