package com.chenframework.common.persistence.repository;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 自定义JpaRepository接口
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {

    /**
     * 逻辑删除，更新属性中的delFlag属性为删除状态
     */
    void deleteLogic(String id);

    /**
     * 逻辑删除，批量更新属性中的delFlag属性为删除状态
     */
    void deleteLogic(List<String> ids);

    /**
     * 通过原生的语句执行update/insert
     */
    int executeByNativeSql(String nativeSql);

    /**
     * 通过原生语句查询数据列表，并转换成指定的实体<br>
     * 通过将 {@link Query} 转换成{@link NativeQuery}来实现
     */
    <S> List<S> findByNativeSql(Class<S> clazz, String nativeSql);

    /**
     * 通过原生的语句查询数据分页列表，并转换成指定的实体
     */
    <S> Page<S> findByNativeSql(Class<S> clazz, String nativeSql, PageRequest pageRequest);

    /**
     * 通过原生的语句查询单个实体
     */
    <S> S getByNativveSql(Class<S> clazz, String nativeSql);

    /**
     * 通过原生语句查询实体数据列表
     */
    List<T> findByNativeSql(String nativeSql);

    /**
     * 通过原生sql获取单个结果
     */
    Object getByNativeSql(String nativeSql);

    /**
     * 实体更新
     * <p>
     * 实体entity是一个分离(detached)状态的实体，该方法会将entity的修改提交到数据库，并返回一个新的托管(managed)状态的实例；<br>
     * 实体entity是是一个托管(managed)状态的实体，它的状态不会发生任何改变。但是系统仍会在数据库执行 UPDATE 操作；<br>
     * </p>
     */
    T update(T entity);
}
