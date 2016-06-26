package com.jackie0.common.enumeration;

/**
 * 系统删除标识
 * ClassName:DeleteTag <br/>
 * Date:     2015年08月03日 14:32 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public enum DeleteTag {
    IS_DELETED("已删除", "1"),
    IS_NOT_DELETED("未删除", "0");

    private String name;
    private String value;


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
