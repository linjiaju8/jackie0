package com.jackie0.common.utils;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.entity.DataDict;
import com.jackie0.common.enumeration.ExcelType;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导入工具测试类
 *
 * @author jackie0
 * @since Java8
 * date 2016-11-10 15:20
 */
public class ExcelLoadUtilsTest extends BaseSprintTestCase {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelLoadUtilsTest.class);

    @Before
    public void beforeExcelLoadUtilsTest() {
        List<Map<String, String>> dataList = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("name", "jakie" + i);
            data.put("id", Integer.toString(i));
            dataList.add(data);
        }
        ExcelExporter simpleExcelExporter = new ExcelExporter(ExcelType.XLSX, dataList, new String[]{"id", "name"});
        simpleExcelExporter.exportTo(UrlUtils.getFilePathByBase("\\test\\simpleExcelExporter.xlsx"));
    }

    @Test
    public void testGetExcelDataListAsMap() throws FileNotFoundException {
        List<Map<String, String>> dataMapList = ExcelLoadUtils.getExcelDataListAsMap(UrlUtils.getFilePathByBase("\\test\\simpleExcelExporter.xlsx"), new String[]{"id", "name"});
        LOGGER.info("Path导入Excel数据{}", ArrayUtils.toString(dataMapList));

        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(UrlUtils.getFilePathByBase("\\test\\simpleExcelExporter.xlsx"))));
        dataMapList = ExcelLoadUtils.getExcelDataListAsMap(inputStream, new String[]{"id", "name"});
        LOGGER.info("InputStream导入Excel数据{}", ArrayUtils.toString(dataMapList));
    }

    @Test
    public void testGetExcelDataListAsBean() throws FileNotFoundException {
        List<DataDict> dataDicts = ExcelLoadUtils.getExcelDataListAsBean(UrlUtils.getFilePathByBase("\\test\\simpleExcelExporter.xlsx"), new String[]{"dictKey", "dictValue"}, DataDict.class);
        LOGGER.info("Path导入Excel数据{}", ArrayUtils.toString(dataDicts));

        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File(UrlUtils.getFilePathByBase("\\test\\simpleExcelExporter.xlsx"))));
        dataDicts = ExcelLoadUtils.getExcelDataListAsBean(inputStream, new String[]{"dictKey", "dictValue"}, DataDict.class);
        LOGGER.info("InputStream导入Excel数据{}", ArrayUtils.toString(dataDicts));
    }
}
