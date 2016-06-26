package com.jackie0.common.mongo.dao;

import com.jackie0.common.mongo.entity.MongoOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * ClassName:OperationLogMongoDao <br/>
 * Date:     2015年08月11日 16:41 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public interface OperationLogMongoDao extends MongoRepository<MongoOperationLog, String> {
    /**
     * 根据用户名分页查询客户操作日志
     *
     * @param userName 用户名
     * @param pageable 分页参数
     * @return 客户操作日志分页信息
     */
    @Query("{'operationUser':?0}")
    Page<MongoOperationLog> findOperationLogs(String userName, Pageable pageable);

    /**
     * 根据用户名及操作名称分页查询客户操作日志
     *
     * @param userName      用户名
     * @param operationName 客户名称
     * @param pageable      分页参数
     * @return 客户操作日志分页信息
     */
    @Query("{'operationUser':?0,'operationName':{'$regex':?1,'$options':'i'}}")
    Page<MongoOperationLog> findOperationLogs(String userName, String operationName, Pageable pageable);

    /**
     * 根据操作名称查找操作日志
     *
     * @param operationName 操作名称
     * @return 操作日志列表
     */
    @Query("{'operationName':?0}")
    List<MongoOperationLog> findOperationLogsByOperationName(String operationName);
}
