package com.jackie0.common.utils;

import com.jackie0.common.constant.CommonExceptionCodeConstant;
import com.jackie0.common.exception.BusinessException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 利用POI组件加载Excel文件的工具类
 *
 * @author jackie0
 * @since Java8
 * date 2016-11-03 10:26
 */
public class ExcelLoadUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelLoadUtils.class);

    private static final String CREATE_WORKBOOK_ERROR = "ExcelLoadUtils创建Excel工作簿异常-->";

    private static final String CLOSE_WORKBOOK_ERROR = "ExcelLoadUtils关闭Excel工作簿异常-->";

    private ExcelLoadUtils() {
        // 防止new对象
    }

    /**
     * 从Excel获取数据，并以Map形式返回
     *
     * @param excelPath  Excel路径
     * @param fieldNames 每列数据对应的属性名称，即：每列数据分别在Map中的key值
     * @return 以Map形式返回，所有数据以String处理
     */
    public static List<Map<String, String>> getExcelDataListAsMap(String excelPath, String[] fieldNames) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(excelPath));
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error(CREATE_WORKBOOK_ERROR, e);
            throw new BusinessException(CommonExceptionCodeConstant.CREATE_EXCEL_WORKBOOK_ERROR);
        }
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        List<Map<String, String>> excelDataList = loadExcelDataList(fieldNames, sheetIterator);
        try {
            workbook.close();
        } catch (IOException e) {
            LOGGER.error(CLOSE_WORKBOOK_ERROR, e);
        }
        return excelDataList;

    }

    /**
     * 从Excel获取数据，并以Map形式返回
     *
     * @param excelInputStream Excel文件流
     * @param fieldNames       每列数据对应的属性名称，即：每列数据分别在Map中的key值
     * @return 以Map形式返回，所有数据以String处理
     */
    public static List<Map<String, String>> getExcelDataListAsMap(InputStream excelInputStream, String[] fieldNames) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(excelInputStream);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error(CREATE_WORKBOOK_ERROR, e);
            throw new BusinessException(CommonExceptionCodeConstant.CREATE_EXCEL_WORKBOOK_ERROR);
        }
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        List<Map<String, String>> excelDataList = loadExcelDataList(fieldNames, sheetIterator);
        try {
            workbook.close();
        } catch (IOException e) {
            LOGGER.error(CLOSE_WORKBOOK_ERROR, e);
        }
        return excelDataList;

    }

    /**
     * 从Excel获取数据，并以Bean形式返回，Bean必须又公共的get、set方法
     *
     * @param excelPath  Excel路径
     * @param fieldNames 每列数据对应的属性名称
     * @param clazz      Bean的Class类型
     * @param <T>        Bean的泛型类型
     * @return 以Bean形式返回，所有数据以String处理
     */
    public static <T> List<T> getExcelDataListAsBean(String excelPath, String[] fieldNames, Class<T> clazz) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(excelPath));
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error(CREATE_WORKBOOK_ERROR, e);
            throw new BusinessException(CommonExceptionCodeConstant.CREATE_EXCEL_WORKBOOK_ERROR);
        }
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        List<T> excelDataList = loadExcelDataList(fieldNames, sheetIterator, clazz);
        try {
            workbook.close();
        } catch (IOException e) {
            LOGGER.error(CLOSE_WORKBOOK_ERROR, e);
        }
        return excelDataList;

    }

    /**
     * 从Excel获取数据，并以Bean形式返回，Bean必须又公共的get、set方法
     *
     * @param excelInputStream Excel文件流
     * @param fieldNames       每列数据对应的属性名称
     * @param clazz            Bean的Class类型
     * @param <T>              Bean的泛型类型
     * @return 以Bean形式返回，所有数据以String处理
     */
    public static <T> List<T> getExcelDataListAsBean(InputStream excelInputStream, String[] fieldNames, Class<T> clazz) {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(excelInputStream);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error(CREATE_WORKBOOK_ERROR, e);
            throw new BusinessException(CommonExceptionCodeConstant.CREATE_EXCEL_WORKBOOK_ERROR);
        }
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        List<T> excelDataList = loadExcelDataList(fieldNames, sheetIterator, clazz);
        try {
            workbook.close();
        } catch (IOException e) {
            LOGGER.error(CLOSE_WORKBOOK_ERROR, e);
        }
        return excelDataList;

    }

    private static List<Map<String, String>> loadExcelDataList(String[] fieldNames, Iterator<Sheet> sheetIterator) {
        List<Map<String, String>> excelDataList = new ArrayList<>();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                Map<String, String> rowDataMap = new HashMap<>();
                int cellIndex = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = cell.getStringCellValue();
                    rowDataMap.put(fieldNames[cellIndex], cellValue);
                    cellIndex++;
                }
                excelDataList.add(rowDataMap);
            }
        }
        return excelDataList;
    }

    private static <T> List<T> loadExcelDataList(String[] fieldNames, Iterator<Sheet> sheetIterator, Class<T> clazz) {
        List<T> excelDataList = new ArrayList<>();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                excelDataList.add(getCellDataAsBean(fieldNames, clazz, cellIterator));
            }
        }
        return excelDataList;
    }

    private static <T> T getCellDataAsBean(String[] fieldNames, Class<T> clazz, Iterator<Cell> cellIterator) {
        T rowData;
        try {
            rowData = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("ExcelLoadUtils以Bean形式读取Excel数据异常，根据{}无法创建对象！-->{}", clazz.toString(), e);
            throw new BusinessException(CommonExceptionCodeConstant.READ_EXCEL_DATA_ERROR, clazz.toString());
        }
        int cellIndex = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String cellValue = cell.getStringCellValue();
            try {
                BeanUtils.setProperty(rowData, fieldNames[cellIndex], cellValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("ExcelLoadUtils以Bean形式读取Excel数据异常，无法设置{}属性值！-->{}", fieldNames[cellIndex], e);
                throw new BusinessException(CommonExceptionCodeConstant.SET_EXCEL_DATA_ERROR, fieldNames[cellIndex]);
            }
            cellIndex++;
        }
        return rowData;
    }
}
