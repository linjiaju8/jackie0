package com.jackie0.common.entity;

import com.jackie0.common.constant.Constant;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 数据字典实体，与系统参数{@link SystemParameter}的区别是，数据字典通常与业务挂钩，并且很少变更的枚举数据，如：性别
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-07 11:36
 */
@Entity
@Table(name = "data_dict", schema = Constant.COMMON_SCHEMA)
public class DataDict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3848515126141043090L;

    /**
     * 数据字典缓存前缀
     */
    public static final String DATA_DICT_CACHE_KEY_PREFIX = Constant.DATA_CACHE_PREFIX + ".DataDict.";

    /**
     * 主键
     */
    private String dataDictId;

    /**
     * 数据字典分组编码，如：{"1":"是"}、{"0":"否"}两个字典可分组为"yesOrNo"
     */
    @NotBlank(message = "{jackie0.common.dataDict.groupCode.NotBlank}")
    @Length(min = 1, max = 36, message = "{jackie0.common.dataDict.groupCode.length}")
    private String groupCode;

    /**
     * 父分组，用于做级联，默认为{@link com.jackie0.common.constant.Constant#DEFAULT_PARENT_CODE}
     */
    private String parentGroupCode;

    /**
     * 字典键，如：sex：male:男,female:女 male就是dictKey，男就是dictValue
     */
    @NotBlank(message = "{jackie0.common.dataDict.dictKey.NotBlank}")
    @Length(min = 1, max = 36, message = "{jackie0.common.dataDict.dictKey.length}")
    private String dictKey;

    /**
     * 字典值
     */
    @NotBlank(message = "{jackie0.common.dataDict.dictValue.NotBlank}")
    private String dictValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 字典顺序，决定了页面展示顺序
     */
    @NotNull(message = "{jackie0.common.dataDict.dictOrder.NotNull}")
    @Digits(integer = 8, fraction = 0, message = "{jackie0.common.dataDict.dictOrder.digits}")
    private Integer dictOrder;

    @Id
    @Column(name = "data_dict_id", unique = true, length = 36, nullable = false)
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    public String getDataDictId() {
        return dataDictId;
    }

    public void setDataDictId(String dataDictId) {
        this.dataDictId = dataDictId;
    }

    @Basic
    @Column(name = "group_code", length = 36, nullable = false)
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Basic
    @Column(name = "parent_group_code", length = 36, nullable = false)
    public String getParentGroupCode() {
        return parentGroupCode;
    }

    public void setParentGroupCode(String parentGroupCode) {
        this.parentGroupCode = parentGroupCode;
    }

    @Basic
    @Column(name = "dict_key", length = 36, nullable = false)
    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    @Basic
    @Column(name = "dict_value", length = 512, nullable = false)
    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @Basic
    @Column(name = "description", length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "dict_order", length = 8, nullable = false)
    public Integer getDictOrder() {
        return dictOrder;
    }

    public void setDictOrder(Integer dictOrder) {
        this.dictOrder = dictOrder;
    }
}
