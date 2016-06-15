package com.jackie0.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ClassName:CalendarUtils <br/>
 * Date:     2015年09月08日 14:43 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
public class CalendarUtils {
    private static final String DEF_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取相对于当前周偏移量的指定星期当天日期
     * 如果想按照国人习惯返回星期天且作为一周的结束可以将amount+1来获取
     *
     * @param amount 相对当前周，周的偏移量，如：1下周，-1上周
     * @param weekField 返回指定星期几的日期
     * @param format 日期的格式，默认：yyyy-MM-dd HH:mm:ss
     * @return 偏移周的星期一当天日期
     */
    public static String geDateOffsetCurrentWeek(int amount, int weekField, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(StringUtils.isBlank(format) ? DEF_FORMAT : format);
        return simpleDateFormat.format(getDateOffsetCurrentWeek(amount, weekField));
    }


    /**
     * 获取相对于当前周偏移量的指定星期当天日期
     * 如果想按照国人习惯返回星期天且作为一周的结束可以将amount+1来获取
     *
     * @param amount    相对当前周，周的偏移量，如：1下周，-1上周
     * @param weekField 返回指定星期几的日期
     * @return 偏移周的星期天当天日期
     */
    public static Date getDateOffsetCurrentWeek(int amount, int weekField) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.WEEK_OF_MONTH, amount);
        calendar.set(Calendar.DAY_OF_WEEK, weekField);
        return calendar.getTime();
    }

    /**
     * 在原来的时间基础上做加减运算，得到的结果返一个新的日期
     *
     * @param date   原有时间，该函数不会改变原有对象
     * @param field  the calendar field. {@link java.util.Calendar#DATE}
     * @param amount the amount of date or time to be added to the field.
     * @param clazz  要返回的时间类型，必须是{@link java.util.Date}的子类
     * @return 新的日期对象
     */
    public static <T extends Date> T add(Date date, int field, int amount, Class<T> clazz) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(field, amount);
        Date resDate;
        // 返回精确的时间类型，避免调用该函数后出现instanceof不准确
        if (clazz == Timestamp.class) {
            resDate = new Timestamp(calendar.getTime().getTime());
        } else if (clazz == Time.class) {
            resDate = new Time(calendar.getTime().getTime());
        } else if (clazz == java.sql.Date.class) {
            resDate = new java.sql.Date(calendar.getTime().getTime());
        } else {
            resDate = calendar.getTime();
        }
        return clazz.cast(resDate);
    }
}
