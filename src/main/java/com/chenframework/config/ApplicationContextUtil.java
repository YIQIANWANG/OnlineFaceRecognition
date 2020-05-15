package com.chenframework.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring上下文
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return CONTEXT;
    }

    public static Object getBean(String name) throws BeansException {
        return CONTEXT.getBean(name);
    }

    public static Object getBean(Class<?> requiredType) throws BeansException {
        return CONTEXT.getBean(requiredType);
    }

    public static Object getBean(String name, Class<?> requiredType) throws BeansException {
        return CONTEXT.getBean(name, requiredType);
    }
}
