package com.jackie0.common.service.impl;

import com.jackie0.common.dao.DataDictDao;
import com.jackie0.common.entity.DataDict;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.service.DataDictService;
import com.jackie0.common.utils.DataUtils;
import com.jackie0.common.utils.I18nUtils;
import com.jackie0.common.utils.ValidatorUtils;
import com.jackie0.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据字典服务实现类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-07 14:27
 */
@Service("dataDictService")
public class DataDictServiceImpl implements DataDictService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataDictServiceImpl.class);

    @Autowired
    private DataDictDao dataDictDao;


    @Override
    @CachePut(key = "#dataDict.groupCode#dataDict.dictKey", cacheNames = "dataDictCache", condition = "#result.errorCode eq 'success'")
    public ResultVO createDataDict(DataDict dataDict) {
        ResultVO createResult = validDataDict(dataDict, OperationType.CREATE);
        LOGGER.debug("新增数据字典验证结果-->{}", createResult);
        if (ResultVO.FAIL.equals(createResult.getErrorCode())) {
            return createResult;
        }
        DataUtils.setBaseEntityField(dataDict, OperationType.CREATE);
        DataDict dataDictCreate = dataDictDao.save(dataDict);
        createResult.setResult(dataDictCreate);
        return createResult;
    }

    @Override
    @Transactional
    public ResultVO createDataDicts(List<DataDict> dataDicts) {
        ResultVO createResult = new ResultVO(ResultVO.SUCCESS, OperationType.CREATE.getName() + I18nUtils.getMessage("jackie0.common.dataDict.success.tips"));
        if (CollectionUtils.isEmpty(dataDicts)) {
            createResult = new ResultVO(ResultVO.FAIL, I18nUtils.getMessage("jackie0.common.dataDict.canNotBeNull"));
            return createResult;
        }
        for (DataDict dataDict : dataDicts) {
            createResult = validDataDict(dataDict, OperationType.CREATE);
            LOGGER.debug("批量新增数据字典验证结果-->{}", createResult);
            if (ResultVO.FAIL.equals(createResult.getErrorCode())) {
                return createResult;
            }
            DataUtils.setBaseEntityField(dataDict, OperationType.CREATE);
        }
        Iterable<DataDict> dataDictsCreate = dataDictDao.save(dataDicts);
        createResult.setResult(dataDictsCreate);
        return createResult;
    }

    @Override
    public Page<DataDict> findDataDictsByPage(DataDict dataDictCondition) {
        if (dataDictCondition == null) {
            LOGGER.info("分页查询数据字典方法接收参数为空，返回null！");
            return null;
        }
        dataDictCondition.setSortFieldName("dictOrder");
        dataDictCondition.setOrderMethod(Sort.Direction.ASC.name());
        return dataDictDao.findAll(getDataDictWhereClause(dataDictCondition), dataDictCondition.getPageRequest());
    }

    @Override
    public List<DataDict> findDataDictsByGroupCode(String groupCode) {
        if (StringUtils.isBlank(groupCode)) {
            return Collections.emptyList();
        }
        DataDict dataDictCondition = new DataDict();
        dataDictCondition.setGroupCode(groupCode);
        dataDictCondition.setSortFieldName("dictOrder");
        dataDictCondition.setOrderMethod(Sort.Direction.ASC.name());
        return dataDictDao.findAll(getDataDictWhereClause(dataDictCondition), dataDictCondition.getSort());
    }

    @Override
    public DataDict findDataDictByGroupCodeAndDictKey(String groupCode, String dictKey) {
        if (StringUtils.isBlank(groupCode) || StringUtils.isBlank(dictKey)) {
            return null;
        }
        DataDict dataDictCondition = new DataDict();
        dataDictCondition.setGroupCode(groupCode);
        dataDictCondition.setDictKey(dictKey);
        return dataDictDao.findOne(getDataDictWhereClause(dataDictCondition));
    }

    private ResultVO validDataDict(DataDict dataDict, OperationType operationType) {
        ResultVO validResult = new ResultVO(ResultVO.SUCCESS, I18nUtils.getMessage("jackie0.common.dataDict.success.tips", operationType.getName()));
        if (dataDict == null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.dataDict.canNotBeNull"));
            return validResult;
        }
        validResult = ValidatorUtils.validateData(dataDict, true);
        if (ResultVO.FAIL.equals(validResult.getErrorCode())) {
            return validResult;
        }
        DataDict dataDictTest = findDataDictByGroupCodeAndDictKey(dataDict.getGroupCode(), dataDict.getDictKey());
        if (operationType == OperationType.CREATE && dataDictTest != null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.dataDict.alreadyExists", dataDict.getGroupCode(), dataDict.getDictKey()));
            return validResult;
        }
        if (operationType == OperationType.UPDATE && dataDictTest == null) {
            validResult.setErrorCode(ResultVO.FAIL);
            validResult.setErrorMsg(I18nUtils.getMessage("jackie0.common.dataDict.notExists", dataDict.getGroupCode(), dataDict.getDictKey()));
            return validResult;
        }
        return validResult;
    }

    private Specification<DataDict> getDataDictWhereClause(final DataDict dataDict) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deletedFlag"), DeleteTag.IS_NOT_DELETED.getValue()));
            if (StringUtils.isNotBlank(dataDict.getGroupCode())) {
                predicates.add(cb.equal(root.get("groupCode"), dataDict.getGroupCode()));
            }
            if (StringUtils.isNotBlank(dataDict.getDictKey())) {
                predicates.add(cb.equal(root.get("dictKey"), dataDict.getDictKey()));
            }
            if (StringUtils.isNotBlank(dataDict.getDictValue())) {
                predicates.add(cb.like(root.get("dictValue").as(String.class), "%" + dataDict.getDictValue() + "%"));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
