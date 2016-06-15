/**
 * Copyright (C),Kingmed
 *
 * @FileName: EmptyEntity.java
 * @Package: com.kingmed.ws.common.entity
 * @Description: 空的实体，不对应任何表，在JPA使用SQL实现N表关联查询，同时满足项目及JAP规范时使用
 * @Author linjiaju
 * @Date 2015年11月30日 16:45
 * @History: //修改记录
 * 〈author〉      〈time〉      〈version〉       〈desc〉
 * 修改人姓名            修改时间            版本号              描述
 */
package com.jackie0.common.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 空的实体，不对应任何表，在JPA使用SQL实现N表关联查询，同时满足项目及JAP规范时使用
 * ClassName:EmptyEntity <br/>
 * Date:     2015年08月29日 16:27 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
@Entity
public class EmptyEntity implements Serializable {
    private static final long serialVersionUID = -5235709822256117187L;
    private String nullId;

    @Id
    public String getNullId() {
        return nullId;
    }

    public void setNullId(String nullId) {
        this.nullId = nullId;
    }
}
