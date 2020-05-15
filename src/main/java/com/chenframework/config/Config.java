package com.chenframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义配置文件
 */
@Getter
@Setter
@Component
// @PropertySource(value = "classpath:config/config.properties")
public class Config {

    @Autowired
    private Environment environment;

    @Value("${sys.init.data}")
    private Boolean sysInitData;
    @Value("${sys.upload.path}")
    private String sysUploadPath;
    @Value("${sys.upload.maxUploadSize}")
    private Long sysUploadMaxUploadSize;

    @Value("${path.admin}")
    private String pathAdmin;
    @Value("${path.front}")
    private String pathFront;
    @Value("${path.wap}")
    private String pathWeb;
    @Value("${path.api}")
    private String pathApi;

    /**
     * 判断是否为开发环境
     */
    public boolean isDevProfiles() {
        boolean isDev = false;
        String[] profiles = environment.getActiveProfiles();
        if (profiles != null && profiles.length > 0) {
            for (String profile : profiles) {
                if ("dev".equals(profile)) {
                    isDev = true;
                    break;
                }
            }
        }
        return isDev;
    }

}
