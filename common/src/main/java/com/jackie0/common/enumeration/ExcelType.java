/**
 * Copyright (C),Kingmed
 *
 * @FileName: ExcelType.java
 * @Package: com.kingmed.ws.enumeration
 * @Description:Excel类型枚举
 * @Author linjiaju
 * @Date 2015年11月30日 16:45
 * @History: //修改记录
 * 〈author〉      〈time〉      〈version〉       〈desc〉
 * 修改人姓名            修改时间            版本号              描述
 */
package com.jackie0.common.enumeration;

/**
 * Excel类型枚举
 * ClassName:ExcelType <br/>
 * Date:     2015年08月03日 14:32 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public enum ExcelType {
    XLS("xls", 1),
    XLSX("xlsx", 2);

    private String name;
    private int value;


    ExcelType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static int getValueByName(String name) {
        int value = 0;
        for (ExcelType excelType : ExcelType.values()) {
            if (excelType.name.equals(name)) {
                value = excelType.value;
                break;
            }
        }
        return value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
