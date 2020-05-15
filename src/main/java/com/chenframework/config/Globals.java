package com.chenframework.config;

import com.google.code.kaptcha.Constants;

import java.io.File;

/**
 * 全局变量定义
 */
public final class Globals {

    public static final String USER_SESSION_KEY = "USER_SESSION";
    public static final String USER_LOGIN_TIME_KEY = "USER_LOGIN_TIME_KEY";
    public static final String KAPTCHA_SESSION_KEY = Constants.KAPTCHA_SESSION_KEY;

    public static final String ACCONT_MANAGER = "superadmin";
    public static final String ACCONT_PASSWORD = "123456";

    public static final int HTTP_CODE_TIMEOUT = 401;
    public static final int HTTP_CODE_NOAUTH = 403;
    public static final int HTTP_CODE_NOFOUNG = 404;
    public static final int HTTP_CODE_EXCEPTION = 500;

    public static final String UTF8 = "UTF-8";

    /**
     * 系统临时文件存放路径
     */
    public static final File TEMP_DIR = new File(System.getProperty("java.io.tmpdir"), "/student/temp");

}
