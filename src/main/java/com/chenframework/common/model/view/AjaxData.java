package com.chenframework.common.model.view;

import java.util.HashMap;

/**
 * 控制层ajax请求返回的结果
 */
public class AjaxData extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public static AjaxData create() {
        return new AjaxData();
    }

    public AjaxData add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
