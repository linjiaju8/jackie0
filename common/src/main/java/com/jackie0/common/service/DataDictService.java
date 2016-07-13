package com.jackie0.common.service;


import com.jackie0.common.entity.DataDict;
import com.jackie0.common.vo.ResultVO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 数据字典服务类
 * ClassName:DataDictService <br/>
 * Date:     2016年07月07日 16:09 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public interface DataDictService {
    /**
     * 新增一个数据字典并返回新增结果
     *
     * @param dataDict 需要新增数据字典实体
     * @return 新增结果，是否成功及失败的异常信息
     */
    ResultVO createDataDict(DataDict dataDict);

    /**
     * 批量新增一个数据字典并返回新增结果
     *
     * @param dataDicts 需要新增数据字典实体列表
     * @return 新增结果，是否成功及失败的异常信息
     */
    ResultVO createDataDicts(List<DataDict> dataDicts);

    /**
     * 分页查询数据字典
     *
     * @param dataDictCondition 查询条件
     * @return 符合条件的数据字典分页信息
     */
    Page<DataDict> findDataDictsByPage(DataDict dataDictCondition);

    /**
     * 根据分组编码获取数据字典列表
     *
     * @param groupCode 分组编码
     * @return 该分组编码对应的数据字典列表
     */
    List<DataDict> findDataDictsByGroupCode(String groupCode);

    /**
     * 根据分组编码及数据字典key获取指定的数据字典
     *
     * @param groupCode 分组编码
     * @param dictKey   数据字典key
     * @return 唯一对应的数据字典
     */
    DataDict findDataDictByGroupCodeAndDictKey(String groupCode, String dictKey);
}
