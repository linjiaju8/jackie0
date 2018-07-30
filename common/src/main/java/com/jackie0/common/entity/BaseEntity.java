package com.jackie0.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 实体或VO中可复用部分
 * ClassName:BaseEntity
 * Date:     2015年11月25日 10:34
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@MappedSuperclass
public class BaseEntity extends PageRequestInfo implements Serializable {
    private static final long serialVersionUID = -8589389245694477621L;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Timestamp creationDate;

    /**
     * 最后修改人
     */
    private String lastUpdatedBy;

    /**
     * 最后修改时间
     */
    private Timestamp lastUpdateDate;

    /**
     * 版本号
     */
    private BigDecimal recordVersion;

    /**
     * 删除状态，参考枚举{@link com.jackie0.common.enumeration.DeleteTag}
     */
    private String deletedFlag;

    /**
     * 删除人
     */
    private String deletedBy;

    /**
     * 删除时间
     */
    private Timestamp deletedDate;

    /**
     * 数据归档时间，方便dba根据此时间做数据归档
     */
    private Timestamp archiveBaseDate;

    /**
     * 业务组织编码，具体看自身系统根据什么规则做业务数据切分
     * 如：子公司、地市或者自有标识等
     */
    private String bizOrgCode;

    @Basic
    @Column(name = "created_by", length = 36, nullable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "creation_date", nullable = false)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "last_updated_by", length = 36, nullable = false)
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Basic
    @Column(name = "last_update_date", nullable = false)
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Basic
    @Column(name = "record_version", length = 8, nullable = false)
    public BigDecimal getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(BigDecimal recordVersion) {
        this.recordVersion = recordVersion;
    }

    @Basic
    @Column(name = "deleted_flag", length = 2, nullable = false)
    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    @Basic
    @Column(name = "deleted_by", length = 36)
    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Basic
    @Column(name = "deleted_date")
    public Timestamp getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Timestamp deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Basic
    @Column(name = "archive_base_date", nullable = false)
    public Timestamp getArchiveBaseDate() {
        return archiveBaseDate;
    }

    public void setArchiveBaseDate(Timestamp archiveBaseDate) {
        this.archiveBaseDate = archiveBaseDate;
    }

    @Basic
    @Column(name = "biz_org_code", nullable = false, length = 36)
    public String getBizOrgCode() {
        return bizOrgCode;
    }

    public void setBizOrgCode(String bizOrgCode) {
        this.bizOrgCode = bizOrgCode;
    }
}
