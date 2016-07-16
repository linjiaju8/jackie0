package com.jackie0.common.service;

import com.jackie0.common.entity.SystemParameter;
import com.jackie0.common.vo.ResultVO;
import org.springframework.data.domain.Page;

/**
 * 系统参数服务接口
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-13 10:56
 */
public interface SystemParameterService {
    /**
     * 新增系统参数
     *
     * @param systemParameter 要新增的系统参数实体
     * @return 新增结果
     */
    ResultVO createSystemParameter(SystemParameter systemParameter);

    /**
     * 修改系统参数
     *
     * @param systemParameter 要修改的系统参数实体
     * @return 修改结果
     */
    ResultVO updateSystemParameter(SystemParameter systemParameter);

    /**
     * 根据主键删除系统参数
     *
     * @param systemParameterId 要删除的系统参数主键
     * @return 删除结果
     */
    ResultVO deleteSystemParameterById(String systemParameterId);

    /**
     * 根据条件分页查询系统参数
     *
     * @param systemParameter 查询条件
     * @return 符合条件的系统参数分页信息
     */
    Page<SystemParameter> findSystemParameterByPage(SystemParameter systemParameter);

    /**
     * 根据系统参数键值查找系统参数
     *
     * @param systemParameterKey 系统参数键值
     * @return 键值对应的系统参数
     */
    SystemParameter findSystemParameterByKey(String systemParameterKey);

    /**
     * 根据系统参数键值查找系统参数值
     *
     * @param systemParameterKey 系统参数键值
     * @return 键值对应的系统参数值
     */
    String findSystemParameterValueByKey(String systemParameterKey);
}
