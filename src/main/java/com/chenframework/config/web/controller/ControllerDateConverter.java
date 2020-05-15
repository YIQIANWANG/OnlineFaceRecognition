package com.chenframework.config.web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.chenframework.common.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义日期类型转换器
 */
@Slf4j
public class ControllerDateConverter implements Converter<String, Date> {

    /**
     * 系统支持的转换格式
     */
    private static final String[] FORMATTER = { "yyyy-MM-dd HH:mm:ss", //
            "yyyy/MM/dd HH:mm:ss", //
            "yyyy-MM-dd HH:mm", //
            "yyyy/MM/dd HH:mm", //
            "yyyy-MM-dd", //
            "yyyy/MM/dd", //
            "yyyyMMdd" };

    @Override
    public Date convert(String source) {
        if (StringUtil.isNotEmpty2(source)) {
            for (int i = 0; i < FORMATTER.length; i++) {
                String formatter = FORMATTER[i];
                SimpleDateFormat format = new SimpleDateFormat(formatter);
                try {
                    return format.parse(source);
                } catch (ParseException e) {
                }
            }
            try {
                return new Date(new Long(source).longValue());
            } catch (Exception e) {
                log.error("Failed to converter string to date:{}", source);
            }
        }
        return null;
    }

}
