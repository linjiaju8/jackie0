package com.jackie0.common.service.impl;

import com.jackie0.common.dao.DataDictDao;
import com.jackie0.common.entity.DataDict;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.exception.BusinessException;
import com.jackie0.common.service.DataDictService;
import com.jackie0.common.utils.DataUtils;
import com.jackie0.common.utils.ValidatorUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CachePut(cacheNames = {"dataDictCache"}, key = "#result.groupCode + #result.dictKey", condition = "#result != null")
    public DataDict createDataDict(DataDict dataDict) {
        validDataDict(dataDict, OperationType.CREATE);
        DataUtils.setBaseEntityField(dataDict, OperationType.CREATE);
        return dataDictDao.save(dataDict);
    }

    @Override
    @Transactional
    public Iterable<DataDict> createDataDicts(List<DataDict> dataDicts) {
        if (CollectionUtils.isEmpty(dataDicts)) {
            throw new BusinessException("jackie0.common.dataDict.canNotBeNull");
        }
        for (DataDict dataDict : dataDicts) {
            validDataDict(dataDict, OperationType.CREATE);
            DataUtils.setBaseEntityField(dataDict, OperationType.CREATE);
        }
        return dataDictDao.save(dataDicts);
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
    @Cacheable(cacheNames = {"dataDictCache"}, key = "#groupCode")
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
    @Cacheable(cacheNames = {"dataDictCache"}, key = "#groupCode + #dictKey")
    public DataDict findDataDictByGroupCodeAndDictKey(String groupCode, String dictKey) {
        if (StringUtils.isBlank(groupCode) || StringUtils.isBlank(dictKey)) {
            return null;
        }
        DataDict dataDictCondition = new DataDict();
        dataDictCondition.setGroupCode(groupCode);
        dataDictCondition.setDictKey(dictKey);
        return dataDictDao.findOne(getDataDictWhereClause(dataDictCondition));
    }

    private void validDataDict(DataDict dataDict, OperationType operationType) {
        if (dataDict == null) {
            throw new BusinessException("jackie0.common.dataDict.canNotBeNull");
        }
        ValidatorUtils.validate(dataDict);
        DataDict dataDictTest = findDataDictByGroupCodeAndDictKey(dataDict.getGroupCode(), dataDict.getDictKey());
        if (operationType == OperationType.CREATE && dataDictTest != null) {
            throw new BusinessException("jackie0.common.dataDict.alreadyExists", dataDict.getGroupCode(), dataDict.getDictKey());
        }
        if (operationType == OperationType.UPDATE && dataDictTest == null) {
            throw new BusinessException("jackie0.common.dataDict.notExists", dataDict.getGroupCode(), dataDict.getDictKey());
        }
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
