package com.chenframework.common.service;

import com.chenframework.common.exception.ServiceRuntimeException;
import com.chenframework.common.model.PageParams;
import com.chenframework.common.model.SortParams;
import com.chenframework.common.persistence.QuerySpecification;
import com.chenframework.common.persistence.entity.BaseEntity;
import com.chenframework.common.persistence.entity.IdEntity;
import com.chenframework.common.persistence.repository.BaseRepository;
import com.chenframework.common.utils.FileUtil;
import com.chenframework.common.utils.StringUtil;
import com.chenframework.common.utils.clazz.BeanUtil;
import com.chenframework.common.utils.clazz.GenericsUtil;
import com.chenframework.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 通用Service层封装
 */
@Slf4j
@Transactional(readOnly = true)
public abstract class BaseService<E extends IdEntity, R extends BaseRepository<E>> {

    private Class<?> entityClass = GenericsUtil.getSuperClassGenricType(this.getClass());

    @Autowired
    protected R baseRepository;

    @Autowired
    protected Config config;

    @Transactional
    public void delete(Collection<E> entitys) {
        baseRepository.deleteAll(entitys);
    }

    @Transactional
    public void delete(E entity) {
        baseRepository.delete(entity);
    }

    /**
     * 物理删除
     */
    @Transactional
    public void delete(String id) {
        baseRepository.deleteById(id);
    }

    @Transactional
    public void delete(String[] ids) {
        if (ids != null) {
            Arrays.stream(ids).forEach(this::delete);
        }
    }

    /**
     * 逻辑删除
     */
    @Transactional
    public void deleteLogic(String id) {
        baseRepository.deleteLogic(id);
    }

    /**
     * 逻辑删除
     */
    @Transactional
    public void deleteLogic(List<String> ids) {
        baseRepository.deleteLogic(ids);
    }

    /**
     * 逻辑删除
     */
    @Transactional
    public void deleteLogic(String[] ids) {
        List<String> idList = new ArrayList<>(Arrays.asList(ids));
        baseRepository.deleteLogic(idList);
    }

    /**
     * 删除上传的文件
     */
    public void deleteUploadFile(String filePath) {
        if (StringUtil.isNotEmpty2(filePath)) {
            filePath = filePath.replace("/upload", "");
            File file = new File(config.getSysUploadPath(), filePath);
            FileUtil.deleteFile(file);
        }
    }

    /**
     * 通过制定属性和值匹配记录是否存在
     */
    @SuppressWarnings("unchecked")
    public boolean exists(String property, Object value) {
        try {
            E t = (E) entityClass.newInstance();
            BeanUtil.setFieldValue(t, property, value);

            ExampleMatcher matcher = ExampleMatcher.matching()//
                    .withMatcher(property, ExampleMatcher.GenericPropertyMatcher::exact)//
                    .withIgnorePaths(BaseEntity.DELFLAG, BaseEntity.ENABLE);

            Example<E> example = Example.of(t, matcher);
            return baseRepository.count(example) > 0;
            // return baseRepository.exists(example);
        } catch (Exception e) {
            log.error("Error!", e);
            throw new ServiceRuntimeException(e.getMessage());
        }
    }

    public List<E> findAll() {
        return baseRepository.findAll();
    }

    public Page<E> findAll(PageParams pageParams) {
        boolean isSearch = pageParams.isSearch();
        if (!isSearch) {
            return baseRepository.findAll(pageParams.createPageRequest());
        }
        QuerySpecification<E> spec = new QuerySpecification<>();
        return this.findAll(spec, pageParams);
    }

    public List<E> findAll(QuerySpecification<E> specification) {
        return baseRepository.findAll(specification);
    }

    public Page<E> findAll(QuerySpecification<E> specification, PageParams pageParams) {
        if (specification == null) {
            throw new ServiceRuntimeException("Specification must not be null");
        }

        boolean isSearch = pageParams.isSearch();
        if (isSearch) {
            List<String> fields = pageParams.getSearchFileds();
            fields.forEach(field -> {
                if (field.indexOf(".") > 0 || !BeanUtil.checkTransient(entityClass, field)) {
                    specification.orLike(field, pageParams.getSearchText());
                }
            });
        }
        return baseRepository.findAll(specification, pageParams.createPageRequest());
    }

    public List<E> findAll(QuerySpecification<E> specification, SortParams sortParams) {
        return baseRepository.findAll(specification, sortParams == null ? Sort.unsorted() : sortParams.createSort());
    }

    public List<E> findAll(SortParams sortParams) {
        return baseRepository.findAll(sortParams.createSort());
    }

    public List<E> findAllById(List<String> ids) {
        return baseRepository.findAllById(ids);
    }

    public List<E> findAllById(String[] ids) {
        return findAllById(Arrays.asList(ids));
    }

    public E findById(String id) {
        Optional<E> op = baseRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        } else {
            throw new ServiceRuntimeException("Can't find entity by id: " + id);
        }
    }

    /**
     * 通过原生语句查询数据列表，并转换成指定的实体
     */
    public <S> List<S> findByNativeSql(Class<S> clazz, String nativeSql) {
        return baseRepository.findByNativeSql(clazz, nativeSql);
    }

    /**
     * 通过原生的语句查询数据分页列表，并转换成指定的实体
     */
    public <S> Page<S> findByNativeSql(Class<S> clazz, String nativeSql, PageParams pageParams) {
        return baseRepository.findByNativeSql(clazz, nativeSql, pageParams.createPageRequest());
    }

    @Transactional
    public E save(E entity) {
        return baseRepository.save(entity);
    }

    @Transactional
    public List<E> saveOnBatch(Collection<E> entities) {
        List<E> result = new ArrayList<>();
        if (entities != null) {
            entities.forEach(entity -> result.add(save(entity)));
        }
        return result;
    }

    /**
     * 实体更新
     * <p>
     * 实体entity是一个 new 状态的实体，将无法更新；<br>
     * 实体entity是一个分离(detached)状态的实体，该方法会将entity的修改提交到数据库，并返回一个新的托管(managed)状态的实例；<br>
     * 实体entity是是一个托管(managed)状态的实体，它的状态不会发生任何改变。但是系统仍会在数据库执行 UPDATE 操作；<br>
     * 注意：如果是通过{@link #findById(String)}方法获取实体之后，修改属性值在进行更新操作，<br>
     * 建议在同一事务下操作，否则{@link #findById(String)}方法事务提交之后再调用该方法，会再次发起get查询
     * </p>
     */
    @Transactional
    public E update(E entity) {
        return baseRepository.update(entity);
    }

    protected final Class<?> getEntityClass() {
        return entityClass;
    }

}
