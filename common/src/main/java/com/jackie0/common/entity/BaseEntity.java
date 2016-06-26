package com.jackie0.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 实体或VO中可复用部分
 * ClassName:BaseEntity <br/>
 * Date:     2015年11月25日 10:34 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@MappedSuperclass
public class BaseEntity extends PageRequestInfo implements Serializable {
    private static final long serialVersionUID = -8589389245694477621L;
    private String createdBy;

    private Timestamp creationDate;
    private String lastUpdatedBy;

    private Timestamp lastUpdateDate;
    private BigDecimal recordVersion;
    private String deletedFlag;
    private String deletedBy;

    private Timestamp deletedDate;
    private Timestamp archiveBaseDate;
    private String bizOrgCode;

    @Basic
    @Column(name = "CREATED_BY", length = 36, nullable = false)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CREATION_DATE", nullable = false)
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "LAST_UPDATED_BY", length = 36, nullable = false)
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Basic
    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Basic
    @Column(name = "RECORD_VERSION", length = 8, nullable = false)
    public BigDecimal getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(BigDecimal recordVersion) {
        this.recordVersion = recordVersion;
    }

    @Basic
    @Column(name = "DELETED_FLAG", length = 2, nullable = false)
    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    @Basic
    @Column(name = "DELETED_BY", length = 36)
    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    @Basic
    @Column(name = "DELETED_DATE")
    public Timestamp getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Timestamp deletedDate) {
        this.deletedDate = deletedDate;
    }

    @Basic
    @Column(name = "ARCHIVE_BASE_DATE", nullable = false)
    public Timestamp getArchiveBaseDate() {
        return archiveBaseDate;
    }

    public void setArchiveBaseDate(Timestamp archiveBaseDate) {
        this.archiveBaseDate = archiveBaseDate;
    }

    @Basic
    @Column(name = "BIZ_ORG_CODE", nullable = false, length = 36)
    public String getBizOrgCode() {
        return bizOrgCode;
    }

    public void setBizOrgCode(String bizOrgCode) {
        this.bizOrgCode = bizOrgCode;
    }
}
