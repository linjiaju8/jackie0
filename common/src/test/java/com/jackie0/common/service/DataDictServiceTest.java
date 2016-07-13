package com.jackie0.common.service;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.constant.Constant;
import com.jackie0.common.dao.DataDictDao;
import com.jackie0.common.entity.DataDict;
import com.jackie0.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典服务测试类
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-12 11:17
 */
public class DataDictServiceTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataDictServiceTest.class);

    private static final int TOTAL_BATCH_SIZE = 25;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private DataDictDao dataDictDao;

    @Test
    public void testCreateDataDict() {
        ResultVO createResult = dataDictService.createDataDict(null);
        Assert.assertTrue(ResultVO.FAIL.equals(createResult.getErrorCode()));

        DataDict dataDict = new DataDict();
        dataDict.setDescription("单元测试性别字典-男性");
        dataDict.setGroupCode("sex");
        dataDict.setParentGroupCode(Constant.DEFAULT_PARENT_CODE);
        dataDict.setDictKey("male");
        dataDict.setDictValue("男性");
        dataDict.setDictOrder(1);
        createResult = dataDictService.createDataDict(dataDict);
        Assert.assertTrue(ResultVO.SUCCESS.equals(createResult.getErrorCode()));
    }

    @Test
    public void testCreateDataDicts() {
        ResultVO createResult = dataDictService.createDataDicts(null);
        Assert.assertTrue(ResultVO.FAIL.equals(createResult.getErrorCode()));

        List<DataDict> sexDataDicts = new ArrayList<>(2);
        DataDict maleDataDict = new DataDict();
        maleDataDict.setDescription("单元测试性别字典-男性");
        maleDataDict.setGroupCode("sex");
        maleDataDict.setParentGroupCode(Constant.DEFAULT_PARENT_CODE);
        maleDataDict.setDictKey("male");
        maleDataDict.setDictValue("男性");
        maleDataDict.setDictOrder(1);
        sexDataDicts.add(maleDataDict);
        DataDict femaleDataDict = new DataDict();
        femaleDataDict.setDescription("单元测试性别字典-女性");
        femaleDataDict.setGroupCode("sex");
        femaleDataDict.setParentGroupCode(Constant.DEFAULT_PARENT_CODE);
        femaleDataDict.setDictKey("female");
        femaleDataDict.setDictValue("女性");
        femaleDataDict.setDictOrder(2);
        sexDataDicts.add(femaleDataDict);
        createResult = dataDictService.createDataDicts(sexDataDicts);
        Assert.assertTrue(ResultVO.SUCCESS.equals(createResult.getErrorCode()));
    }

    @Test
    public void testFindDataDictsByPage() {
        List<DataDict> dataDicts = buildDataDcitsData();
        dataDictService.createDataDicts(dataDicts);
        Page<DataDict> dataDictPage = dataDictService.findDataDictsByPage(new DataDict());
        Assert.assertTrue(dataDictPage.getTotalElements() == TOTAL_BATCH_SIZE);
    }

    @Test
    public void testFindDataDictsByGroupCode() {
        List<DataDict> dataDicts = buildDataDcitsData();
        dataDictService.createDataDicts(dataDicts);
        List<DataDict> dataDictRsults = dataDictService.findDataDictsByGroupCode("test");
        Assert.assertNotNull(dataDictRsults);
    }

    @Test
    public void testFindDataDictByGroupCodeAndDictKey() {
        List<DataDict> dataDicts = buildDataDcitsData();
        dataDictService.createDataDicts(dataDicts);
        DataDict dataDict = dataDictService.findDataDictByGroupCodeAndDictKey("test", "dictKey1");
        Assert.assertNotNull(dataDict);
    }

    @After
    public void clear() {
        List<DataDict> dataDicts = dataDictDao.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(root.get("description").as(String.class), "单元测试%"));
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        });
        if (CollectionUtils.isNotEmpty(dataDicts)) {
            dataDictDao.delete(dataDicts);
        }
        LOGGER.debug("DataDictServiceTest清理单元测试数据成功！{}", ArrayUtils.toString(dataDicts));
    }

    private List<DataDict> buildDataDcitsData() {
        List<DataDict> dataDicts = new ArrayList<>();
        for (int i = 0; i < TOTAL_BATCH_SIZE; i++) {
            DataDict dataDict = new DataDict();
            dataDict.setDictOrder(i + 1);
            dataDict.setDictKey("dictKey" + i);
            dataDict.setDictValue("dictValue" + i);
            dataDict.setDescription("单元测试数据字典");
            dataDict.setGroupCode("test");
            dataDict.setParentGroupCode(Constant.DEFAULT_PARENT_CODE);
            dataDicts.add(dataDict);
        }
        return dataDicts;
    }
}
