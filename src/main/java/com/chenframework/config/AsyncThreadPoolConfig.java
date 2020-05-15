package com.chenframework.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务（多线程）线程池配置<br>
 * 启动类中需要加上@EnableAsync<br>
 * 在异步执行的方法或者类上加上@Async
 */
@Configurable
public class AsyncThreadPoolConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(availableProcessors);// 核心线程池数量， 返回可用处理器的Java虚拟机的数量。
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 5);// 最大线程数量
        executor.setQueueCapacity(availableProcessors * 2);// 线程池的队列容量
        executor.setThreadNamePrefix("ls-framework-excutor-");// 线程名称的前缀
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
