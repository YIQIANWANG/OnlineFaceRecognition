package com.chenframework.config.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * WEB应用启动监听器
 */
@Slf4j
@WebListener
public class WebApplicationListener implements ServletContextListener {


    @Override
    public final void contextInitialized(ServletContextEvent event) {
        log.debug("The web application is initializing...");
        WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public final void contextDestroyed(ServletContextEvent event) {
        log.debug("The web application is destroyed");
    }

}
