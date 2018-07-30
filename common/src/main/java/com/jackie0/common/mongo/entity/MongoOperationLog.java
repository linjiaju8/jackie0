package com.jackie0.common.mongo.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jackie0.common.entity.BaseOperationLog;
import com.jackie0.common.entity.PageRequestInfo;
import com.jackie0.common.utils.CustomDateDeserialize;
import com.jackie0.common.utils.CustomDateSerializer;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 客户操作日志实体类
 * ClassName:CustLog
 * Date:     2015年08月11日 16:21
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@Document
public class MongoOperationLog extends PageRequestInfo implements BaseOperationLog {

    /**
     * 主键
     */
    private String id;

    /**
     * 操作人:userName非ID
     */
    private String operationUser;

    /**
     * 操作时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserialize.class)
    private Date operationTime;

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
    @GenericGenerator(name = "idGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "idGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOldJsonData() {
        return oldJsonData;
    }

    public void setOldJsonData(String oldJsonData) {
        this.oldJsonData = oldJsonData;
    }

    public String getNewJsonData() {
        return newJsonData;
    }

    public void setNewJsonData(String newJsonData) {
        this.newJsonData = newJsonData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOldDataClass() {
        return oldDataClass;
    }

    public void setOldDataClass(String oldDataClass) {
        this.oldDataClass = oldDataClass;
    }

    public String getOldDataParamName() {
        return oldDataParamName;
    }

    public void setOldDataParamName(String oldDataParamName) {
        this.oldDataParamName = oldDataParamName;
    }

    public String getNewDataClass() {
        return newDataClass;
    }

    public void setNewDataClass(String newDataClass) {
        this.newDataClass = newDataClass;
    }

    public String getNewDataParamName() {
        return newDataParamName;
    }

    public void setNewDataParamName(String newDataParamName) {
        this.newDataParamName = newDataParamName;
    }
}