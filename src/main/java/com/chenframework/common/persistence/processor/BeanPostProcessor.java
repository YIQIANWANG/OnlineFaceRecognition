package com.chenframework.common.persistence.processor;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

/**
 * 自定义代理执行器，指定Repository代理对象
 */
public class BeanPostProcessor implements RepositoryProxyPostProcessor {

    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(new BeanMethodInterceptor(repositoryInformation));
    }

}
