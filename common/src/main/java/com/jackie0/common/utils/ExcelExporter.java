package com.jackie0.common.utils;

import com.jackie0.common.enumeration.ExcelType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Excel导出处理类，支持简单的样式及简单列表数据格式
 * 目前还不支持复杂表头、合并单元格等复杂需求
 *
 * @author jackie0
 * @since Java8
 * date 2016-11-03 15:54
 */
public class ExcelExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExporter.class);

    private static final int MAX_SHEET_ROWS = 50000; // xls格式时每个sheet最大行，不包括表头

    /**
     * 工作薄
     */
    private Workbook workbook;

    /**
     * 工作表
     */
    private Sheet[] sheets;

    /**
     * 表头样式，key对应数据Map中的key或Bean中的属性，这里不直接使用POI的CellStyle是因为titleStyleMap还包括了其他样式
     */
    private Map<String, ExcelStyle.TitleStyle> titleStyleMap;

    /**
     * 表头样式titleStyleMap，对应POI组件的CellStyle
     */
    private Map<String, CellStyle> titleCellStyleMap;

    /**
     * 数据表格样式，解释参考titleStyleMap
     *
     * @see #titleStyleMap
     */
    private Map<String, ExcelStyle.ColumnStyle> dataStyleMap;

    /**
     * 数据表格样式dataStyleMap，对应POI组件的CellStyle
     */
    private Map<String, CellStyle> dataCellStyleMap;

    /**
     * 数据，目前只支持Map及Bean类型的数据
     */
    private Collection data;

    /**
     * excel类型，{@link ExcelStyle}
     */
    private ExcelType excelType;

    /**
     * 对应数据Map中的key或Bean中的属性
     */
    private String[] fields;

    /**
     * 标题名称，顺序与{@link #fields}一一对应
     */
    private String[] titleNames;

    public ExcelExporter(ExcelType excelType, Collection data, String[] fields) {
        this(excelType, data, fields, null);
    }

    public ExcelExporter(ExcelType excelType, Collection data, String[] fields, String[] titleNames) {
        this.excelType = excelType;
        this.data = data;
        this.fields = fields;
        this.titleNames = titleNames;
        initWorkbook();
        initSheets();
    }

    public ExcelExporter(ExcelType excelType, Collection data, String[] fields, String[] titleNames, Map<String, ExcelStyle.TitleStyle> titleStyleMap) {
        this(excelType, data, fields, titleNames, titleStyleMap, null);
    }

    public ExcelExporter(ExcelType excelType, Collection data, String[] fields, String[] titleNames, Map<String, ExcelStyle.TitleStyle> titleStyleMap, Map<String, ExcelStyle.ColumnStyle> dataStyleMap) {
        this.excelType = excelType;
        this.data = data;
        this.fields = fields;
        this.titleNames = titleNames;
        this.titleStyleMap = titleStyleMap;
        this.dataStyleMap = dataStyleMap;
        initWorkbook();
        initSheets();
        initStyles();
    }

    /**
     * 导出Excel到指定输出流
     *
     * @param exportToOutputStream 目标输出流
     */
    public void exportTo(OutputStream exportToOutputStream) {
        generateDataExcel();
        try {
            workbook.write(exportToOutputStream);
        } catch (IOException e) {
            LOGGER.error("导出数据到指定Excel时发生未知异常！", e);
        } finally {
            closeWorkbook();
        }
    }

    /**
     * 导出Excel到指定文件，如果文件不存在会尝试创建此文件
     *
     * @param exportToFilePath 目标Excel文件路径
     */
    public void exportTo(String exportToFilePath) {
        File targetExcel = new File(exportToFilePath);
        if (!targetExcel.exists()) {
            try {
                boolean ret = targetExcel.createNewFile();
                LOGGER.debug("{}文件创建成功，createNewFile()执行结果：{}", exportToFilePath, ret);
            } catch (IOException e) {
                LOGGER.error("导出数据到指定Excel：{}时异常，文件不存在且系统无法创建此文件！", exportToFilePath, e);
            }
        }
        try (OutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(targetExcel))) {
            exportTo(fileOutputStream);
        } catch (IOException e) {
            LOGGER.error("导出数据到指定Excel：{}时异常，文件不存在！", exportToFilePath, e);
        }
    }

    /**
     * 生成单元格，并填充数据
     */
    private void generateDataExcel() {
        for (int i = 0; i < sheets.length; i++) {
            Sheet sheet = sheets[i];
            if (ArrayUtils.isNotEmpty(titleNames)) {
                // 生成表头
                List<Map<String, String>> titleDataList = new ArrayList<>(1);
                titleDataList.add(generateTitleMap(titleNames, fields)); // 把表头当成一行数据
                generateDataRow(sheet, titleDataList);
            }
            Collection sheetData; // 每个sheet页的数据，数据按MAX_SHEET_ROWS分页了，生成数据表格前，先从所有的数据中抽取每个sheet的数据sheetData
            if (CollectionUtils.isNotEmpty(data)) {
                sheetData = new ArrayList<>(MAX_SHEET_ROWS);
                Object[] dataArray = data.toArray();
                int sheetDataStartIndex = MAX_SHEET_ROWS * i; // sheet页数据在所有数据data中的起始下标，开区间
                int sheetDataEndIndex = MAX_SHEET_ROWS * i + MAX_SHEET_ROWS; // sheet页数据在所有数据data中的结束下标，闭区间
                CollectionUtils.addAll(sheetData, ArrayUtils.subarray(dataArray, sheetDataStartIndex, sheetDataEndIndex));
                generateDataRow(sheet, sheetData); // 生成数据表格
            }
        }
    }

    /**
     * 根据表头名称和数据属性名称一一对应生成表头数据
     * 如：titleNames:["编号","姓名"]，fields:["id","name"]生成：{"id":"编号","name":"姓名"}
     *
     * @param titleNames 表头每列的名称
     * @param fields     要导出数据的字段名称
     * @return 表头数据
     */
    private static Map<String, String> generateTitleMap(String[] titleNames, String[] fields) {
        Map<String, String> titleMap = null;
        if (ArrayUtils.isNotEmpty(fields)) {
            titleMap = new LinkedHashMap<>(fields.length);
            for (int i = 0; i < fields.length; i++) {
                if (titleNames != null && i < titleNames.length) {
                    titleMap.put(fields[i], titleNames[i]);
                } else {
                    titleMap.put(fields[i], fields[i]);
                }
            }
        }
        return titleMap;
    }

    /**
     * 生成Excel行，并填充数据
     *
     * @param sheet     工作表
     * @param excelData 要导出的数据
     */
    private void generateDataRow(Sheet sheet, Collection excelData) {
        if (CollectionUtils.isNotEmpty(excelData)) {
            for (Object rowData : excelData) {
                // 注：getPhysicalNumberOfRows只是当前sheet实际的行数，并不是最大行的下标，在逐行创建数据时，刚好getPhysicalNumberOfRows()就是当前要创建行的下标。如果有跳行不能用此法。
                Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
                generateDataCell(row, rowData);
            }
        }
    }

    /**
     * 创建单元格，并填充数据
     *
     * @param row     Excel中的行
     * @param rowData 要导出的一行数据
     */
    private void generateDataCell(Row row, Object rowData) {
        String dataValue;
        if (ArrayUtils.isNotEmpty(fields)) {
            for (String field : fields) {
                dataValue = getDataValue(rowData, field);
                Cell cell = createCell(row, dataValue);
                if (row.getSheet().getPhysicalNumberOfRows() == 1 && ArrayUtils.contains(titleNames, dataValue) && MapUtils.isNotEmpty(titleCellStyleMap)) {
                    cell.setCellStyle(titleCellStyleMap.get(field));
                } else if (MapUtils.isNotEmpty(dataCellStyleMap)) {
                    cell.setCellStyle(dataCellStyleMap.get(field));
                }
            }
        }
    }

    // 创建单元格
    private Cell createCell(Row row, String dataValue) {
        Cell cell = null;
        if (row != null) {

            // 注：getPhysicalNumberOfCells只是当前行实际的列数，并不是最大列下标，在逐列创建数据时，刚好getPhysicalNumberOfCells()就是当前要创建列的下标。如果有跳列不能用此法。
            cell = row.createCell(row.getPhysicalNumberOfCells());

            // 暂不支持数据类型，所有数据统一当作字符串处理，有需要可扩展
            cell.setCellValue(dataValue);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        return cell;
    }

    /**
     * 获取单元格数据值并以String返回
     *
     * @param rowData 一行数据，目前支持Bean和Map
     * @param field   对应rowData的Bean属性或Map的key
     * @return 指定单元格数据
     */
    private String getDataValue(Object rowData, String field) {
        String dataValue;
        if (rowData instanceof Map) {
            dataValue = Objects.toString(((Map) rowData).get(field), "");
        } else {
            try {
                dataValue = BeanUtils.getProperty(rowData, field);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                dataValue = "";
                LOGGER.error("从原始数据，获取要导出Excel的属性{}的值异常！", field, e);
            }
        }
        return dataValue;
    }

    /**
     * 根据{@link ExcelType}初始化工作薄
     */
    private void initWorkbook() {
        // 默认创建xlsx缓存导出的工作薄
        Workbook thisWorkbook;
        if (excelType.getValue() == ExcelType.XLS.getValue()) {
            thisWorkbook = new HSSFWorkbook();
        } else {
            thisWorkbook = new SXSSFWorkbook(200);
        }
        workbook = thisWorkbook;
    }

    /**
     * 根据要导出的数据初始化工作表
     */
    private void initSheets() {
        Sheet[] thisSheets;
        int sheetSize = 1; // 工作薄的页数，默认1没数据也导个空表格
        if (CollectionUtils.isNotEmpty(data)) {
            if (data.size() % MAX_SHEET_ROWS > 0) {
                sheetSize = data.size() / MAX_SHEET_ROWS + 1;
            } else if (data.size() % MAX_SHEET_ROWS == 0) {
                sheetSize = data.size() / MAX_SHEET_ROWS;
            }
        }
        thisSheets = new Sheet[sheetSize];
        for (int i = 0; i < sheetSize; i++) {
            thisSheets[i] = workbook.createSheet("sheet" + (i + 1));
        }
        sheets = thisSheets;
    }

    /**
     * 关闭工作薄
     */
    private void closeWorkbook() {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("关闭Excel工作薄时异常！", e);
            }
        }
    }

    /**
     * 初始化样式
     * 把自定义的{@link ExcelStyle}转换为POI组件支持的{@link CellStyle}同时生成sheet层面样式
     */
    private void initStyles() {
        if (MapUtils.isNotEmpty(titleStyleMap)) {
            initCellStyleAndSheetStyle(titleStyleMap);
        }

        if (MapUtils.isNotEmpty(dataStyleMap)) {
            initCellStyleAndSheetStyle(dataStyleMap);
        }
    }

    private void initCellStyleAndSheetStyle(Map<String, ? extends ExcelStyle.ColumnStyle> columnStyle) {
        Map<String, CellStyle> cellStyleMap = new HashMap<>(columnStyle.size());
        boolean isTitleStyle = false;
        for (Map.Entry<String, ? extends ExcelStyle.ColumnStyle> columnStyleEntry : columnStyle.entrySet()) {
            isTitleStyle = columnStyleEntry.getValue() instanceof ExcelStyle.TitleStyle;
            cellStyleMap.put(columnStyleEntry.getKey(), ExcelStyle.generateCellStyleByColumnStyle(columnStyleEntry.getValue(), workbook));
            if (columnStyleEntry.getValue().getColumnWidth() > 0) {
                // 设置列宽，如果ExcelStyle.ColumnStyle和ExcelStyle.TitleStyle都有这个参数且不同，则以ExcelStyle.ColumnStyle为准，因为initCellStyleAndSheetStyle(dataStyleMap);在后面调用
                for (Sheet sheet : sheets) {
                    sheet.setColumnWidth(ArrayUtils.indexOf(fields, columnStyleEntry.getKey()), columnStyleEntry.getValue().getColumnWidth());
                }
            }
        }
        if (isTitleStyle) {
            titleCellStyleMap = cellStyleMap;
        } else {
            dataCellStyleMap = cellStyleMap;
        }
    }
}
