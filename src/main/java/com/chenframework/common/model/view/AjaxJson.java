package com.chenframework.common.model.view;

import lombok.Getter;
import lombok.Setter;

/**
 * 控制层ajax请求返回结果
 */
@Getter
@Setter
public class AjaxJson extends AjaxResult<AjaxData> {

    public AjaxJson() {
        this(CODE_UNKNOW);
    }

    public AjaxJson(int code) {
        this(code, null);
    }

    public AjaxJson(int code, String msg) {
        super(code, msg, AjaxData.create());
    }

    /**
     * 往datas中添加数据
     */
    public AjaxJson addData(String key, Object value) {
        this.getDatas().put(key, value);
        return this;
    }

    public AjaxJson setError() {
        return this.setError(null);
    }

    public AjaxJson setError(String msg) {
        this.setCode(CODE_ERROR);
        this.setMsg(msg);
        return this;
    }

    public AjaxJson setSuccess() {
        return this.setSuccess(null);
    }

    public AjaxJson setSuccess(String msg) {
        this.setCode(CODE_SUCCESS);
        this.setMsg(msg);
        return this;
    }

    public static AjaxJson createError() {
        return new AjaxJson(CODE_ERROR);
    }

    public static AjaxJson createError(String msg) {
        return new AjaxJson(CODE_ERROR, msg);
    }

    public static AjaxJson createSuccess() {
        return new AjaxJson(CODE_SUCCESS);
    }

    public static AjaxJson createSuccess(String msg) {
        return new AjaxJson(CODE_SUCCESS, msg);
    }
}
