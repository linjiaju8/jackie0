package com.jackie0.common.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jackie0.common.utils.CustomDateDeserialize;
import com.jackie0.common.utils.CustomDateSerializer;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 操作日志实体类
 * ClassName:OperationLog <br/>
 * Date:     2015年08月11日 16:21 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@Entity
@Table(name = "operation_log", schema = "jackie0")
public class OperationLog extends BaseEntity implements BaseOperationLog, Serializable {

    private static final long serialVersionUID = 5624905180094101790L;
    /**
     * 主键
     */
    private String operationLogId;

    /**
     * 操作人:userName非ID
     */
    private String operationUser;

    /**
     * 操作时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserialize.class)
    private Timestamp operationTime;

    /**
     * 操作类型，新增、修改、删除、查询
     */
    private String operationType;

    /**
     * 操作名称
     */
    private String operationName;

    /**
     * 描述
     */
    private String description;

    /**
     * 老数据Json串，如修改前后删除前数据
     */
    private String oldJsonData;

    /**
     * 老数据Class类型
     */
    private String oldDataClass;

    /**
     * 在足迹页面跳转url地址时老数据的参数名称
     */
    private String oldDataParamName;

    /**
     * 新数据Json串，如修改后数据新增后数据
     */
    private String newJsonData;

    /**
     * 新数据Class类型
     */
    private String newDataClass;

    /**
     * 在足迹页面跳转url地址时新数据的参数名称
     */
    private String newDataParamName;

    /**
     * 在查看日志中可点击的链接
     */
    private String url;

    // 客户足迹搜索框查询条件
    private String operationLogQueryParam;

    @Transient
    public String getOperationLogQueryParam() {
        return operationLogQueryParam;
    }

    public void setOperationLogQueryParam(String operationLogQueryParam) {
        this.operationLogQueryParam = operationLogQueryParam;
    }

    @Id
    @Column(name = "OPERATION_LOG_ID", unique = true, length = 36, nullable = false)
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    public String getOperationLogId() {
        return operationLogId;
    }

    public void setOperationLogId(String operationLogId) {
        this.operationLogId = operationLogId;
    }

    @Basic
    @Column(name = "OPERATION_USER", length = 36)
    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    @Basic
    @Column(name = "OPERATION_TIME", nullable = false)
    public Timestamp getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Timestamp operationTime) {
        this.operationTime = operationTime;
    }

    @Basic
    @Column(name = "OPERATION_TYPE", length = 16, nullable = false)
    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Basic
    @Column(name = "OPERATION_NAME", length = 256, nullable = false)
    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Basic
    @Column(name = "DESCRIPTION", length = 2048)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Lob
    @Basic
    @Column(name = "OLD_JSON_DATA")
    public String getOldJsonData() {
        return oldJsonData;
    }

    public void setOldJsonData(String oldJsonData) {
        this.oldJsonData = oldJsonData;
    }

    @Lob
    @Basic
    @Column(name = "NEW_JSON_DATA")
    public String getNewJsonData() {
        return newJsonData;
    }

    public void setNewJsonData(String newJsonData) {
        this.newJsonData = newJsonData;
    }

    @Basic
    @Column(name = "URL", length = 2048)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "OLD_DATA_CLASS", length = 1024)
    public String getOldDataClass() {
        return oldDataClass;
    }

    public void setOldDataClass(String oldDataClass) {
        this.oldDataClass = oldDataClass;
    }

    @Basic
    @Column(name = "OLD_DATA_PARAM_NAME", length = 512)
    public String getOldDataParamName() {
        return oldDataParamName;
    }

    public void setOldDataParamName(String oldDataParamName) {
        this.oldDataParamName = oldDataParamName;
    }

    @Basic
    @Column(name = "NEW_DATA_CLASS", length = 1024)
    public String getNewDataClass() {
        return newDataClass;
    }

    public void setNewDataClass(String newDataClass) {
        this.newDataClass = newDataClass;
    }

    @Basic
    @Column(name = "NEW_DATA_PARAM_NAME", length = 512)
    public String getNewDataParamName() {
        return newDataParamName;
    }

    public void setNewDataParamName(String newDataParamName) {
        this.newDataParamName = newDataParamName;
    }
}