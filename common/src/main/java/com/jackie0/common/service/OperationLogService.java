package com.jackie0.common.service;


import com.jackie0.common.entity.BaseOperationLog;
import org.springframework.data.domain.Page;

/**
 * 客户操作日志服务
 * ClassName:CustLogServiceImpl <br/>
 * Date:     2015年08月10日 16:09 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.7
 */
public interface OperationLogService {


    /**
     * 创建客户操作日志
     *
     * @param operationLog 客户操作日志实体
     * @return 创建成功后的客户操作日志，返回了主键
     */
    BaseOperationLog createOperationLog(BaseOperationLog operationLog);

    /**
     * 根据ID客户操作日志信息
     *
     * @param operationLogId 客户操作日志ID
     * @return 客户操作日志信息
     */
    BaseOperationLog findOne(String operationLogId);

    /**
     * 根据用户查询条件分页查询客户操作日志
     * 如果用户为空则查询当前登录用户的数据
     *
     * @param operationLog 客户操作日志查询条件
     * @return 符合条件的客户操作日志列表
     */
    Page<BaseOperationLog> findByPage(BaseOperationLog operationLog);
}
