package com.jackie0.common.utils;

import com.jackie0.common.BaseSprintTestCase;
import com.jackie0.common.enumeration.ExcelType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel导出工具测试类
 *
 * @author jackie0
 * @since Java8
 * date 2016-11-10 11:26
 */
public class ExcelExporterTest extends BaseSprintTestCase {
    @Test
    public void testExportTo() {
        List<Map<String, String>> dataList = new ArrayList<>(100000);
        for (int i = 0; i < 100000; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("name", "jakie" + i);
            data.put("id", Integer.toString(i));
            dataList.add(data);
        }

        ExcelStyle.TitleStyle titleStyle = new ExcelStyle.TitleStyle();
        titleStyle.setFontHeightInPoints((short) 12);
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Map<String, ExcelStyle.TitleStyle> titleStyleMap = new HashMap<>();
        titleStyleMap.put("id", titleStyle);
        titleStyleMap.put("name", titleStyle);

        ExcelExporter simpleExcelExporter = new ExcelExporter(ExcelType.XLSX, dataList, new String[]{"id", "name"});
        simpleExcelExporter.exportTo(UrlUtils.getFilePathByBase("\\test\\simpleExcelExporter.xlsx"));

        ExcelExporter titleExcelExporter = new ExcelExporter(ExcelType.XLSX, dataList, new String[]{"id", "name"}, new String[]{"编号", "姓名"});
        titleExcelExporter.exportTo(UrlUtils.getFilePathByBase("\\test\\titleExcelExporter.xlsx"));

        ExcelExporter titleAndTitleStyleExcelExporter = new ExcelExporter(ExcelType.XLSX, dataList, new String[]{"id", "name"}, new String[]{"编号", "姓名"}, titleStyleMap);
        titleAndTitleStyleExcelExporter.exportTo(UrlUtils.getFilePathByBase("\\test\\titleAndTitleStyleExcelExporter.xlsx"));

        ExcelStyle.ColumnStyle idDataStyle = new ExcelStyle.ColumnStyle();
        idDataStyle.setColumnWidth(2000);
        idDataStyle.setColor(IndexedColors.RED.getIndex());

        ExcelStyle.ColumnStyle nameDataStyle = new ExcelStyle.ColumnStyle();
        nameDataStyle.setColumnWidth(4000);

        Map<String, ExcelStyle.ColumnStyle> dataStyleMap = new HashMap<>();
        dataStyleMap.put("id", idDataStyle);
        dataStyleMap.put("name", nameDataStyle);
        ExcelExporter titleAndTitleStyleAndDataStyleExcelExporter = new ExcelExporter(ExcelType.XLSX, dataList, new String[]{"id", "name"}, new String[]{"编号", "姓名"}, titleStyleMap, dataStyleMap);
        titleAndTitleStyleAndDataStyleExcelExporter.exportTo(UrlUtils.getFilePathByBase("\\test\\titleAndTitleStyleAndDataStyleExcelExporter.xlsx"));
    }
}
