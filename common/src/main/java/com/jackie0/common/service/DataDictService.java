package com.jackie0.common.service;


import com.jackie0.common.entity.DataDict;
import com.jackie0.common.vo.ResultVO;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
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
     * 创建一个数据字典并返回创建结果
     *
     * @param dataDict 需要创建数据字典实体
     * @return 创年间结果，是否成功及失败的异常信息
     */
    ResultVO createDataDict(@NotNull DataDict dataDict);

    /**
     * 批量创建一个数据字典并返回创建结果
     *
     * @param dataDicts 需要创建数据字典实体列表
     * @return 创年间结果，是否成功及失败的异常信息
     */
    ResultVO createDataDicts(@NotNull List<DataDict> dataDicts);

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
    List<DataDict> findDataDictsByGroupCode(@NotNull String groupCode);

    /**
     * 根据分组编码及数据字典key获取指定的数据字典
     *
     * @param groupCode 分组编码
     * @param dictKey   数据字典key
     * @return 唯一对应的数据字典
     */
    DataDict findDataDictByGroupCodeAndDictKey(@NotNull String groupCode, @NotNull String dictKey);
}
