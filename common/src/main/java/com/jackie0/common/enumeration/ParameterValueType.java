package com.jackie0.common.enumeration;

/**
 * 系统参数值类型枚举
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-13 15:52
 */
public enum ParameterValueType {
    INT("整数", "int"),
    CHAR("字符", "char"),
    STRING("字符串", "string"),
    FLOAT("浮点", "float"),
    BOOLEAN("布尔值", "boolean");

    private String name;
    private String value;

    ParameterValueType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static String getValueByName(String name) {
        String value = null;
        for (ParameterValueType parameterValueType : ParameterValueType.values()) {
            if (parameterValueType.name.equals(name)) {
                value = parameterValueType.value;
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
