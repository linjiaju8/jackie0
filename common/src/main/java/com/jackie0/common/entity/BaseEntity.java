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
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
@MappedSuperclass
public class BaseEntity extends Page implements Serializable {
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
    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Basic
    @Column(name = "CREATION_DATE")
    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @Basic
    @Column(name = "LAST_UPDATED_BY")
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    @Basic
    @Column(name = "LAST_UPDATE_DATE")
    public Timestamp getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Timestamp lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Basic
    @Column(name = "RECORD_VERSION")
    public BigDecimal getRecordVersion() {
        return recordVersion;
    }

    public void setRecordVersion(BigDecimal recordVersion) {
        this.recordVersion = recordVersion;
    }

    @Basic
    @Column(name = "DELETED_FLAG")
    public String getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    @Basic
    @Column(name = "DELETED_BY")
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
    @Column(name = "ARCHIVE_BASE_DATE")
    public Timestamp getArchiveBaseDate() {
        return archiveBaseDate;
    }

    public void setArchiveBaseDate(Timestamp archiveBaseDate) {
        this.archiveBaseDate = archiveBaseDate;
    }

    @Basic
    @Column(name = "BIZ_ORG_CODE")
    public String getBizOrgCode() {
        return bizOrgCode;
    }

    public void setBizOrgCode(String bizOrgCode) {
        this.bizOrgCode = bizOrgCode;
    }

    // 解决排序分页bug，如果有大量（大于每页显示的数据量至少2页的情况下）数据creationDate（排序字段）相同时会出现分页数据重复的bug，加上rowid就可以解决，且不会有性能损耗
    private String rowid;

    @Basic
    @Column(name = "ROWID", insertable = false, updatable = false)
    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }
}
