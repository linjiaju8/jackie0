package com.jackie0.common.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * 通用Specification整合{@link JpaSpecificationExecutor}及{@link PagingAndSortingRepository}
 * ClassName:BaseDao
 * Date:     2015年08月04日 11:09
 *
 * @author jackie0
 * @param <T> 需要持久化的实体类型
 * @param <K> 需要持久化的实体对应的主键
 * @see
 * @since JDK 1.8
 */
@NoRepositoryBean
public interface BaseDao<T, K extends Serializable> extends JpaSpecificationExecutor<T>, PagingAndSortingRepository<T, K> {
}
