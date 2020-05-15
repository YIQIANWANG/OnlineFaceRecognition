package com.chenframework.common.persistence.processor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.data.repository.core.RepositoryInformation;

/**
 */
public class BeanMethodInterceptor implements MethodInterceptor {

    public BeanMethodInterceptor(RepositoryInformation repositoryInformation) {

    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();

    }

}
