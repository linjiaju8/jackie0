package com.jackie0.common.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * 通用Specification整合{@link JpaSpecificationExecutor}及{@link PagingAndSortingRepository}
 * ClassName:BaseDao <br/>
 * Date:     2015年08月04日 11:09 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
@NoRepositoryBean
public interface BaseDao<T, K extends Serializable> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, K> {
}
