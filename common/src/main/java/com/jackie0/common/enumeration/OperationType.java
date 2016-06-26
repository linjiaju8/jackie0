package com.jackie0.common.enumeration;

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
    LOGIN("登录", "login"),
    CREATE("新增", "create"),
    DELETE("删除", "delete"),
    UPDATE("更新", "update"),
    QUERY("查询", "query");

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
