package com.chenframework.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 文件上传大小限制异常
 */
@Getter
@Setter
public class FileUploadException extends RuntimeException {
    private static final long serialVersionUID = 1672314203491245886L;

    private Long maxSize;
    private Long curSize;

    public FileUploadException(Long maxSize, Long curSize) {
        super();
        this.maxSize = maxSize;
        this.curSize = curSize;
    }

}
