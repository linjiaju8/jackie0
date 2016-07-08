package com.jackie0.common.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 数据字典实体
 *
 * @author jackie0
 * @since Java8
 * date 2016-07-07 11:36
 */
public class DataDict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3848515126141043090L;

    /**
     * 主键
     */
    private String dataDictId;

    /**
     * 数据字典分组编码，如：{"1":"是"}、{"0":"否"}两个字典可分组为"yesOrNo"
     */
    private String groupCode;

    /**
     * 父分组，用于做级联，默认为{@link com.jackie0.common.constant.Constant#DEFAULT_PARENT_CODE}
     */
    private String parentGroupCode;

    /**
     * 字典键，如：sex：male:男,female:女 male就是dictKey，男就是dictValue
     */
    private String dictKey;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 字典顺序，决定了页面展示顺序
     */
    private Integer dictOrder;

    @Id
    @Column(name = "DATA_DICT_ID", unique = true, length = 36, nullable = false)
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    public String getDataDictId() {
        return dataDictId;
    }

    public void setDataDictId(String dataDictId) {
        this.dataDictId = dataDictId;
    }

    @Basic
    @Column(name = "GROUP_CODE", length = 36, nullable = false)
    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    @Basic
    @Column(name = "PARENT_GROUP_CODE", length = 36, nullable = false)
    public String getParentGroupCode() {
        return parentGroupCode;
    }

    public void setParentGroupCode(String parentGroupCode) {
        this.parentGroupCode = parentGroupCode;
    }

    @Basic
    @Column(name = "DICT_KEY", length = 36, nullable = false)
    public String getDictKey() {
        return dictKey;
    }

    public void setDictKey(String dictKey) {
        this.dictKey = dictKey;
    }

    @Basic
    @Column(name = "DICT_VALUE", length = 512, nullable = false)
    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    @Basic
    @Column(name = "DESCRIPTION", length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "DICT_ORDER", length = 8, nullable = false)
    public Integer getDictOrder() {
        return dictOrder;
    }

    public void setDictOrder(Integer dictOrder) {
        this.dictOrder = dictOrder;
    }
}
