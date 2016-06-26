package com.jackie0.common.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分页请求信息
 * ClassName:PageRequestInfo <br/>
 * Date:     2015年08月08日 16:43 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class PageRequestInfo implements Serializable {
    private static final int DEF_SIZE = 15;
    private static final int DEF_PAGE = 1;
    private static final long serialVersionUID = -6001903009948393880L;

    public PageRequestInfo() {
        page = DEF_PAGE;
        size = DEF_SIZE;
    }

    // 排序字段名称
    private String sortFieldName;

    // 排序方式，asc、desc
    private String orderMethod;

    // 页码
    private int page;

    // 每页显示多少条
    private int size;

    // 无意义，只是JPA在生成oracle分页SQL时会查出该字段，这里只是为了防止SQL报错加上该变量
    private BigDecimal ROWNUM_;

    public BigDecimal getROWNUM_() {
        return ROWNUM_;
    }

    public void setROWNUM_(BigDecimal ROWNUM_) {
        this.ROWNUM_ = ROWNUM_;
    }

    public String getSortFieldName() {
        return sortFieldName;
    }

    public void setSortFieldName(String sortFieldName) {
        this.sortFieldName = sortFieldName;
    }

    public String getOrderMethod() {
        return orderMethod;
    }

    public void setOrderMethod(String orderMethod) {
        this.orderMethod = orderMethod;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getSort()
     */
    public Sort getSort() {
        Sort sort = null;
        if (StringUtils.isNotBlank(getSortFieldName()) && StringUtils.isNotBlank(getOrderMethod())) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (Sort.Direction.DESC.toString().equalsIgnoreCase(getOrderMethod())) {
                direction = Sort.Direction.DESC;
            }
            sort = new Sort(direction, sortFieldName);
        }
        return sort;
    }

    public PageRequest getPageRequest() {
        return new PageRequest(page, size, getSort());
    }
}
