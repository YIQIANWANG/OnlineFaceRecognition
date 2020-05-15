package com.chenframework.common.exception;

/**
 * DAO层异常
 */
public class DaoRuntimeException extends BusinessException {
    private static final long serialVersionUID = 1672314203491245886L;

    public DaoRuntimeException(String message) {
        super(message);
    }

    public DaoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoRuntimeException(Throwable cause) {
        super(cause);
    }

    protected DaoRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
