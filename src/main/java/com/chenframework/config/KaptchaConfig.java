package com.chenframework.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码配置
 */
@Component
public class KaptchaConfig {

    @Value("${kaptcha.border.color}")
    private String kaptchaBorderColor;
    @Value("${kaptcha.textproducer.font.color}")
    private String kaptchaTextproducerFontColor;
    @Value("${kaptcha.image.width}")
    private String kaptchaImageWidth;
    @Value("${kaptcha.image.height}")
    private String kaptchaImageHeight;
    @Value("${kaptcha.textproducer.font.size}")
    private String kaptchaTextproducerFontSize;
    @Value("${kaptcha.session.key}")
    private String kaptchaSessionKey;
    @Value("${kaptcha.textproducer.char.length}")
    private String kaptchaTextproducerCharLength;
    @Value("${kaptcha.textproducer.font.names}")
    private String kaptchaTextproducerFontNames;

    @Bean
    public DefaultKaptcha getDefaultKaptcha() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // properties.setProperty("kaptcha.border", kaptchaBorder);
        properties.setProperty("kaptcha.border.color", kaptchaBorderColor);
        properties.setProperty("kaptcha.textproducer.font.color", kaptchaTextproducerFontColor);
        properties.setProperty("kaptcha.image.width", kaptchaImageWidth);
        properties.setProperty("kaptcha.image.height", kaptchaImageHeight);
        properties.setProperty("kaptcha.textproducer.font.size", kaptchaTextproducerFontSize);
        properties.setProperty("kaptcha.session.key", kaptchaSessionKey);
        properties.setProperty("kaptcha.textproducer.char.length", kaptchaTextproducerCharLength);
        properties.setProperty("kaptcha.textproducer.font.names", kaptchaTextproducerFontNames);
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);

        return defaultKaptcha;
    }
}
