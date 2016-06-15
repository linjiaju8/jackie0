/**
 * Copyright (C),Kingmed
 *
 * @FileName: Excel.java
 * @Package: com.kingmed.ws.util
 * @Description: //模块目的、功能描述
 * @Author linjiaju
 * @Date 2015年12月09日 16:26
 * @History: //修改记录
 * 〈author〉      〈time〉      〈version〉       〈desc〉
 * 修改人姓名            修改时间            版本号              描述
 */
package com.jackie0.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

/**
 * Excel的一些常量及样式信息
 * ClassName:Excel <br/>
 * Date:     2015年12月09日 16:26 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
public class Excel {
    // 默认列样式
    private static final ColumnStyle DEF_COLUMN_STYLE = new ColumnStyle();

    // 默认title样式
    private static final TitleStyle DEF_TITLE_STYLE = new TitleStyle();

    // 默认的excel单元格列宽，单位1/256个字符，这个值是根据现有的KOS系统得来的，需求要求与老系统一样
    private static final int DEF_CELL_WIDTH = 2304;

    public static ColumnStyle getDefColumnStyle() {
        initColumnStyle(DEF_COLUMN_STYLE);
        return DEF_COLUMN_STYLE;
    }

    public static TitleStyle getDefTitleStyle() {
        initColumnStyle(DEF_TITLE_STYLE);
        return DEF_TITLE_STYLE;
    }

    private static void initColumnStyle(ColumnStyle columnStyle) {
        columnStyle.setAlignment(CellStyle.ALIGN_CENTER);
        columnStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        columnStyle.setWrapText(false);
        columnStyle.setBorderRight(CellStyle.BORDER_THIN);
        columnStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        columnStyle.setBorderLeft(CellStyle.BORDER_THIN);
        columnStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        columnStyle.setBorderTop(CellStyle.BORDER_THIN);
        columnStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        columnStyle.setBorderBottom(CellStyle.BORDER_THIN);
        columnStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        columnStyle.setColor(IndexedColors.BLACK.getIndex());
        columnStyle.setFontHeightInPoints((short) 11);
        columnStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        columnStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        columnStyle.setColumnWidth(DEF_CELL_WIDTH);
        if (columnStyle instanceof TitleStyle) {
            columnStyle.setColor(IndexedColors.BLACK.getIndex());
            columnStyle.setFontHeightInPoints((short) 12);
            columnStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            columnStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
    }


    /**
     * 根据字段属性名查找该属性对应列的样式，是POI组件中的样式{@link CellStyle}
     *
     * @param columnsRow   列对应的行对象
     * @param columnStyles 列样式列表
     * @param filed        属性名称
     * @return 属性对应的列的样式
     */
    public static CellStyle findCellStyle(Row columnsRow, List<ColumnStyle> columnStyles, String filed) {
        CellStyle cellStyle = columnsRow.getSheet().getWorkbook().createCellStyle();
        Font cellFont = columnsRow.getSheet().getWorkbook().createFont();
        setCellStyle(cellStyle, findColumnStyle(columnStyles, filed), cellFont);
        return cellStyle;
    }

    /**
     * 根据字段属性名查找该属性对应列的样式，是WEB自助中自定的样式{@link ColumnStyle}
     *
     * @param columnStyles 列样式列表
     * @param filed        属性名称
     * @return 属性对应的列的样式
     */
    public static ColumnStyle findColumnStyle(List<ColumnStyle> columnStyles, String filed) {
        ColumnStyle retColumnStyle = null;
        if (CollectionUtils.isNotEmpty(columnStyles) && StringUtils.isNotBlank(filed)) {
            for (ColumnStyle columnStyle : columnStyles) {
                if (filed.equals(columnStyle.getColumnField())) {
                    retColumnStyle = columnStyle;
                    break;
                }
            }
        }
        if (retColumnStyle == null) {
            retColumnStyle = getDefColumnStyle();
        }
        return retColumnStyle;
    }

    private static void setCellStyle(CellStyle cellStyle, ColumnStyle columnStyle, Font cellFont) {
        cellStyle.setAlignment(columnStyle.getAlignment());
        cellStyle.setVerticalAlignment(columnStyle.getVerticalAlignment());
        cellStyle.setWrapText(columnStyle.isWrapText());
        cellStyle.setBorderRight(columnStyle.getBorderRight());
        cellStyle.setRightBorderColor(columnStyle.getRightBorderColor());
        cellStyle.setBorderLeft(columnStyle.getBorderLeft());
        cellStyle.setLeftBorderColor(columnStyle.getLeftBorderColor());
        cellStyle.setBorderTop(columnStyle.getBorderTop());
        cellStyle.setTopBorderColor(columnStyle.getTopBorderColor());
        cellStyle.setBorderBottom(columnStyle.getBorderBottom());
        cellStyle.setBottomBorderColor(columnStyle.getBottomBorderColor());
        cellFont.setColor(columnStyle.getColor());
        cellFont.setFontHeightInPoints(columnStyle.getFontHeightInPoints());
        cellStyle.setFillForegroundColor(columnStyle.getFillForegroundColor());
        cellStyle.setFillPattern(columnStyle.getFillPattern());
        cellStyle.setFont(cellFont);
    }

    /**
     * 获取标题行样式
     *
     * @param titleRow   标题行对象
     * @param titleStyle 标题web自助自定义样式
     * @return 标题POI组件样式
     */
    public static CellStyle getTitleStyle(Row titleRow, TitleStyle titleStyle) {
        CellStyle cellStyle = titleRow.getSheet().getWorkbook().createCellStyle();
        Font cellFont = titleRow.getSheet().getWorkbook().createFont();
        TitleStyle finalTitleStyle = titleStyle;
        if (titleStyle == null) {
            finalTitleStyle = getDefTitleStyle();
        }
        setCellStyle(cellStyle, finalTitleStyle, cellFont);
        return cellStyle;
    }

    public static class ColumnStyle {
        private short alignment; // 水平位置
        private short verticalAlignment; // 垂直位置
        private boolean wrapText; // 自动换行
        private short borderRight; // 右边框
        private short rightBorderColor; // 右边框颜色
        private short borderLeft; // 左边框
        private short leftBorderColor; // 左边框颜色
        private short borderTop; // 上边框
        private short topBorderColor; // 上边框颜色
        private short borderBottom; // 底部边框
        private short bottomBorderColor; // 底部边框颜色

        private short color; // 字体颜色
        private short fontHeightInPoints; // 字体高度
        private short fillForegroundColor; // 填充背景色
        private short fillPattern; // 填充模式

        private String columnField; // 改列对应的列属性名称
        private int columnWidth; // 列宽

        public short getAlignment() {
            return alignment;
        }

        /**
         * @param alignment - the type of alignment
         * @see {@link CellStyle#setAlignment(short)}
         */
        public void setAlignment(short alignment) {
            this.alignment = alignment;
        }

        public short getVerticalAlignment() {
            return verticalAlignment;
        }

        /**
         * @param verticalAlignment the type of alignment
         * @see {@link CellStyle#setVerticalAlignment(short)}
         */
        public void setVerticalAlignment(short verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
        }

        public boolean isWrapText() {
            return wrapText;
        }

        /**
         * @param wrapText wrap text or not
         * @see {@link CellStyle#setWrapText(boolean)}
         */
        public void setWrapText(boolean wrapText) {
            this.wrapText = wrapText;
        }

        public short getBorderRight() {
            return borderRight;
        }

        /**
         * @param borderRight type
         * @see {@link CellStyle#setBorderRight(short)}
         */
        public void setBorderRight(short borderRight) {
            this.borderRight = borderRight;
        }

        public short getRightBorderColor() {
            return rightBorderColor;
        }

        /**
         * @param rightBorderColor The index of the color definition
         * @see {@link CellStyle#setRightBorderColor(short)}
         */
        public void setRightBorderColor(short rightBorderColor) {
            this.rightBorderColor = rightBorderColor;
        }

        public short getBorderLeft() {
            return borderLeft;
        }

        /**
         * @param borderLeft type
         * @see {@link CellStyle#setBorderLeft(short)}
         */
        public void setBorderLeft(short borderLeft) {
            this.borderLeft = borderLeft;
        }

        public short getLeftBorderColor() {
            return leftBorderColor;
        }

        /**
         * @param leftBorderColor The index of the color definition
         * @see {@link CellStyle#setLeftBorderColor(short)}
         */
        public void setLeftBorderColor(short leftBorderColor) {
            this.leftBorderColor = leftBorderColor;
        }

        public short getBorderTop() {
            return borderTop;
        }

        /**
         * @param borderTop type
         * @see {@link CellStyle#setBorderTop(short)}
         */
        public void setBorderTop(short borderTop) {
            this.borderTop = borderTop;
        }

        public short getTopBorderColor() {
            return topBorderColor;
        }

        /**
         * @param topBorderColor The index of the color definition
         * @see {@link CellStyle#setTopBorderColor(short)}
         */
        public void setTopBorderColor(short topBorderColor) {
            this.topBorderColor = topBorderColor;
        }

        public short getBorderBottom() {
            return borderBottom;
        }

        /**
         * @param borderBottom type
         * @see {@link CellStyle#setBorderBottom(short)}
         */
        public void setBorderBottom(short borderBottom) {
            this.borderBottom = borderBottom;
        }

        public short getBottomBorderColor() {
            return bottomBorderColor;
        }

        /**
         * @param bottomBorderColor The index of the color definition
         * @see {@link CellStyle#setBottomBorderColor(short)}
         */
        public void setBottomBorderColor(short bottomBorderColor) {
            this.bottomBorderColor = bottomBorderColor;
        }

        public short getColor() {
            return color;
        }

        /**
         * @param color to use
         * @see {@link Font#setColor(short)}
         */
        public void setColor(short color) {
            this.color = color;
        }

        public short getFontHeightInPoints() {
            return fontHeightInPoints;
        }

        /**
         * @param fontHeightInPoints height in the familiar unit of measure - points
         * @see {@link Font#setFontHeightInPoints(short)}
         */
        public void setFontHeightInPoints(short fontHeightInPoints) {
            this.fontHeightInPoints = fontHeightInPoints;
        }

        public short getFillForegroundColor() {
            return fillForegroundColor;
        }

        /**
         * @param fillForegroundColor color
         * @see {@link CellStyle#setFillForegroundColor(short)}
         */
        public void setFillForegroundColor(short fillForegroundColor) {
            this.fillForegroundColor = fillForegroundColor;
        }

        public short getFillPattern() {
            return fillPattern;
        }

        /**
         * @param fillPattern fill pattern (set to 1 to fill w/foreground color)
         * @see {@link CellStyle#setFillPattern(short)}
         */
        public void setFillPattern(short fillPattern) {
            this.fillPattern = fillPattern;
        }

        public String getColumnField() {
            return columnField;
        }

        public void setColumnField(String columnField) {
            this.columnField = columnField;
        }

        public int getColumnWidth() {
            return columnWidth;
        }

        public void setColumnWidth(int columnWidth) {
            this.columnWidth = columnWidth;
        }


        public ColumnStyle() {
            // 默认初始化样式
            initColumnStyle(this);
        }
    }

    public static class TitleStyle extends ColumnStyle {
        public TitleStyle() {
            super();
        }
    }
}
