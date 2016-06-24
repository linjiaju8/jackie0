package com.jackie0.common.service.impl;

import com.jackie0.common.enumeration.OperationType;
import com.jackie0.common.mongo.dao.OperationLogMongoDao;
import com.jackie0.common.mongo.entity.OperationLog;
import com.jackie0.common.service.OperationLogService;
import com.jackie0.common.utils.DataUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户操作日志服务
 * ClassName:CustLogServiceImpl <br/>
 * Date:     2015年08月11日 16:45 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.7
 */
@Service("operationLogServiceImpl")
public class OperationLogServiceImpl implements OperationLogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Resource
    private OperationLogMongoDao operationLogMongoDao;

    /**
     * 记录客户日志
     *
     * @param operationLog 客户操作日志实体
     * @return 新增成功后的客户日志
     */
    @Override
    public OperationLog createCustLog(OperationLog operationLog) {
        // CustLog custLogRt = custLogDao.save(custLog); mongodb实现
        LOGGER.debug("记录客户操作日志成功-->{}", JsonUtil.javaObjToJson(custLog));
        DataUtils.setBaseEntityField(operationLog, OperationType.CREATE);
        return operationLogMongoDao.save(operationLog);
    }

    /**
     * 分页查询客户操作日志
     *
     * @param custLog 客户操作日志查询条件
     * @return 客户操作日志分页信息
     */
    @Override
    public Page<OperationLog> findByPage(OperationLog custLog) {
        Page<OperationLog> operationLogs;
        if (StringUtils.isBlank(custLog.getCustLogQueryParam())) {
            operationLogs = operationLogMongoDao.findCustLogs(custLog.getOperationUser(), getCustLogPageRequest(custLog));
        } else {
            operationLogs = operationLogMongoDao.findCustLogs(custLog.getOperationUser(), ".*" + custLog.getCustLogQueryParam() + ".*", getCustLogPageRequest(custLog.getPage(), custLog.getSize()));
        }
        return operationLogs;
    }

    /**
     * 根据ID获取客户操作日志
     *
     * @param operationLogId 客户操作日志ID
     * @return 客户操作日志实体
     */
    @Override
    public OperationLog findOne(String operationLogId) {
        return operationLogMongoDao.findOne(operationLogId);
    }

    private Specification<CustLog> getCustLogWhereClause(final CustLog custLog) {
        return new Specification<CustLog>() {
            @Override
            public Predicate toPredicate(Root<CustLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("deletedFlag"), Constant.IS_NOT_DELETED));
                predicates.add(cb.equal(root.get("operationUser"), custLog.getOperationUser()));
                // 默认查近一个月数据
                predicates.add(cb.between(root.<Timestamp>get("creationDate"), new Timestamp(DateUtils.addMonths(new Date(), -1).getTime()), new Timestamp(System.currentTimeMillis())));
                return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
    }

    /**
     * 创建分页请求.
     */
    private PageRequest getCustLogPageRequest(OperationLog operationLog) {
        Sort sort = new Sort(Sort.Direction.DESC, "creationDate");
        sort = sort.and(new Sort("rowid"));
        return new PageRequest(operationLog.getPage() - 1, operationLog.getSize(), sort);
    }
}
