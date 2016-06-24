package com.jackie0.common.mongo.dao;

import com.jackie0.common.mongo.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * ClassName:OperationLogMongoDao <br/>
 * Date:     2015年08月11日 16:41 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.7
 */
public interface OperationLogMongoDao extends MongoRepository<OperationLog, String> {
    /**
     * 根据用户名分页查询客户操作日志
     *
     * @param userName 用户名
     * @param pageable 分页参数
     * @return 客户操作日志分页信息
     */
    @Query("{'operationUser':?0}")
    Page<OperationLog> findCustLogs(String userName, Pageable pageable);

    /**
     * 根据用户名及操作名称分页查询客户操作日志
     *
     * @param userName      用户名
     * @param operationName 客户名称
     * @param pageable      分页参数
     * @return 客户操作日志分页信息
     */
    @Query("{'operationUser':?0,'operationName':{'$regex':?1,'$options':'i'}}")
    Page<OperationLog> findCustLogs(String userName, String operationName, Pageable pageable);
}
