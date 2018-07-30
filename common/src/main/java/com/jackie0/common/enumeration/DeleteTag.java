package com.jackie0.common.enumeration;

import com.jackie0.common.utils.I18nUtils;

/**
 * 系统删除标识
 * ClassName:DeleteTag
 * Date:     2015年08月03日 14:32
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public enum DeleteTag {
    IS_DELETED(I18nUtils.getMessage("jackie0.common.DeleteTag.alreadyDeleted"), "1"),
    IS_NOT_DELETED(I18nUtils.getMessage("jackie0.common.DeleteTag.notDelete"), "0");

    private final String name;
    private final String value;


    DeleteTag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValueByName(String name) {
        String value = null;
        for (DeleteTag operationType : DeleteTag.values()) {
            if (operationType.name.equals(name)) {
                value = operationType.value;
                break;
            }
        }
        return value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
