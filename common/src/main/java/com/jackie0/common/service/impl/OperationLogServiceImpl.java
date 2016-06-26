package com.jackie0.common.service.impl;

import com.jackie0.common.dao.OperationLogDao;
import com.jackie0.common.entity.BaseOperationLog;
import com.jackie0.common.entity.OperationLog;
import com.jackie0.common.enumeration.DeleteTag;
import com.jackie0.common.mongo.dao.OperationLogMongoDao;
import com.jackie0.common.mongo.entity.MongoOperationLog;
import com.jackie0.common.service.OperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 操作日志服务
 * ClassName:OperationLogServiceImpl <br/>
 * Date:     2015年08月11日 16:45 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@Service("operationLogServiceImpl")
public class OperationLogServiceImpl implements OperationLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Autowired
    private OperationLogMongoDao operationLogMongoDao;

    @Autowired
    private OperationLogDao operationLogDao;

    /**
     * 记录操作日志
     *
     * @param baseOperationLog 操作日志实体
     * @return 新增成功后的日志
     */
    @Override
    public BaseOperationLog createOperationLog(BaseOperationLog baseOperationLog) {
        if (baseOperationLog == null) {
            return null;
        }
        BaseOperationLog baseOperationLogResult = null;
        if (baseOperationLog instanceof OperationLog) {
            baseOperationLogResult = operationLogDao.save((OperationLog) baseOperationLog);
        } else if (baseOperationLog instanceof MongoOperationLog) {
            if (StringUtils.isBlank(((MongoOperationLog) baseOperationLog).getId())) {
                // mongodb的主键不会自动生成，如果没有则手动设置
                ((MongoOperationLog) baseOperationLog).setId(UUID.randomUUID().toString());
            }
            baseOperationLogResult = operationLogMongoDao.save((MongoOperationLog) baseOperationLog);
        }
        return baseOperationLogResult;
    }

    /**
     * 根据ID获取操作日志信息
     *
     * @param operationLogId 操作日志ID
     * @param clazz          操作日志的类型，觉得是使用mongodb还是关系数据库
     * @return 操作日志信息
     */
    @Override
    public <T extends BaseOperationLog> BaseOperationLog findOne(String operationLogId, Class<T> clazz) {
        if (StringUtils.isBlank(operationLogId)) {
            return null;
        }
        if (clazz == OperationLog.class) {
            return operationLogDao.findOne(operationLogId);
        } else if (clazz == MongoOperationLog.class) {
            return operationLogMongoDao.findOne(operationLogId);
        }
        return null;
    }

    /**
     * 分页查询操作日志
     *
     * @param baseOperationLog 操作日志查询条件
     * @return 操作日志分页信息
     */
    @Override
    public Page<? extends BaseOperationLog> findByPage(BaseOperationLog baseOperationLog) {
        Page<? extends BaseOperationLog> operationLogs = null;
        if (baseOperationLog instanceof OperationLog) {
            operationLogs = operationLogDao.findAll(getOperationLogWhereClause((OperationLog) baseOperationLog), ((OperationLog) baseOperationLog).getPageRequest());
        } else if (baseOperationLog instanceof MongoOperationLog) {
            if (StringUtils.isBlank(((MongoOperationLog) baseOperationLog).getOperationLogQueryParam())) {
                operationLogs = operationLogMongoDao.findOperationLogs(((MongoOperationLog) baseOperationLog).getOperationUser(), ((MongoOperationLog) baseOperationLog).getPageRequest());
            } else {
                operationLogs = operationLogMongoDao.findOperationLogs(((MongoOperationLog) baseOperationLog).getOperationUser(), ".*" + ((MongoOperationLog) baseOperationLog).getOperationLogQueryParam() + ".*", ((MongoOperationLog) baseOperationLog).getPageRequest());
            }
        }
        return operationLogs;
    }

    private Specification<OperationLog> getOperationLogWhereClause(final OperationLog operationLog) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("deletedFlag"), DeleteTag.IS_NOT_DELETED.getValue()));
            predicates.add(cb.equal(root.get("operationUser"), operationLog.getOperationUser()));
            // 默认查近一个月数据
            predicates.add(cb.between(root.get("creationDate"), new Timestamp(DateUtils.addMonths(new Date(), -1).getTime()), new Timestamp(System.currentTimeMillis())));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
    }
}
