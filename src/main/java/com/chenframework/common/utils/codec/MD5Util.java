package com.chenframework.common.utils.codec;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * MD5密码加密工具类
 */
@Slf4j
public abstract class MD5Util {

    public final static String DEFAULT_SALT = "";
    public final static int DEFAULT_HASHITERATIONS = 1;

    private MD5Util() {

    }

    /**
     * 比较原文与密文是否匹配
     * @param rawText 原文
     * @param encText 密文
     * @see #encode(String)
     */
    public static boolean compare(String rawText, String encText) {
        return encode(rawText).equals(encText);
    }

    /**
     * 比较原文与密文是否匹配
     * @param rawText 原文
     * @param encText 密文
     * @param salt 盐
     * @see #encode(String,String)
     */
    public static boolean compare(String rawText, String encText, String salt) {
        return encode(rawText, salt).equals(encText);
    }

    /**
     * 比较原文与密文是否匹配
     * @param rawText 原文
     * @param encText 密文
     * @param salt 盐
     * @param hashIterations 散列值
     * @see #encode(String,String,int)
     */
    public static boolean compare(String rawText, String encText, String salt, int hashIterations) {
        return encode(rawText, salt, hashIterations).equals(encText);
    }

    /**
     * MD5加密,直接加密
     * @param rawText 明文
     * @return 返回密文
     */
    public static String encode(String rawText) {
        return encode(rawText, DEFAULT_SALT);
    }

    /**
     * MD5加密,用盐加密
     * @param rawText 明文
     * @param salt 盐
     * @return 返回密文
     */
    public static String encode(String rawText, String salt) {
        return encode(rawText, salt, DEFAULT_HASHITERATIONS);
    }

    /**
     * MD5加密,用盐和散列值加密
     * @param rawText 明文
     * @param salt 盐
     * @param hashIterations 散列值
     * @return 返回密文
     */
    public static String encode(String rawText, String salt, int hashIterations) {
        return createMd5Hash(rawText, salt, hashIterations).toString();
    }

    /**
     * 创建一个MD5加密工具类
     * @param rawText 明文
     * @param salt 盐 如果为空,将不用盐加密
     * @param hashIterations 散列次数 如果为空,将不用散列值加密
     */
    public static Md5Hash createMd5Hash(String rawText, String salt, Integer hashIterations) {
        if (rawText == null) {
            String msg = "The system does not support null encryption";
            log.warn(msg);
            throw new RuntimeException(msg);
        }
        if (salt == null) {
            return new Md5Hash(rawText);
        }

        if (hashIterations == null) {
            return new Md5Hash(rawText, salt);
        }

        return new Md5Hash(rawText, salt, hashIterations);
    }
}
