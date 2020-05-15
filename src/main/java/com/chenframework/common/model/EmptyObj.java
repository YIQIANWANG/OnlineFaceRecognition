package com.chenframework.common.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * 空对象
 */

public class EmptyObj implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    private String version = "1.0";

    public static EmptyObj create() {
        return new EmptyObj();
    }

}
