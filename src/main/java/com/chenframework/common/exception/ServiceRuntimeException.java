package com.chenframework.common.exception;

/**
 * Service层的异常
 */
public class ServiceRuntimeException extends BusinessException {
    private static final long serialVersionUID = 1672314203491245886L;

    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRuntimeException(Throwable cause) {
        super(cause);
    }

    protected ServiceRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
