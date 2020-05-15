package com.chenframework.common.utils;

import org.apache.commons.lang3.SystemUtils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 系统工具类
 */
public final class SystemUtil extends SystemUtils {

    private SystemUtil() {
    }

    /**
     * 产生一个随机的长整形数据
     */
    public static long randomLong() {
        return (new SecureRandom()).nextLong();
    }

    /**
     * 获取系统自定义的UUID<br>
     * 将产生32位的UUID,并且全部转换为大写字母
     * @return UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 获取系统当前时间
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
