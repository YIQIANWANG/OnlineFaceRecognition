package com.chenframework.common.persistence.repository;

import com.chenframework.common.persistence.processor.BeanPostProcessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;

/**
 * 创建一个自定义的RepositoryFactoryBean去替代默认的工厂类
 */
public class CusRepositoryFactoryBean<R extends JpaRepository<T, String>, T> extends JpaRepositoryFactoryBean<R, T, String> {

    public CusRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        JpaRepositoryFactory jpaRepositoryFactory = new CustomRepositoryFactory<T>(em);
        jpaRepositoryFactory.addRepositoryProxyPostProcessor(new BeanPostProcessor());
        return jpaRepositoryFactory;
    }

    private static class CustomRepositoryFactory<T> extends JpaRepositoryFactory {

        public CustomRepositoryFactory(EntityManager em) {
            super(em);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            return new BaseRepositoryImpl<>((Class<T>) information.getDomainType(), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseRepositoryImpl.class;
        }
    }
}
