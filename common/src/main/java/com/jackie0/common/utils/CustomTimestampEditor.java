package com.jackie0.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * 本地Timestamp类型转换处理类
 * ClassName:CustomDateSerializer <br/>
 * Date:     2015年10月16日 14:13 <br/>
 *
 * @author linjiaju
 * @since JDK 1.8
 */
public class CustomTimestampEditor extends PropertyEditorSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomTimestampEditor.class);
    private final DateFormat[] dateFormats; // 可配置多个DateFormat从第一个开始转换，没异常正常返回，有异常继续下一个。

    private final boolean allowEmpty;

    private final int exactDateLength;

    private static final String[] NULL_DATA = {"null", "undefined", "nan"};


    /**
     * Create a new CustomDateEditor instance, using the given DateFormat
     * for parsing and rendering.
     * <p>The "allowEmpty" parameter states if an empty String should
     * be allowed for parsing, i.e. get interpreted as null value.
     * Otherwise, an IllegalArgumentException gets thrown in that case.
     *
     * @param dateFormats DateFormat[] to use for parsing and rendering
     * @param allowEmpty  if empty strings should be allowed
     */
    public CustomTimestampEditor(DateFormat[] dateFormats, boolean allowEmpty) {
        this.dateFormats = dateFormats;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    /**
     * Create a new CustomDateEditor instance, using the given DateFormat
     * for parsing and rendering.
     * <p>The "allowEmpty" parameter states if an empty String should
     * be allowed for parsing, i.e. get interpreted as null value.
     * Otherwise, an IllegalArgumentException gets thrown in that case.
     * <p>The "exactDateLength" parameter states that IllegalArgumentException gets
     * thrown if the String does not exactly match the length specified. This is useful
     * because SimpleDateFormat does not enforce strict parsing of the year part,
     * not even with {@code setLenient(false)}. Without an "exactDateLength"
     * specified, the "01/01/05" would get parsed to "01/01/0005". However, even
     * with an "exactDateLength" specified, prepended zeros in the day or month
     * part may still allow for a shorter year part, so consider this as just
     * one more assertion that gets you closer to the intended date format.
     *
     * @param dateFormats     DateFormat[] to use for parsing and rendering
     * @param allowEmpty      if empty strings should be allowed
     * @param exactDateLength the exact expected length of the date String
     */
    public CustomTimestampEditor(DateFormat[] dateFormats, boolean allowEmpty, int exactDateLength) {
        this.dateFormats = dateFormats;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }


    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && (StringUtils.isBlank(text) || ArrayUtils.contains(NULL_DATA, text.toLowerCase()))) {
            // Treat empty String as null value.
            setValue(null);
        } else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        } else {
            int index = 0;
            for (DateFormat dateFormat : dateFormats) {
                try {
                    setValue(new Timestamp(dateFormat.parse(text).getTime()));
                    break; // 成功一个就退出
                } catch (ParseException ex) {
                    LOGGER.info("setAsText:dateFormat-->{}转换异常，时间参数-->{}，将使用下一个dateFormat转换", dateFormat.toString(), text);
                    if (index == dateFormats.length - 1) {
                        // 最后一个转换器都没成功，则把异常抛出
                        throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
                    }
                }
                index++;
            }
        }
    }

    /**
     * Format the Date as String, using the specified DateFormat.
     */
    @Override
    public String getAsText() {
        Timestamp value = (Timestamp) getValue();
        String dateText = "";
        int index = 0;
        for (DateFormat dateFormat : dateFormats) {
            try {
                dateText = dateFormat.format(value);
                break; // 成功一个就退出
            } catch (Exception ex) {
                LOGGER.info("getAsText:dateFormat-->{}转换异常，时间参数-->{}，将使用下一个dateFormat转换", dateFormat.toString(), value);
                if (index == dateFormats.length - 1) {
                    // 最后一个转换器都没成功，则把异常抛出
                    throw ex;
                }
            }
            index++;
        }
        return (value != null ? dateText : "");
    }
}
