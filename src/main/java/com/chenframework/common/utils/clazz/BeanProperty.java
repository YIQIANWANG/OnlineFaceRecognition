package com.chenframework.common.utils.clazz;

import lombok.Builder;
import lombok.Data;

/**
 * Bean属性实体
 */
@Data
@Builder
public class BeanProperty {

    private Class<?> clazz;// 属性的类型
    private String name;// 属性名称
    private Object value;// 属性的值

}
