package com.chenframework.common.model.view;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * 控制层ajax请求返回结果
 */
@Getter
@Setter
public abstract class AjaxResult<T> {

    public final static int CODE_UNKNOW = -2;
    public final static int CODE_ERROR = 0;
    public final static int CODE_SUCCESS = 1;
    public final static int CODE_EXPIRED = 300;
    public final static int CODE_VALIDATE = 400;
    public final static int CODE_EXCEPTION = 500;

    @NonNull
    protected Integer code = CODE_UNKNOW;
    protected String msg;
    protected T datas;

    public AjaxResult() {
    }

    public AjaxResult(int code) {
        this(code, null);
    }

    public AjaxResult(int code, String msg) {
        this(code, msg, null);
    }

    public AjaxResult(int code, String msg, T datas) {
        this.code = code;
        this.msg = msg;
        this.datas = datas;
    }

}
