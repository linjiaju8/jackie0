package com.jackie0.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * 本地时间JSON序列化处理类
 * ClassName:CustomDateSerializer <br/>
 * Date:     2015年10月16日 14:13 <br/>
 *
 * @author linjiaju
 * @since JDK 1.8
 */
public class CustomDateSerializer extends JsonSerializer<Date> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomDateSerializer.class);

    @Override
    public Class<Date> handledType() {
        return Date.class;
    }

    /**
     * 日期参数序列化工具类
     *
     * @param value    要序列化的日期
     * @param jgen     {@link JsonGenerator}
     * @param provider 　{@link SerializerProvider}
     * @throws IOException
     */
    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        String formattedDate = "";
        if (value != null) {
            formattedDate = value.toString();
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
                    formattedDate = DateFormatUtils.format(value, dateFormat);
                    if (StringUtils.isNotBlank(formattedDate)) {
                        break; // 有匹配的格式转换成功则跳出匹配，返回结果
                    }
                } catch (Exception e) {
                    formattedDate = value.toString();
                    LOGGER.debug("日期{}按照格式{}转换日期异常-->{},跳到下一个格式！", value, dateFormat, e.getMessage());
                    if (index == dateFormats.length - 1) {
                        LOGGER.info("所有格式【{}】都无法转换当前日期{}默认返回日期的toString()结果！", ArrayUtils.toString(dateFormats), value);
                    }
                }
                index++;
            }
        }
        jgen.writeString(formattedDate);
    }
}
