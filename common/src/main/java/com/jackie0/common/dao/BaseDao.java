package com.jackie0.common.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * 通用Specification在平台BaseDao的基础上指定了主键类型，避免通过主键操作时有时会提示强制类型转换（然而并不需要）
 * ClassName:BaseDao <br/>
 * Date:     2015年08月04日 11:09 <br/>
 *
 * @author linjiaju
 * @see
 * @since JDK 1.7
 */
@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, ID> {
}
