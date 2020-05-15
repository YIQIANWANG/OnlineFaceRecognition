package com.chenframework.config;

import com.chenframework.common.model.json.JsonMapper;
import com.chenframework.config.interceptor.LogInterceptor;
import com.chenframework.config.web.controller.ControllerDateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * springMVC配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private Config config;

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 全局类型转换
     */
    @PostConstruct
    public void initEditableAvlidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
        assert initializer != null;
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new ControllerDateConverter());
        }
    }

    /**
     * 配置消息转换器
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(new JsonMapper());

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.valueOf("text/json;charset=UTF-8"));
        supportedMediaTypes.add(MediaType.valueOf("application/json;charset=UTF-8"));
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);

        jsonConverter.setPrettyPrint(false);

        return jsonConverter;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(Globals.UTF8);
        // resolver.setMaxUploadSize(config.getSysUploadMaxUploadSize());
        resolver.setMaxUploadSize(1024 * 1024 * 1024);
        resolver.setResolveLazily(true); // 启用是为了推迟文件解析，业务控制层自行验证文件大小
        return resolver;
    }

    /**
     * 自定义拦截器 - 日志拦截器
     */
    @Bean
    public LogInterceptor newLogInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String adminPat = config.getPathAdmin() + "/**";
        String apiPat = config.getPathApi() + "/**";
        registry.addInterceptor(newLogInterceptor()).addPathPatterns(adminPat, apiPat);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String redirect = UrlBasedViewResolver.REDIRECT_URL_PREFIX;
        registry.addViewController("/").setViewName(redirect + config.getPathAdmin() + "/index");
        registry.addViewController("/" + config.getPathAdmin()).setViewName(redirect + config.getPathAdmin() + "/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + config.getSysUploadPath()); // 映射上传到文件到磁盘的绝对路径
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    }

}
