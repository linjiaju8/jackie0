package com.jackie0.common.dao.impl;

import com.jackie0.common.dao.BaseDaoPlus;
import com.jackie0.common.entity.PageRequestInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * spring-data-jpa公用DAO实现
 * ClassName:BaseDaoImpl <br/>
 * Date:     2015年08月29日 10:05 <br/>
 *
 * @author jackie0
 * @see
 * @since JDK 1.8
 */
public class BaseDaoImpl implements BaseDaoPlus {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @see #findByPage(PageRequestInfo, String, Sort, Map, Object[])
     */
    @Override
    public <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Object[] params) {
        return findByPage(e, sql, null, params);
    }

    /**
     * @see #findByPage(PageRequestInfo, String, Sort, Map, Object[])
     */
    @Override
    public <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Map<String, Object> params) {
        return findByPage(e, sql, null, params);
    }

    /**
     * @see #findByPage(PageRequestInfo, String, Sort, Map, Object[])
     */
    @Override
    public <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Sort sort, Object[] params) {
        return findByPage(e, sql, sort, null, params);
    }

    /**
     * @see #findByPage(PageRequestInfo, String, Sort, Map, Object[])
     */
    @Override
    public <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Sort sort, Map<String, Object> params) {
        return findByPage(e, sql, sort, params, null);
    }

    /**
     * 分页查询通用方法，具体实现，通过{@link Query#setMaxResults(int)}
     * {@link Query#setFirstResult(int)}实现
     *
     * @param e           分页实体对象
     * @param sql         要执行的查询语句
     * @param sort        {@link Sort}排序对象
     * @param mapParams   Map类型参数
     * @param arrayParams 数组类型参数
     * @param <E>         分页实体的泛型类型
     * @return 分页结果
     */
    private <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Sort sort, Map<String, Object> mapParams, Object[] arrayParams) {
        Query countQuery = entityManager.createNativeQuery(sql);
        String pageSql = buildSortSql(sql, sort);
        Query resultQuery = entityManager.createNativeQuery(pageSql);
        LOGGER.debug("count sql-->{}", sql);
        LOGGER.debug("page sql-->{}", pageSql);
        setParams(mapParams, arrayParams, countQuery);
        setParams(mapParams, arrayParams, resultQuery);
        return buildPageResult(e, countQuery, resultQuery);
    }

    /**
     * 构建分页结果集
     *
     * @param e           分页实体对象
     * @param countQuery  统计总数的{@link Query}对象
     * @param resultQuery 分页查询的{@link Query}对象
     * @param <E>         分页实体的泛型类型
     * @return 分页结果集
     */
    private <E extends PageRequestInfo> Page<E> buildPageResult(E e, Query countQuery, Query resultQuery) {
        int total = countQuery.getResultList().size();
        List<E> list = null;
        if (total > 0) {
            resultQuery.unwrap(SQLQuery.class).setResultTransformer(new AliasToBeanResultTransformer(e.getClass()));
            list = resultQuery.setMaxResults(e.getPageRequest().getPageNumber()).setFirstResult(e.getPageRequest().getOffset()).getResultList();
        }
        // 分离内存中受EntityManager管理的实体bean，让VM进行垃圾回收
        entityManager.clear();
        if (list != null) {
            new PageImpl<>(list, e.getPageRequest(), total);
        }
        return null;
    }

    /**
     * 根据排序对象{@link Sort}拼接排序SQL
     *
     * @param sql  查询sql语句
     * @param sort {@link Sort}排序对象
     * @return 拼接排序后的sql
     */
    private String buildSortSql(String sql, Sort sort) {
        StringBuilder resultSqlBd = new StringBuilder(sql);
        if (sort != null) {
            int i = 0;
            for (Sort.Order order : sort) {
                if (i == 0) {
                    resultSqlBd.append("\n order by ").append(order.getProperty()).append(" ").append(order.getDirection());
                } else {
                    resultSqlBd.append(" , ").append(order.getProperty()).append(" ").append(order.getDirection());
                }
                i++;
            }
        }
        return resultSqlBd.toString();
    }

    /**
     * @see #findAll(String, Map, Object[], ResultTransformer)
     */
    @Override
    public <E> List<E> findAll(Class<E> clazz, String sql, Object[] params) {
        return findAll(sql, null, params, new AliasToBeanResultTransformer(clazz));
    }

    /**
     * @see #findAll(String, Map, Object[], ResultTransformer)
     */
    @Override
    public <E> List<E> findAll(Class<E> clazz, String sql, Map<String, Object> params) {
        return findAll(sql, params, null, new AliasToBeanResultTransformer(clazz));
    }

    /**
     * 查询所有通用实现
     *
     * @param sql         查询语句
     * @param mapParams   Map类型的参数
     * @param arrayParams 数组类型的参数
     * @param transformer {@link ResultTransformer}
     * @param <E>         返回实体的泛型类型
     * @return 符合条件的结果集
     */
    private <E> List<E> findAll(String sql, Map<String, Object> mapParams, Object[] arrayParams, ResultTransformer transformer) {
        Query resultQuery = entityManager.createNativeQuery(sql);
        LOGGER.debug("sql-->{}", sql);
        setParams(mapParams, arrayParams, resultQuery);
        resultQuery.unwrap(SQLQuery.class).setResultTransformer(transformer);
        List<E> list = resultQuery.getResultList();
        // 分离内存中受EntityManager管理的实体bean，让VM进行垃圾回收
        entityManager.clear();
        return list;
    }

    /**
     * @see #findAll(String, Map, Object[], ResultTransformer)
     */
    @Override
    public List<Map<String, Object>> findAllToMap(String sql, Object[] params) {
        return findAll(sql, null, params, Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * @see #findAll(String, Map, Object[], ResultTransformer)
     */
    @Override
    public List<Map<String, Object>> findAllToMap(String sql, Map<String, Object> params) {
        return findAll(sql, params, null, Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * @see #save(String, Map, Object[])
     */
    @Override
    public void save(String sql, Map<String, Object> params) {
        save(sql, params, null);
    }

    /**
     * @see #save(String, Map, Object[])
     */
    @Override
    public void save(String sql, Object[] params) {
        save(sql, null, params);
    }

    /**
     * @see #findOne(String, Map, Object[], ResultTransformer)
     */
    @Override
    public <E> E findOne(Class<E> clazz, String sql, Map<String, Object> params) {
        return findOne(sql, params, null, new AliasToBeanResultTransformer(clazz));
    }

    /**
     * @see #findOne(String, Map, Object[], ResultTransformer)
     */
    @Override
    public Map<String, Object> findOneToMap(String sql, Object[] params) {
        return findOne(sql, null, params, Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * @see #findOne(String, Map, Object[], ResultTransformer)
     */
    @Override
    public Map<String, Object> findOneToMap(String sql, Map<String, Object> params) {
        return findOne(sql, params, null, Transformers.ALIAS_TO_ENTITY_MAP);
    }

    /**
     * @see #findOne(String, Map, Object[], ResultTransformer)
     */
    @Override
    public <E> E findOne(Class<E> clazz, String sql, Object[] params) {
        return findOne(sql, null, params, new AliasToBeanResultTransformer(clazz));
    }

    /**
     * 查询单条记录通用方法
     *
     * @param sql         查询语句
     * @param mapParams   Map类型参数
     * @param arrayParams 数组类型参数
     * @param transformer {@link ResultTransformer}
     * @param <E>         返回实体的泛型类型
     * @return 符合条件的实体
     */
    private <E> E findOne(String sql, Map<String, Object> mapParams, Object[] arrayParams, ResultTransformer transformer) {
        Query resultQuery = entityManager.createNativeQuery(sql);
        LOGGER.debug("sql-->{}", sql);
        setParams(mapParams, arrayParams, resultQuery);
        resultQuery.unwrap(SQLQuery.class).setResultTransformer(transformer);
        E e;
        try {
            e = (E) resultQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            // 没有结果会抛该异常
            e = null;
        }
        // 分离内存中受EntityManager管理的实体bean，让VM进行垃圾回收
        entityManager.clear();
        return e;
    }

    /**
     * @param sql         要执行的sql语句
     * @param mapParams   Map类型参数
     * @param arrayParams 数组类型参数
     * @see Query#executeUpdate()
     */
    private void save(String sql, Map<String, Object> mapParams, Object[] arrayParams) {
        Query query = entityManager.createNativeQuery(sql);
        LOGGER.debug("sql-->{}", sql);
        setParams(mapParams, arrayParams, query);
        query.executeUpdate();
        entityManager.flush();
        entityManager.clear();
    }

    private void setParams(Map<String, Object> mapParams, Object[] arrayParams, Query resultQuery) {
        setMapParams(mapParams, resultQuery);
        setArrayParams(arrayParams, resultQuery);
    }

    private void setMapParams(Map<String, Object> params, Query resultQuery) {
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                LOGGER.debug("params-->key:{},value:{}", entry.getKey(), entry.getValue());
                resultQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }

    private void setArrayParams(Object[] params, Query resultQuery) {
        if (ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; i++) {
                LOGGER.debug("params-->{}", params[i]);
                resultQuery.setParameter(i + 1, params[i]);
            }
        }
    }
}
