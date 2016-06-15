/**
 * Copyright (C),Kingmed
 *
 * @FileName: SmsUtils.java
 * @Package: com.kingmed.ws.util
 * @Description: 短信处理工具类
 * @Author linjiaju
 * @Date 2015年11月30日 16:45
 * @History: //修改记录
 * 〈author〉      〈time〉      〈version〉       〈desc〉
 * 修改人姓名            修改时间            版本号              描述
 */
package com.jackie0.common.utils;

import com.jackie0.common.enumeration.ExcelType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * excel 工具类， 支持xls，xlsx
 * User: linjj
 * Date: 13-9-24
 * Time: 下午8:37
 */
public class ExcelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtils.class);

    private static final int MAX_SHEET_ROWS = 50000; // xls格式时每个sheet最大行
    private static final int MAX_EXPORT_LINE = 100000; // 最大导出行

    private ExcelUtils() {
    }

    public static <T> void exportData(Map<String, String> titleMap, Collection<T> data, Excel.TitleStyle titleStyle, ExcelType exportAs, OutputStream out) {
        exportData(titleMap, data, null, titleStyle, exportAs, out);
    }

    /**
     * 导出Excel
     *
     * @param titleMap     字段属性:标题Map
     * @param data         数据：只处理Collection<Map<String,Object>>及Collection<Entity>两种数据形式
     * @param columnStyles 列样式，根据{@link Excel.ColumnStyle#columnField}确定是哪列的样式
     * @param titleStyle   标题样式如果为空默认不导标题
     * @param exportAs     导出方式，ExcelUtils.EXPORT_AS_XLS, ExcelUtils.EXPORT_AS_XLSX
     * @param out          导出的excel的输出流
     * @param <T>          数据的泛型类型
     */
    public static <T> void exportData(Map<String, String> titleMap, Collection<T> data, List<Excel.ColumnStyle> columnStyles, Excel.TitleStyle titleStyle, ExcelType exportAs, OutputStream out) {
        Workbook workbook = createWorkbook(exportAs);
        createSheets(workbook, titleMap, data, exportAs, columnStyles, titleStyle);
        writeWorkbook(workbook, out);
    }

    public static <T> void exportData(List<String> titles, List<String> fields, Collection<T> data, Excel.TitleStyle titleStyle, ExcelType exportAs, OutputStream out) {
        exportData(titles, fields, data, null, titleStyle, exportAs, out);
    }

    /**
     * 导出Excel
     *
     * @param titles       标题
     * @param fields       字段名与标题对应
     * @param data         数据
     * @param columnStyles 列样式，根据{@link Excel.ColumnStyle#columnField}确定是哪列的样式
     * @param titleStyle   标题样式如果为空默认不导标题
     * @param exportAs     导出方式，ExcelUtils.EXPORT_AS_XLS, ExcelUtils.EXPORT_AS_XLSX
     * @param out          导出的excel的输出流
     * @param <T>          数据的泛型类型
     */
    public static <T> void exportData(List<String> titles, List<String> fields, Collection<T> data, List<Excel.ColumnStyle> columnStyles, Excel.TitleStyle titleStyle, ExcelType exportAs, OutputStream out) {
        exportData(createTitleMap(titles, fields), data, columnStyles, titleStyle, exportAs, out);
    }


    /**
     * 创建工作薄
     *
     * @param exportAs 导出方式1 xls 2 xlsx
     * @return Workbook
     */
    private static Workbook createWorkbook(ExcelType exportAs) {
        // 默认创建xlsx缓存导出的工作薄
        Workbook workbook = new SXSSFWorkbook(200);
        if (exportAs.getValue() == ExcelType.XLS.getValue()) {
            workbook = new HSSFWorkbook();
        }
        return workbook;
    }

    /**
     * 创建excel sheet
     *
     * @param workbook     工作薄
     * @param titleMap     标题字段Map
     * @param data         数据
     * @param exportAs     导出方式
     * @param columnStyles 列样式，根据{@link Excel.ColumnStyle#columnField}确定是哪列的样式
     * @param titleStyle   标题样式如果为空默认不导标题
     */
    private static <T> void createSheets(Workbook workbook, Map<String, String> titleMap, Collection<T> data, ExcelType exportAs, List<Excel.ColumnStyle> columnStyles, Excel.TitleStyle titleStyle) {
        Sheet[] sheets = null;
        if (data == null || data.isEmpty() || exportAs.getValue() == ExcelType.XLSX.getValue()) {
            // 如果是xlsx格式导出则数据放在一个sheet
            sheets = new Sheet[]{workbook.createSheet("sheet1")};
        } else {
            if (exportAs.getValue() == ExcelType.XLS.getValue()) {
                // 如果是xls格式导出则每MAX_SHEET_ROWS行分一个sheet
                sheets = new Sheet[data.size() / MAX_SHEET_ROWS + 1];
                for (int i = 0; i <= data.size() / MAX_SHEET_ROWS; i++) {
                    sheets[i] = workbook.createSheet("sheet" + (i + 1));
                }
            }
        }
        createRows(sheets, titleMap, data, columnStyles, titleStyle);
    }

    /**
     * 创建导出行
     *
     * @param sheets       excel sheet
     * @param titleMap     标题
     * @param data         数据
     * @param columnStyles 列样式，根据{@link Excel.ColumnStyle#columnField}确定是哪列的样式
     * @param titleStyle   标题样式如果为空默认不导标题
     */
    private static <T> void createRows(Sheet[] sheets, Map<String, String> titleMap, Collection<T> data, List<Excel.ColumnStyle> columnStyles, Excel.TitleStyle titleStyle) {
        Row row;
        int dataIndex = 0; // 导出数据的数组下标
        for (Sheet sheet : sheets) {
            int cellIndex = 0; // 单元格下标
            int rowIndex = 0;  // 当前sheet行数下标
            if (titleMap != null && !titleMap.isEmpty()) {
                if (titleStyle != null) {
                    // 创建列头
                    row = sheet.createRow(rowIndex);
                    CellStyle titleCellStyle = Excel.getTitleStyle(row, titleStyle);
                    for (Map.Entry<String, String> titleMapEntry : titleMap.entrySet()) {
                        Map.Entry entry = (Map.Entry) titleMapEntry;
                        //String cellKey = Objects.toString(entry.getKey(), "");
                        String cellData = Objects.toString(entry.getValue(), "");
                        createCell(row, cellData, cellIndex, titleCellStyle);
                        //sheet.setColumnWidth(cellIndex, Excel.findColumnStyle(columnStyles, cellKey).getColumnWidth()); 20160418 因为性能及样式兼容性问题，先屏蔽单元格样式
                        cellIndex++;
                    }
                    rowIndex++;
                }
                if (data != null && !data.isEmpty()) {
                    Iterator<T> dataIterator = data.iterator();
                    for (; dataIterator.hasNext() && dataIndex < MAX_EXPORT_LINE; dataIndex++) {
                        // 数据最大导出限制为MAX_EXPORT_LINE
                        T dataT = dataIterator.next();
                        row = sheet.createRow(rowIndex);
                        cellIndex = 0;
                        for (Map.Entry<String, String> titleMapEntry : titleMap.entrySet()) {
                            Map.Entry entry = (Map.Entry) titleMapEntry;
                            String cellKey = Objects.toString(entry.getKey(), "");
                            Object value;
                            if (dataT instanceof Map) {
                                value = ((Map) dataT).get(cellKey);
                            } else {
                                PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(dataT.getClass(), cellKey);
                                try {
                                    // 从get方法读取属性值
                                    value = propertyDescriptor != null ? propertyDescriptor.getReadMethod().invoke(dataT) : null;
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    LOGGER.error("导出数据时，从实体-->{}中获取属性-->{}异常，设置该值为null！", dataT.getClass().toString(), cellKey);
                                    value = null;
                                }
                            }
                            //createCell(row, value, cellIndex, Excel.findCellStyle(row, columnStyles, cellKey)); modify by linjiaju 20160418 因为性能及样式兼容性问题，先屏蔽单元格样式
                            createCell(row, value, cellIndex, null);
                            cellIndex++;
                        }
                        rowIndex++;
                        if (rowIndex > MAX_SHEET_ROWS && sheets.length > 1) {
                            // 如果超过每页最大数则进行excel分页,把数据存到下一个sheet中,此逻辑主要针对xls格式导出方式
                            dataIndex++;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 创建单元格
     *
     * @param row       行
     * @param cellData  单元格数据
     * @param cellIndex 单元格下标
     * @param style     样式
     */
    private static void createCell(Row row, Object cellData, int cellIndex, CellStyle style) {
        if (row != null) {
            Cell cell = row.createCell(cellIndex);

            // 暂不支持数据类型，所有数据统一当作字符串处理，有需要可扩展
            cell.setCellValue(Objects.toString(cellData, ""));
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (style != null) {
                cell.setCellStyle(style);
            }
        }
    }


    private static Map<String, String> createTitleMap(List<String> titles, List<String> fields) {
        Map<String, String> titleMap = null;
        if (fields != null && !fields.isEmpty()) {
            titleMap = new LinkedHashMap<>(fields.size());
            for (int i = 0; i < fields.size(); i++) {
                if (titles != null && i < titles.size()) {
                    titleMap.put(fields.get(i), titles.get(i));
                } else {
                    titleMap.put(fields.get(i), fields.get(i));
                }
            }
        }
        return titleMap;
    }

    private static void writeWorkbook(Workbook workbook, OutputStream out) {
        try {
            workbook.write(out);
        } catch (IOException e) {
            LOGGER.error("数据导出失败-->", e);
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException ce) {
                LOGGER.error("writeWorkbook finally exception ", ce);
            }
            LOGGER.info("writeWorkbook finally ");
        }
    }
}