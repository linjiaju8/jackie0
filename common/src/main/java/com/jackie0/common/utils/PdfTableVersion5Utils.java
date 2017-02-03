package com.jackie0.common.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.jackie0.common.constant.Constant;
import com.jackie0.common.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class PdfTableVersion5Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfTableVersion5Utils.class);

    private static final String DEFAULT_TABLE_TEMPLET = "default";
    private static final String DEFAULT_TABLE_TEMPLET_PATH = "templet/pdfTables/";
    private static Map<Integer, Font> FONT_CACHE = new LinkedHashMap<>();

    /**
     * 生成PDF表格
     *
     * @param tableTemplet  PDF表格对应的HTML模板名称，只需传文件名即可，无需加后缀名，模板在指定目录templet/pdfTables/下
     * @param data          原数据
     * @param addToDocument 表格要加入的PDF文档对象
     * @param pdfWriter     PdfWriter对象，用于设置事件回调
     */
    public static void generatePdfTable(String tableTemplet, Object data, Document addToDocument, PdfWriter pdfWriter) {
        Element pdfTableTemplet = parseByTableTemplet(tableTemplet); // 根据HTML模板解析获取table元素
        if (pdfTableTemplet == null) {
            throw new BusinessException("COMMON_ERROR_12");
        }
        PdfPTable table = generatePdfTableByTemplet(pdfTableTemplet, data); // 根据模板生成PDFtable对象
        if (table != null) {
            //setPageAndTableEvent(pdfWriter, pdfTableTemplet, table); // 设置事件回调
            int headerRowNum = generatePdfHeader(table, data, pdfTableTemplet, false); // 生成表头
            int footerRowNum = generatePdfHeader(table, data, pdfTableTemplet, true); // 生成表尾
            table.setHeaderRows(headerRowNum + footerRowNum); // header行从第1行到第8行
            table.setFooterRows(footerRowNum); // footer行是header行的子集，从8-4行开始
            generatePdfBody(table, data, addToDocument, pdfTableTemplet); // 生成表格数据
            try {
                addToDocument.add(table);
            } catch (DocumentException e) {
                LOGGER.error("把table加入文档时异常：", e);
                throw new BusinessException("COMMON_ERROR_15");
            }
        }
    }

    private static void generatePdfBody(PdfPTable table, Object data, Document addToDocument, Element pdfTableTemplet) {
        Elements tbodyElements = pdfTableTemplet.getElementsByTag("tbody");
        List bodyDataList;
        if (CollectionUtils.isNotEmpty(tbodyElements) && tbodyElements.get(0).hasAttr("dataList")) {
            Element tbodyElement = pdfTableTemplet.getElementsByTag("tbody").get(0);
            String tbodyDataListExpression = tbodyElement.attr("dataList");
            if (StringUtils.isBlank(tbodyDataListExpression)) {
                bodyDataList = (List) data;
            } else {
                bodyDataList = DataUtils.getFieldByExpression(data, tbodyDataListExpression, List.class);
            }
            Elements tbodyTrElements = tbodyElement.getElementsByTag("tr");
            if (CollectionUtils.isNotEmpty(bodyDataList)) {
                createDataBody(table, bodyDataList, tbodyTrElements, data);
            }
        }
    }

    private static void createDataBody(PdfPTable table, List bodyDataList, Elements tbodyTrElements, Object data) {
        for (Object tbodyData : bodyDataList) {
            if (CollectionUtils.isNotEmpty(tbodyTrElements)) {
                createDataBodyByTmp(table, tbodyTrElements, tbodyData, data);
            } else {
                createDataBodyByData(table, tbodyData);
            }
        }
    }

    private static void createDataBodyByData(PdfPTable table, Object tbodyData) {
        for (int i = 0; i < table.getNumberOfColumns(); i++) {
            StyleConfig styleConfig = new StyleConfig();
            if (tbodyData instanceof Map) {
                Set keySet = ((Map) tbodyData).keySet();
                Object[] keyArray = keySet.toArray(new Object[keySet.size()]);
                Object key = keyArray[i];
                Object cellData = DataUtils.getFieldByExpression(tbodyData, Objects.toString(key), Object.class);
                PdfPCell pdfPCell = new PdfPCell(new Phrase(Objects.toString(cellData, ""), styleConfig.getFont()));
                pdfPCell.setHorizontalAlignment(styleConfig.getAlign());
                table.addCell(pdfPCell);
            } else if (tbodyData != null) {
                Field[] fields = tbodyData.getClass().getFields();
                Field field = fields[i];
                Object cellData = DataUtils.getFieldByExpression(tbodyData, Objects.toString(field.getName()), Object.class);
                PdfPCell pdfPCell = new PdfPCell(new Phrase(Objects.toString(cellData, ""), styleConfig.getFont()));
                pdfPCell.setHorizontalAlignment(styleConfig.getAlign());
                table.addCell(pdfPCell);
            }
        }
    }

    private static void createDataBodyByTmp(PdfPTable table, Elements tbodyTrElements, Object tbodyData, Object data) {
        for (Element tbodyTrElement : tbodyTrElements) {
            StyleConfig rowStyleConfig = createStyleConfig(tbodyTrElement, null);
            Elements theadTdElements = tbodyTrElement.getElementsByTag("td");
            if (CollectionUtils.isNotEmpty(theadTdElements)) {
                generatePdfCell(theadTdElements, rowStyleConfig, table, tbodyData, data);
            }
        }
    }

    private static int generatePdfHeader(PdfPTable table, Object data, Element pdfTableTemplet, boolean isFooter) {
        int theadRowNum = 0;
        String theadOrTfoot = isFooter ? "tfoot" : "thead";
        String thOrTd = isFooter ? "td" : "th";
        Elements theadTrElements = pdfTableTemplet.select(theadOrTfoot + " > tr");
        if (CollectionUtils.isNotEmpty(theadTrElements)) {
            for (Element theadTrElement : theadTrElements) {
                StyleConfig rowStyleConfig = createStyleConfig(theadTrElement, null);
                Elements theadThElements = theadTrElement.getElementsByTag(thOrTd);
                if (CollectionUtils.isNotEmpty(theadThElements)) {
                    generatePdfCell(theadThElements, rowStyleConfig, table, data, null);
                }
                theadRowNum++;
            }
        }
        return theadRowNum;
    }

    private static void generatePdfCell(Elements cellElements, StyleConfig rowStyleConfig, PdfPTable table, Object data, Object rootData) {
        Object configData = rootData == null ? data : rootData;
        for (Element cellElement : cellElements) {
            if (cellElement.hasAttr("if")) {
                String ifExpression = cellElement.attr("if");
                boolean dynamicColIfVal = DataUtils.getFieldByExpression(configData, ifExpression, Boolean.class);
                if (!dynamicColIfVal) {
                    continue;
                }
            }
            StyleConfig cellStyleConfig = createStyleConfig(cellElement, rowStyleConfig);
            String cellText = parseDataText(cellElement.text(), data);
            PdfPCell pdfPCell = new PdfPCell(new Phrase(cellText, cellStyleConfig.getFont()));
            setCellStyle(cellElement, pdfPCell, cellStyleConfig, configData);
            table.addCell(pdfPCell);
        }
    }

    private static void setCellStyle(Element cellElement, PdfPCell pdfPCell, StyleConfig cellStyleConfig, Object data) {
        if (Rectangle.ALIGN_CENTER != cellStyleConfig.getAlign()) {
            // 只有不等于默认值时才设置值
            pdfPCell.setHorizontalAlignment(cellStyleConfig.getAlign());
        }

        if (Rectangle.NO_BORDER == cellStyleConfig.getBorder()) {
            // 只有设置不显示边框时才设置值
            pdfPCell.setBorder(cellStyleConfig.getBorder());
        }

        if (Rectangle.NO_BORDER == cellStyleConfig.getBorderTop()) {
            // 只有设置不显示边框时才设置值
            pdfPCell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
        }

        if (Rectangle.NO_BORDER == cellStyleConfig.getBorderLeft()) {
            // 只有设置不显示边框时才设置值
            pdfPCell.setBorder(Rectangle.TOP | Rectangle.RIGHT | Rectangle.BOTTOM);
        }

        if (Rectangle.NO_BORDER == cellStyleConfig.getBorderRight()) {
            // 只有设置不显示边框时才设置值
            pdfPCell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.BOTTOM);
        }

        if (Rectangle.NO_BORDER == cellStyleConfig.getBorderBottom()) {
            // 只有设置不显示边框时才设置值
            pdfPCell.setBorder(Rectangle.TOP | Rectangle.LEFT | Rectangle.RIGHT);
        }

        if (cellElement.hasAttr("colspan")) {
            // 合并单元格，colspan可以是表达式
            String colspanExpression = cellElement.attr("colspan");
            String colspan = parseDataText(colspanExpression, data);
            int colspanNum = 1;
            if (StringUtils.isNumeric(colspan)) {
                colspanNum = Integer.parseInt(colspan);
            }
            pdfPCell.setColspan(colspanNum);
        }

        if (cellElement.hasAttr("rowspan")) {
            // 合并行，rowspan可以是表达式
            String rowspanExpression = cellElement.attr("rowspan");
            String rowspan = parseDataText(rowspanExpression, data);
            int rowspanNum = 1;
            if (StringUtils.isNumeric(rowspan)) {
                rowspanNum = Integer.parseInt(rowspan);
            }
            pdfPCell.setRowspan(rowspanNum);
        }
    }

    /**
     * 解析数据表达式，支持{{a.b}}形式的数据表达式解析
     *
     * @param text 要解析的数据表达式
     * @param data 原数据
     * @return 解析后的数据
     */
    private static String parseDataText(String text, Object data) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String newText;
        if (text.contains("{{") && text.contains("}}")) {
            String[] newTexts = text.split("\\{\\{");
            newText = !text.startsWith("{{") ? newTexts[0] : "";
            newTexts = ArrayUtils.remove(newTexts, 0);
            for (String subText : newTexts) {
                if (StringUtils.isBlank(subText)) {
                    continue;
                }
                String[] subTexts = subText.split("}}");
                String dataExpression = subTexts.length > 0 ? subTexts[0] : null;
                String cellData = parseDataExpression(dataExpression, data);
                newText = StringUtils.isBlank(newText) ? cellData : (newText + cellData);
                if (subTexts.length > 1) {
                    newText = newText + subTexts[1];
                }
            }
        } else {
            newText = text;
        }
        return newText;
    }

    /**
     * 解析表达式数据
     *
     * @param dataExpression 表达式
     * @param data           原数据
     * @return 解析后的数据统一转为String类型返回
     */
    private static String parseDataExpression(String dataExpression, Object data) {
        String cellData;
        if (dataExpression.contains("\\|")) {
            String[] subDataExpressions = dataExpression.split("\\|");
            String subDataExpression = subDataExpressions[0];
            if (StringUtils.isNotBlank(subDataExpression) && subDataExpression.contains("?") && subDataExpression.contains(":")) {
                cellData = parseDataMultiObjectExpression(subDataExpression.trim(), data);
            } else {
                Object cellDataObject = DataUtils.getFieldByExpression(data, subDataExpression.trim(), Object.class);
                cellData = Objects.toString(cellDataObject, "");
            }
            String dataFormatExpression = null;
            if (subDataExpressions.length > 1) {
                dataFormatExpression = subDataExpressions[1];
            }
            String[] dataFormatExpressions = dataFormatExpression != null ? dataFormatExpression.split(":") : null;
            String dataType = null;
            if (dataFormatExpressions != null && dataFormatExpressions.length == 2) {
                dataType = dataFormatExpressions[0];
                dataFormatExpression = dataFormatExpressions[1];
            }
            if (StringUtils.isNotBlank(cellData) && StringUtils.isNotBlank(dataType) && StringUtils.isNotBlank(dataFormatExpression)) {
                cellData = formatData(cellData, dataType.trim(), dataFormatExpression.trim());
            }
        } else {
            if (dataExpression.contains("?") && dataExpression.contains(":")) {
                cellData = parseDataMultiObjectExpression(dataExpression.trim(), data);
            } else {
                Object cellDataObject = DataUtils.getFieldByExpression(data, dataExpression.trim(), Object.class);
                cellData = Objects.toString(cellDataObject, "");
            }
        }
        return cellData;
    }

    /**
     * 按指定数据类型格式化数据
     *
     * @param newData              需要格式化的数据
     * @param dataType             数据类型
     * @param dataFormatExpression 格式目前支持：date的各种格式、数字的位数
     * @return 格式化后的数据
     */
    private static String formatData(String newData, String dataType, String dataFormatExpression) {
        String newData1 = newData;
        if ("date".equalsIgnoreCase(dataType)) {
            try {
                Date dateData = DateFormat.getInstance().parse(newData);
                newData1 = DateFormatUtils.format(dateData, dataFormatExpression);
            } catch (ParseException e) {
                LOGGER.error("获取PDF单元格动态数据时异常，数据转换失败！", e);
                throw new BusinessException("COMMON_ERROR_16", newData);
            }
        } else if ("number".equalsIgnoreCase(dataType)) {
            BigDecimal numberData = new BigDecimal(newData);
            int count = Integer.parseInt(dataFormatExpression);
            numberData = numberData.setScale(count, BigDecimal.ROUND_HALF_UP);
            newData1 = numberData.toString();

        }
        return newData1;
    }

    /**
     * 解析多目运算符，目前只支持三目元算且只能返回常量
     *
     * @param subDataExpression 多目运算表达式，条件判断只支持数据表达式，且对应的数据值必须是boolean，不支持运算
     * @param data              原数据
     * @return 解析后的数据
     */
    private static String parseDataMultiObjectExpression(String subDataExpression, Object data) {
        String[] multiObjectExpressions = subDataExpression.split("\\?");
        String multiObjectExpression = multiObjectExpressions[0]; // 三目运算的条件
        String multiObjectValueExpression = multiObjectExpressions[1];
        String[] multiObjectValueExpressions = multiObjectValueExpression.split(":");
        boolean multiObjectExpressionVal = DataUtils.getFieldByExpression(data, multiObjectExpression, Boolean.class);
        if (multiObjectExpressionVal) {
            return multiObjectValueExpressions[0];
        } else {
            return multiObjectValueExpressions[1];
        }
    }

    private static StyleConfig createStyleConfig(Element element, StyleConfig defaultStyleConfig) {
        StyleConfig styleConfig = new StyleConfig();
        if (defaultStyleConfig != null) {
            BeanUtils.copyProperties(defaultStyleConfig, styleConfig);
        }
        if (element.hasAttr("font-size") && NumberUtils.isNumber(element.attr("font-size"))) {
            styleConfig.setFontSize(Integer.parseInt(element.attr("font-size")));
        }
        if (element.hasAttr("align")) {
            styleConfig.setAlign(element.attr("align"));
        }
        if (element.hasAttr("border") && NumberUtils.isNumber(element.attr("border"))) {
            styleConfig.setBorder(Integer.parseInt(element.attr("border")));
        }
        if (element.hasAttr("border-bottom") && NumberUtils.isNumber(element.attr("border-bottom"))) {
            styleConfig.setBorderBottom(Integer.parseInt(element.attr("border-bottom")));
        }
        if (element.hasAttr("border-top") && NumberUtils.isNumber(element.attr("border-top"))) {
            styleConfig.setBorderTop(Integer.parseInt(element.attr("border-top")));
        }
        if (element.hasAttr("border-left") && NumberUtils.isNumber(element.attr("border-left"))) {
            styleConfig.setBorderLeft(Integer.parseInt(element.attr("border-left")));
        }
        if (element.hasAttr("border-right") && NumberUtils.isNumber(element.attr("border-right"))) {
            styleConfig.setBorderRight(Integer.parseInt(element.attr("border-right")));
        }
        return styleConfig;
    }

    private static PdfPTable generatePdfTableByTemplet(Element pdfTableTemplet, Object data) {
        Elements dataTitleElements = pdfTableTemplet.getElementsByAttributeValue("name", "dataTitle");
        Elements tbodyElements = pdfTableTemplet.getElementsByTag("tbody");
        Element tbodyElement = CollectionUtils.isNotEmpty(tbodyElements) ? tbodyElements.get(0) : null;
        Elements dataElements = tbodyElement != null ? tbodyElement.getElementsByTag("td") : null;
        int columnNum = 0;
        List<Float> columnWidths = null;
        if (CollectionUtils.isNotEmpty(dataTitleElements) && CollectionUtils.isNotEmpty(dataTitleElements.get(0).getElementsByTag("th"))) {
            Elements colThElements = dataTitleElements.get(0).getElementsByTag("th");
            if (CollectionUtils.isNotEmpty(colThElements)) {
                columnWidths = new ArrayList<>(colThElements.size());
                columnNum = countColumnNumAndWidths(data, columnNum, columnWidths, colThElements);
            }
        } else if (CollectionUtils.isNotEmpty(dataElements)) {
            columnWidths = new ArrayList<>(dataElements.size());
            columnNum = countColumnNumAndWidths(data, columnNum, columnWidths, dataElements);
        } else if (tbodyElement != null && tbodyElement.hasAttr("dataList") && data instanceof List && CollectionUtils.isNotEmpty((List) data)) {
            Object aData = ((List) data).get(0);
            if (aData instanceof Map) {
                columnNum = ((Map) aData).keySet().size();
            } else if (aData != null) {
                columnNum = aData.getClass().getFields().length;
            }
        }
        PdfPTable pdfPTable = null;
        if (columnNum > 0) {
            pdfPTable = new PdfPTable(columnNum);
            if (CollectionUtils.isNotEmpty(columnWidths)) {
                try {
                    pdfPTable.setWidths(ArrayUtils.toPrimitive(columnWidths.toArray(new Float[columnWidths.size()])));
                } catch (DocumentException e) {
                    LOGGER.error("PDFTable设置每列列宽时异常：", e);
                    throw new BusinessException("COMMON_ERROR_14");
                }
                pdfPTable.setWidthPercentage(100); // table占满整个PDF
            }
        }
        return pdfPTable;
    }

    /**
     * 根据thead中th来计算table的列数计每列宽度占比
     *
     * @param data          数据，如果有动态列时计算动态列是否展示
     * @param columnNum     table的列数
     * @param columnWidths  table每列的宽度占比数组
     * @param colThElements 每列的th或td元素
     * @return 计算后的列數
     */
    private static int countColumnNumAndWidths(Object data, int columnNum, List<Float> columnWidths, Elements colThElements) {
        List<Integer> autoColumnIndexs = new ArrayList<>(colThElements.size());
        float determinedColumnWidthSum = 0F;
        for (Element colThElement : colThElements) {
            float columnWidth = 0;
            if (colThElement.hasAttr("if")) {
                String ifExpression = colThElement.attr("if");
                boolean dynamicColIfVal = DataUtils.getFieldByExpression(data, ifExpression, Boolean.class);
                if (dynamicColIfVal) {
                    columnNum++;
                    columnWidth = addColWidth(colThElement, columnWidths, autoColumnIndexs);

                }
            } else {
                columnNum++;
                columnWidth = addColWidth(colThElement, columnWidths, autoColumnIndexs);
            }
            determinedColumnWidthSum += columnWidth;
        }
        if (CollectionUtils.isNotEmpty(autoColumnIndexs)) {
            float autoWidth = (1F - determinedColumnWidthSum) / autoColumnIndexs.size();
            for (Integer autoColumnIndex : autoColumnIndexs) {
                columnWidths.set(autoColumnIndex, autoWidth);
            }
        }
        return columnNum;
    }

    private static float addColWidth(Element col, List<Float> columnWidths, List<Integer> autoColumnIndexs) {
        float width = 0F;
        if (col.hasAttr("width")) {
            width = Float.parseFloat(col.attr("width"));
        }
        columnWidths.add(width);
        if (width == 0) {
            autoColumnIndexs.add(columnWidths.indexOf(width));
        }
        return width;
    }

    private static void setPageAndTableEvent(PdfWriter pdfWriter, Element pdfTableTemplet, PdfPTable table) {
        Elements subCountElements = pdfTableTemplet.getElementsByAttributeValue("tdata", "SubCount");
        Elements subSumElements = pdfTableTemplet.getElementsByAttributeValue("tdata", "SubSum");
        Elements pageNOElements = pdfTableTemplet.getElementsByAttributeValue("tdata", "PageNO");
        Elements pageCountElements = pdfTableTemplet.getElementsByAttributeValue("tdata", "PageCount");
        PdfEvent pdfEvent = new PdfEvent();
        if (CollectionUtils.isNotEmpty(subCountElements) || CollectionUtils.isNotEmpty(subSumElements)
                || CollectionUtils.isNotEmpty(pageNOElements) || CollectionUtils.isNotEmpty(pageCountElements)) {
            pdfWriter.setPageEvent(pdfEvent);
        }
        if ((CollectionUtils.isNotEmpty(subCountElements) || CollectionUtils.isNotEmpty(subSumElements)) && table != null) {
            table.setTableEvent(pdfEvent);
        }
    }

    private static BaseFont createSongLightBaseFont() {
        try {
            return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException | IOException e) {
            LOGGER.error("无法创建itext宋体字体！", e);
            throw new BusinessException("COMMON_ERROR_13");
        }
    }

    private static Element parseByTableTemplet(String tableTemplet) {
        String tableTempletPath;
        String tableId = tableTemplet; // 模板中table的ID默认与文件名相同
        if (StringUtils.isBlank(tableTemplet)) {
            tableId = DEFAULT_TABLE_TEMPLET;
            tableTempletPath = DEFAULT_TABLE_TEMPLET_PATH + DEFAULT_TABLE_TEMPLET + ".html";
        } else {
            tableTempletPath = DEFAULT_TABLE_TEMPLET_PATH + tableTemplet + ".html";
        }
        URL pdfTableTempletUrl = UrlUtils.getResourceURL(tableTempletPath);
        if (pdfTableTempletUrl != null) {
            File file = new File(pdfTableTempletUrl.getPath());
            try {
                org.jsoup.nodes.Document pdfTableTemplet = Jsoup.parse(file, Constant.DEF_ENC);
                return pdfTableTemplet.getElementById(tableId);
            } catch (IOException e) {
                LOGGER.error("根据模板解析HTML异常", e);
                throw new BusinessException("COMMON_ERROR_11");
            }
        } else {
            throw new BusinessException("COMMON_ERROR_10");
        }
    }

    private static class PdfEvent extends PdfPageEventHelper implements PdfPTableEvent {
        private PdfPTable table;

        // 总页数
        PdfTemplate totalPage;

        // 打开文档时，创建一个总页数的模版
        public void onOpenDocument(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            totalPage = cb.createTemplate(30, 16);
        }

        public void onEndPage(PdfWriter writer, Document document) {
            /*if (table != null) {
                LOGGER.info("第"+document.getPageNumber()+"页/共"+writer.getPageNumber()+"页");
				ArrayList<PdfPRow> pdfPRows = table.getRows();
				LOGGER.info("当前table页行数-->{},ID-->{},当前页高度-->{},header-->{},footer-->{}",pdfPRows.size(),table.getId(),table.getTotalHeight(),table.getHeaderRows(),table.getFooterRows());
				if (CollectionUtils.isNotEmpty(pdfPRows)) {
					BigDecimal currentPageHospitalPrice = null;
					BigDecimal currentPageActualPrice = null;
					PdfPCell hospitalPriceCell = null;
					for (PdfPRow pdfPRow : pdfPRows) {
						LOGGER.info("当前table页rowid-->{}",pdfPRow.getId());
						PdfPCell[] pdfPCells = pdfPRow.getCells();
						if (ArrayUtils.isNotEmpty(pdfPCells)) {
							PdfPCell subtotalCell = null;
							for (PdfPCell pdfPCell : pdfPCells) {
								*//*if (pdfPCell != null && pdfPCell.getPhrase() != null && "${page}".equals(pdfPCell.getPhrase().getContent())) {
                                    LOGGER.info(pdfPCell.getPhrase().getContent());
									//pdfPCell.setPhrase(new Phrase("第"+writer.getCurrentPageNumber()+"页/共"+writer.getPageNumber()+"页"));
									//pdfPCell.setPhrase(new Phrase("第"+writer.getCurrentPageNumber()+"页/共"+writer.getPageNumber()+"页"));
									pdfPCell.getPhrase().add(0,new Chunk("第"+writer.getCurrentPageNumber()+"页/共"+writer.getPageNumber()+"页"));
								}*//*
                                if (pdfPCell != null) {
									PdfObject hospitalPricePdfObject = pdfPCell.getAccessibleAttributes() == null ? null : pdfPCell.getAccessibleAttribute(new PdfName("hospitalPriceCell" + Objects.toString(pdfPCell.getId())));
									if (hospitalPricePdfObject != null) {
										if (currentPageHospitalPrice == null) {
											currentPageHospitalPrice = new BigDecimal(0);
										}
										currentPageHospitalPrice = currentPageHospitalPrice.add(new BigDecimal(hospitalPricePdfObject.toString()));
									}

									PdfObject actualPricePdfObject = pdfPCell.getAccessibleAttributes() == null ? null : pdfPCell.getAccessibleAttribute(new PdfName("actualPriceCell" + Objects.toString(pdfPCell.getId())));
									if (actualPricePdfObject != null) {
										if (currentPageActualPrice == null) {
											currentPageActualPrice = new BigDecimal(0);
										}
										currentPageActualPrice = currentPageActualPrice.add(new BigDecimal(actualPricePdfObject.toString()));
									}
									if(pdfPCell.getPhrase()!=null && "${hospitalPrice}".equals(pdfPCell.getPhrase().getContent())) {
										hospitalPriceCell = pdfPCell;
									}
									if(pdfPCell.getPhrase()!=null && "${actualPrice}".equals(pdfPCell.getPhrase().getContent())) {
										hospitalPriceCell = pdfPCell;
									}
									if(pdfPCell.getPhrase()!=null && "${subtotal}".equals(pdfPCell.getPhrase().getContent())) {
										subtotalCell = pdfPCell;
									}
								}
							}
							if(hospitalPriceCell!=null && currentPageHospitalPrice!=null) {
								hospitalPriceCell.setPhrase(new Phrase(currentPageHospitalPrice.toString()));
							}
							if(hospitalPriceCell!=null && currentPageHospitalPrice!=null) {
								hospitalPriceCell.setPhrase(new Phrase(currentPageHospitalPrice.toString()));
							}
						}
					}
					LOGGER.info("本页小计医院价格-->{}，实际价格-->{}", currentPageHospitalPrice, currentPageActualPrice);
				}
			}*/
            PdfPTable table = new PdfPTable(3);
            try {
                table.setTotalWidth(PageSize.A4.getWidth() - 100);
                table.setWidths(new int[]{24, 24, 3});
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(-10);
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);

                //table.addCell(new Paragraph("我是文字", font));// 可以直接使用addCell(str)，不过不能指定字体，中文无法显示
                //table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                //table.addCell(new Paragraph("第" + writer.getPageNumber() + "页/", font));
                // 总页数
                PdfPCell cell = new PdfPCell(Image.getInstance(totalPage));
                cell.setBorder(Rectangle.BOTTOM);
                table.addCell(cell);
                // 将页眉写到document中，位置可以指定，指定到下面就是页脚
                table.writeSelectedRows(0, -1, 50, PageSize.A4.getHeight() - 20, writer.getDirectContent());
            } catch (Exception de) {
                throw new ExceptionConverter(de);
            }
            LOGGER.info("PdfEvent-->onEndPage事件执行！当前页码：{}，总页码：{}", writer.getCurrentPageNumber(), writer.getPageNumber());
        }

        // 全部完成后，将总页数的pdf模版写到指定位置
        public void onCloseDocument(PdfWriter writer, Document document) {
            String text = "总" + (writer.getPageNumber() - 1) + "页";
            //ColumnText.showTextAligned(totalPage, Element.ALIGN_LEFT, new Paragraph(text,font), 2, 2, 0);
            LOGGER.info("PdfEvent-->onCloseDocument事件执行！总页码：{}", writer.getPageNumber());
        }

        @Override
        public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart, PdfContentByte[] canvases) {
            this.table = table;
            LOGGER.info("PdfPTableEvent.tableLayout事件触发，table信息：大小-->{}", table.size());
        }
    }

    private static class StyleConfig {
        private int fontSize;

        private int border;

        private int align;

        private int borderTop;

        private int borderLeft;

        private int borderRight;

        private int borderBottom;

        StyleConfig() {
            fontSize = 12;
            border = Rectangle.UNDEFINED;
            align = Rectangle.ALIGN_CENTER;
            borderTop = Rectangle.UNDEFINED;
            borderLeft = Rectangle.UNDEFINED;
            borderRight = Rectangle.UNDEFINED;
            borderBottom = Rectangle.UNDEFINED;
            putFontToCache();
        }

        public Font getFont() {
            return putFontToCache();
        }

        void setFontSize(int fontSize) {
            this.fontSize = fontSize;
            putFontToCache();
        }

        private Font putFontToCache() {
            return FONT_CACHE.putIfAbsent(fontSize, new Font(createSongLightBaseFont(), fontSize, Font.NORMAL));
        }

        int getBorder() {
            return border;
        }

        StyleConfig setBorder(int border) {
            this.border = border;
            return this;
        }

        int getAlign() {
            return align;
        }

        StyleConfig setAlign(String align) {
            if ("center".equalsIgnoreCase(align)) {
                this.align = Rectangle.ALIGN_CENTER;
            } else if ("left".equalsIgnoreCase(align)) {
                this.align = Rectangle.ALIGN_LEFT;
            } else if ("right".equalsIgnoreCase(align)) {
                this.align = Rectangle.ALIGN_RIGHT;
            } else {
                this.align = Rectangle.ALIGN_CENTER;
            }
            return this;
        }

        int getBorderTop() {
            return borderTop;
        }

        StyleConfig setBorderTop(int borderTop) {
            this.borderTop = borderTop;
            return this;
        }

        int getBorderLeft() {
            return borderLeft;
        }

        StyleConfig setBorderLeft(int borderLeft) {
            this.borderLeft = borderLeft;
            return this;
        }

        int getBorderRight() {
            return borderRight;
        }

        StyleConfig setBorderRight(int borderRight) {
            this.borderRight = borderRight;
            return this;
        }

        int getBorderBottom() {
            return borderBottom;
        }

        StyleConfig setBorderBottom(int borderBottom) {
            this.borderBottom = borderBottom;
            return this;
        }
    }

    public static void main(String[] args) throws IOException, DocumentException {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("receiveDate", new Date());
            data.put("barCode", DataUtils.getRandomNumbers(10));
            data.put("patient", "小明");
            data.put("itemName", "检测项目" + i);
            data.put("clinicalDepartment", "科室" + i);
            data.put("doctor", "医生" + i);
            data.put("hospitalPrice", Math.random());
            dataList.add(data);
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("rows", 8);
        dataMap.put("clientName", "深圳市儿童医院");
        dataMap.put("showOutpatientNo", false);
        dataMap.put("minDate", new Date());
        dataMap.put("maxDate", new Date());
        dataMap.put("statementCode", DataUtils.getRandomNumbers(16));
        dataMap.put("salesman", "林珈驹");
        dataMap.put("region", "C区");
        dataMap.put("showActualPrice", true);
        dataMap.put("showHospitalCode", false);
        dataMap.put("hospitalPrice", new BigDecimal(Math.random() * 1000));
        dataMap.put("actualPrice", new BigDecimal(Math.random() * 1000));
        dataMap.put("settlingPerson", "张珊");
        dataMap.put("dataList", dataList);
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(new FileOutputStream("F:\\tmp\\itext\\faPrint.pdf")));
        document.open();
        long startTime = System.currentTimeMillis();
        generatePdfTable("faPrint", dataMap, document, pdfWriter);
        LOGGER.info("生成PDF表格消耗时间：{}秒", (System.currentTimeMillis() - startTime) / 1000);
        document.close();

    }
}
