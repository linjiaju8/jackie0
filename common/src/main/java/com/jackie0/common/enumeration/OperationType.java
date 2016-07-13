package com.jackie0.common.enumeration;

import com.jackie0.common.utils.I18nUtils;

/**
 * 操作类型枚举
 * ClassName:OperationType <br/>
 * Date:     2015年08月03日 14:32 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public enum OperationType {
    LOGIN(I18nUtils.getMessage("jackie0.common.OperationType.login"), "login"),
    CREATE(I18nUtils.getMessage("jackie0.common.OperationType.create"), "create"),
    DELETE(I18nUtils.getMessage("jackie0.common.OperationType.delete"), "delete"),
    UPDATE(I18nUtils.getMessage("jackie0.common.OperationType.update"), "update"),
    QUERY(I18nUtils.getMessage("jackie0.common.OperationType.query"), "query");

    private String name;
    private String value;


    OperationType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValueByName(String name) {
        String value = null;
        for (OperationType operationType : OperationType.values()) {
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
