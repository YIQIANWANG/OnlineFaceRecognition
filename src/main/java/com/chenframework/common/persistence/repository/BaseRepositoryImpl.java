package com.chenframework.common.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.chenframework.common.exception.DaoRuntimeException;
import com.chenframework.common.persistence.entity.BaseEntity;
import com.chenframework.common.utils.SQLUtil;
import com.chenframework.common.utils.SystemUtil;
import com.chenframework.common.utils.clazz.BeanUtil;

/**
 * 自定义JpaRepository接口实现
 */
public class BaseRepositoryImpl<T> extends SimpleJpaRepository<T, String> implements BaseRepository<T> {

    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager entityManager;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
        this.entityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, em);
    }

    @Override
    public void deleteLogic(String id) {
        boolean e = BeanUtil.checkDeclaredField(getDomainClass(), BaseEntity.DELFLAG);
        if (!e) {
            throw new DaoRuntimeException(
                    "There is no [delFlag] attribute in the entity[" + getDomainClass() + "], and no logical deletion can be performed.");
        } else {
            String hl = String.format("update %s x set x.%s = ?1 where x.id = ?2 ", entityInformation.getEntityName(), BaseEntity.DELFLAG);
            Query query = entityManager.createQuery(hl);
            query.setParameter(1, false).setParameter(2, id).executeUpdate();
        }

    }

    @Override
    public void deleteLogic(List<String> ids) {
        boolean e = BeanUtil.checkDeclaredField(getDomainClass(), BaseEntity.DELFLAG);
        if (!e) {
            throw new DaoRuntimeException(
                    "There is no [delFlag] attribute in the entity[" + getDomainClass() + "], and no logical deletion can be performed.");
        } else {
            String hl = String.format("update %s x set x.%s = ?1 where x.id in (:ids) ", entityInformation.getEntityName(), BaseEntity.DELFLAG);
            Query query = entityManager.createQuery(hl);
            query.setParameter(1, false).setParameter("ids", ids).executeUpdate();
        }
    }

    @Override
    public int executeByNativeSql(String nativeSql) {
        Query query = entityManager.createNativeQuery(nativeSql);
        return query.executeUpdate();
    }

    @Override
    @SuppressWarnings({ "deprecation", "unchecked" })
    public <S> S getByNativveSql(Class<S> clazz, String nativeSql) {
        Query query = entityManager.createNativeQuery(nativeSql);
        NativeQuery<?> hibernateNativeQuery = query.unwrap(NativeQuery.class);
        return (S) hibernateNativeQuery.setResultTransformer(Transformers.aliasToBean(clazz)).uniqueResult();
    }

    @Override
    @SuppressWarnings({ "deprecation", "unchecked" })
    public <S> List<S> findByNativeSql(Class<S> clazz, String nativeSql) {
        Query query = entityManager.createNativeQuery(nativeSql);
        NativeQuery<?> hibernateNativeQuery = query.unwrap(NativeQuery.class);
        return (List<S>) hibernateNativeQuery.setResultTransformer(Transformers.aliasToBean(clazz)).list();
    }

    /**
     * 通过原生的语句查询数据分页列表，并转换成指定的实体
     */
    @Override
    @SuppressWarnings({ "deprecation", "unchecked" })
    public <S> Page<S> findByNativeSql(Class<S> clazz, String nativeSql, PageRequest pageRequest) {
        String listQueryString = String.valueOf(nativeSql);
        String countQueryString = " select count(*) " + SQLUtil.removeSelect(SQLUtil.removeOrders(nativeSql));
        Object countObj = getByNativeSql(countQueryString);
        int totalCount = countObj == null ? 0 : Long.valueOf(countObj.toString()).intValue();

        Query query = entityManager.createNativeQuery(listQueryString);
        NativeQuery<?> hibernateNativeQuery = query.unwrap(NativeQuery.class);

        List<S> list = (List<S>) hibernateNativeQuery.setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize())
                .setResultTransformer(Transformers.aliasToBean(clazz)).list();

        return new PageImpl<>(list, pageRequest, totalCount);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByNativeSql(String nativeSql) {
        Query query = entityManager.createNativeQuery(nativeSql, getDomainClass());
        return query.getResultList();
    }

    @Override
    public Object getByNativeSql(String nativeSql) {
        Query query = entityManager.createNativeQuery(nativeSql);
        return query.getSingleResult();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.SimpleJpaRepository#save(S)
     */
    @Override
    public <S extends T> S save(S entity) {

        entityManager.persist(initIdValue(entity));
        entityManager.flush();
        return entity;
    }

    @Override
    public T update(T entity) {
        if (entityInformation.isNew(entity)) {
            throw new DaoRuntimeException("The object[" + getDomainClass() + "] is a new entity and cannot be updated");
        } else {
            T t = entityManager.merge(entity);
            entityManager.flush();
            return t;
        }
    }

    private T initIdValue(T entity) {
        String id = BaseEntity.ID;
        Object idValue = BeanUtil.getFieldValue(entity, id);
        if (idValue == null || idValue.toString().trim().equals("")) {
            BeanUtil.setFieldValue(entity, id, SystemUtil.getUUID());
        }
        return entity;
    }

}
