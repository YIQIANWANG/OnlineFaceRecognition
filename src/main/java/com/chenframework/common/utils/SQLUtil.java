package com.chenframework.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL处理工具类
 */
@Slf4j
public class SQLUtil {

    /**
     * 移除查询语句中的order by 子句
     */
    public static String removeOrders(String queryString) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 移除查询语句中的select子句
     */
    public static String removeSelect(String queryString) {
        int beginPos = queryString.toLowerCase().indexOf("from");
        return queryString.substring(beginPos);
    }
}
