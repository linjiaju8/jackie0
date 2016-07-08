package com.jackie0.common.service;


import com.jackie0.common.entity.BaseOperationLog;
import org.springframework.data.domain.Page;

/**
 * 操作日志服务，同时支持关系数据库及mongodb
 * ClassName:OperationLogService <br/>
 * Date:     2015年08月10日 16:09 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public interface OperationLogService {


    /**
     * 创建客户操作日志
     *
     * @param baseOperationLog 客户操作日志实体
     * @return 创建成功后的客户操作日志
     */
    BaseOperationLog createOperationLog(BaseOperationLog baseOperationLog);

    /**
     * 根据ID获取操作日志信息
     *
     * @param operationLogId 操作日志ID
     * @param clazz          操作日志的类型，决定是使用mongodb还是关系数据库
     * @return 操作日志信息
     */
    <T extends BaseOperationLog> BaseOperationLog findOne(String operationLogId, Class<T> clazz);

    /**
     * 根据用户查询条件分页查询客户操作日志
     * 如果用户为空则查询当前登录用户的数据
     *
     * @param baseOperationLog 客户操作日志查询条件
     * @return 符合条件的客户操作日志列表
     */
    Page<? extends BaseOperationLog> findByPage(BaseOperationLog baseOperationLog);
}
