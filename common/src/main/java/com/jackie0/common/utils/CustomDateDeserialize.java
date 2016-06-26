package com.jackie0.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 本地时间JSON反序列化处理类
 * ClassName:CustomDateDeserialize <br/>
 * Date:     2015年10月16日 14:13 <br/>
 *
 * @author jackie0
 * @since JDK 1.8
 */
public class CustomDateDeserialize extends JsonDeserializer<Date> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDateDeserialize.class);

    /**
     * 日期参数反序列化工具类
     *
     * @param jp   {@link JsonParser}
     * @param ctxt {@link DeserializationContext}
     * @return 反序列化后的日期
     * @throws IOException
     */
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        Date formattedDate = null;
        if (StringUtils.isNotBlank(jp.getText())) {
            String formattedDateText = jp.getText();
            String[] dateFormats = new String[]{
                    I18nUtils.getMessage("ws.timestamp.format"),
                    I18nUtils.getMessage("ws.date.format"),
                    I18nUtils.getMessage("ws.month.format"),
                    I18nUtils.getMessage("ws.yyyyMMdd.HHmmss.format"),
                    I18nUtils.getMessage("ws.yyyyMMdd.format"),
                    I18nUtils.getMessage("ws.yyyyMM.format")

            }; // 精度由高到低依次匹配
            int index = 0;
            for (String dateFormat : dateFormats) {
                try {
                    formattedDate = DateUtils.parseDate(formattedDateText, dateFormat);
                    if (formattedDate != null) {
                        if (dateFormat.equals(I18nUtils.getMessage("ws.timestamp.format")) || dateFormat.equals(I18nUtils.getMessage("ws.yyyyMMdd.HHmmss.format"))) {
                            // 约定精确到秒及以上经度的时间格式统一用Timestamp映射，经度不够用Date映射
                            formattedDate = new Timestamp(formattedDate.getTime());
                        }
                        break; // 有匹配的格式转换成功则跳出匹配，返回结果
                    }
                } catch (Exception e) {
                    formattedDate = null;
                    LOGGER.debug("日期{}按照格式{}转换日期异常-->{},跳到下一个格式！", formattedDateText, dateFormat, e.getMessage());
                    if (index == dateFormats.length - 1) {
                        LOGGER.info("所有格式【{}】都无法转换当前日期{}默认返回日期的toString()结果！", ArrayUtils.toString(dateFormats), formattedDateText);
                    }
                }
                index++;
            }
        }
        return formattedDate;
    }

    @Override
    public Class<Date> handledType() {
        return Date.class;
    }

}
