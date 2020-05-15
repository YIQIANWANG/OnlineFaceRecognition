package com.chenframework.common.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数字操作工具类
 */
public final class NumberUtil extends NumberUtils {

    private NumberUtil() {

    }

    /**
     * double类型的保留两位小数
     */
    public static double formatDouble(double d) {
        DecimalFormat formater = new DecimalFormat("#0.##");
        return Double.parseDouble(formater.format(d));
    }

    /**
     * 计算两个double类型的差值a-b
     */
    public static double minus(double a, double b) {
        BigDecimal c = new BigDecimal(Double.valueOf(a).toString()).subtract(new BigDecimal(Double.valueOf(b).toString()));
        return c.doubleValue();
    }

    /**
     * Double类型，如果为空返回0
     */
    public static double getDouble(Double d) {
        return d == null ? 0.0 : d;
    }

    /**
     * Integer类型，如果为空返回0
     */
    public static int getInteger(Integer i) {
        return i == null ? 0 : i;
    }
}
