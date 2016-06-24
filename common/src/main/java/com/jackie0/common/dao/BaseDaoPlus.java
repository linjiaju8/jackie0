package com.jackie0.common.dao;

import com.jackie0.common.entity.PageRequestInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

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
public interface BaseDaoPlus {
    /**
     * 数组类型参数，分页查询通用方法
     *
     * @param e      分页实体
     * @param sql    要执行的查询语句，注意：这里不是分页语句
     * @param params 参数
     * @param <E>    分页实体的泛型类型
     * @return 分页查询结果
     * @see #findByPage(PageRequestInfo, String, Sort, Object[])
     */
    <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Object[] params);

    /**
     * 数组类型参数，分页查询通用方法，支持排序
     *
     * @param e      分页实体
     * @param sql    要执行的查询语句，注意：这里不是分页语句
     * @param sort   {@link Sort}排序对象
     * @param params 参数
     * @param <E>    分页实体的泛型类型
     * @return 分页查询结果
     */
    <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Sort sort, Object[] params);

    /**
     * map类型参数，分页查询通用方法
     *
     * @param e      分页实体
     * @param sql    要执行的查询语句，注意：这里不是分页语句
     * @param params 参数
     * @param <E>    分页实体的泛型类型
     * @return 分页查询结果
     * @see #findByPage(PageRequestInfo, String, Sort, Map)
     */
    <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Map<String, Object> params);

    /**
     * map类型参数，分页查询通用方法，支持排序
     *
     * @param e      分页实体
     * @param sql    要执行的查询语句，注意：这里不是分页语句
     * @param sort   {@link Sort}排序对象
     * @param params 参数
     * @param <E>    分页实体的泛型类型
     * @return 分页查询结果
     */
    <E extends PageRequestInfo> Page<E> findByPage(E e, String sql, Sort sort, Map<String, Object> params);

    /**
     * 数组参数，查询所有符合条件的数据，以实体形式返回
     *
     * @param clazz  要返回实体的类类型
     * @param sql    要执行的查询语句
     * @param params 参数
     * @param <E>    要返回实体的泛型类型
     * @return 符合条件的结果集
     * @see #findAll(Class, String, Map)
     */
    <E> List<E> findAll(Class<E> clazz, String sql, Object[] params);

    /**
     * map类型参数，查询所有符合条件的数据，以实体形式返回
     *
     * @param clazz  要返回实体的类类型
     * @param sql    要执行的查询语句
     * @param params 参数
     * @param <E>    要返回实体的泛型类型
     * @return 符合条件的结果集
     */
    <E> List<E> findAll(Class<E> clazz, String sql, Map<String, Object> params);

    /**
     * 数组类型参数，查询所有符合条件的数据，以Map形式返回
     *
     * @param sql    要执行的查询语句
     * @param params 参数
     * @return 符合条件的结果集
     * @see #findAllToMap(String, Map)
     */
    List<Map<String, Object>> findAllToMap(String sql, Object[] params);

    /**
     * map类型参数，查询所有符合条件的数据，以Map形式返回
     *
     * @param sql    要执行的查询语句
     * @param params 参数
     * @return 符合条件的结果集
     */
    List<Map<String, Object>> findAllToMap(String sql, Map<String, Object> params);

    /**
     * map类型参数，通用保存方法
     *
     * @param sql    要执行的sql语句
     * @param params 参数
     * @see Query#executeUpdate()
     */
    void save(String sql, Map<String, Object> params);

    /**
     * 数组类型参数，通用保存方法
     *
     * @param sql    要执行的sql语句
     * @param params 参数
     * @see Query#executeUpdate()
     * @see #save(String, Map)
     */
    void save(String sql, Object[] params);

    /**
     * 数组类型参数，查询单条记录并以实体返回
     *
     * @param clazz  要返回实体的类类型
     * @param sql    要执行的查询语句
     * @param params 参数
     * @param <E>    要返回实体的泛型类型
     * @return 符合条件的单个实体
     * @see #findOne(Class, String, Map)
     */
    <E> E findOne(Class<E> clazz, String sql, Object[] params);

    /**
     * map类型参数，查询单条记录并以实体返回
     *
     * @param clazz  要返回实体的类类型
     * @param sql    要执行的查询语句
     * @param params 参数
     * @param <E>    要返回实体的泛型类型
     * @return 符合条件的单个实体
     */
    <E> E findOne(Class<E> clazz, String sql, Map<String, Object> params);

    /**
     * 数组类型参数，查询单条记录并以Map形式返回
     *
     * @param sql    要执行的查询语句
     * @param params 参数
     * @return 符合条件的单个实体
     */
    Map<String, Object> findOneToMap(String sql, Object[] params);

    /**
     * map类型参数，查询单条记录并以Map形式返回
     *
     * @param sql    要执行的查询语句
     * @param params 参数
     * @return 符合条件的Map
     */
    Map<String, Object> findOneToMap(String sql, Map<String, Object> params);
}
