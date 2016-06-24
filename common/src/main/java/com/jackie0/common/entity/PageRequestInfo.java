/**
 * Copyright (C),Kingmed
 *
 * @FileName: PageRequestInfo.java
 * @Package: com.kingmed.ws.common.entity
 * @Description: 分页
 * @Author linjiaju
 * @Date 2015年11月30日 16:45
 * @History: //修改记录
 * 〈author〉      〈time〉      〈version〉       〈desc〉
 * 修改人姓名            修改时间            版本号              描述
 */
package com.jackie0.common.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分页信息
 * ClassName:PageRequestInfo <br/>
 * Date:     2015年08月08日 16:43 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.8
 */
public class PageRequestInfo implements Serializable {
    private static final int DEF_LIMIT = 15;
    private static final int DEF_OFFSET = 0;
    private static final long serialVersionUID = -6001903009948393880L;

    public PageRequestInfo() {
        limit = DEF_LIMIT;
        offset = DEF_OFFSET;
    }

    // 排序字段名称
    private String sort;

    // 排序方式，asc、desc
    private String order;

    // 每页显示多少条
    private Integer limit;

    // 数据偏移量，第一页为0
    private Integer offset;

    // 每页显示多少条
    private Integer size;

    // 数据偏移量，第一页为0
    private Integer page;

    // 无意义，只是JPA在生成分页SQL时会查出该字段，这里只是为了防止SQL报错加上该变量
    private BigDecimal ROWNUM_;

    public BigDecimal getROWNUM_() {
        return ROWNUM_;
    }

    public void setROWNUM_(BigDecimal ROWNUM_) {
        this.ROWNUM_ = ROWNUM_;
    }

    public Integer getPage() {
        return offset / limit + 1;
    }

    public Integer getSize() {
        return limit;
    }

    public Integer getLimit() {
        return limit == null ? Integer.valueOf(DEF_LIMIT) : limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset == null ? Integer.valueOf(DEF_OFFSET) : offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer findOriginalPage() {
        return page;
    }

    public Integer findOriginalSize() {
        return size;
    }
}
