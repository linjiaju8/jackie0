package com.jackie0.common.service.impl;

import com.jackie0.common.dao.DataDictDao;
import com.jackie0.common.entity.DataDict;
import com.jackie0.common.service.DataDictService;
import com.jackie0.common.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
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
    @Autowired
    private DataDictDao dataDictDao;


    @Override
    public ResultVO createDataDict(@NotNull DataDict dataDict) {

        return null;
    }

    @Override
    public ResultVO createDataDicts(@NotNull List<DataDict> dataDicts) {
        return null;
    }

    @Override
    public Page<DataDict> findDataDictsByPage(DataDict dataDictCondition) {
        return null;
    }

    @Override
    public List<DataDict> findDataDictsByGroupCode(@NotNull String groupCode) {
        return null;
    }

    @Override
    public DataDict findDataDictByGroupCodeAndDictKey(@NotNull String groupCode, @NotNull String dictKey) {
        return null;
    }
}
