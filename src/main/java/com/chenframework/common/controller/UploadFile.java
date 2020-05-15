package com.chenframework.common.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * 上传的文件信息
 */
@Getter
@Setter
@Builder
public class UploadFile {

    @Builder.Default
    private Boolean hasFile = false;

    private String originalFilename; // 原始文件名称
    private String fileName;// 保存到系统中的文件名称
    private String path;// 保存到系统中的图片的相对路径
    private File file;// 上传保存的文件

}
