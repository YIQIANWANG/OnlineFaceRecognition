package com.chenframework.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常基类
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1672314203491245886L;

    @Getter
    @Setter
    private String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }

}
