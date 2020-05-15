package com.chenframework.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串处理工具类
 */
public final class StringUtil extends StringUtils {

    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String SEPARATOR = "/";
    public static final String SEPARATOR_WINDOWS = "\\";

    private StringUtil() {

    }

    /**
     * 驼峰法转下划线：UserEntity -> user_entity || userName -> user_name
     */
    public static String convertCamel2Line(String str) {
        StringBuilder result = new StringBuilder();
        if (isNotEmpty2(str)) {
            result.append(str.substring(0, 1).toLowerCase());
            for (int i = 1; i < str.length(); i++) {
                char ch = str.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString().toLowerCase();
    }

    /**
     * 下划线转换为驼峰命名：user_name -> userName
     */
    public static String convertLine2Camel(String str) {
        StringBuilder result = new StringBuilder();
        if (isNotEmpty2(str)) {
            boolean flag = false;
            for (int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if ('_' == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return convertFirstChar2LowerCase(result.toString());
    }

    /**
     * 首字母转小写
     */
    public static String convertFirstChar2LowerCase(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 首字母转大写
     */
    public static String convertFirstChar2UpperCase(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     */
    public static int getLength(String value) {
        int valueLength = 0;
        if (isNotEmpty2(value)) {
            String chinese = "[\u0391-\uFFE5]";
            for (int i = 0; i < value.length(); i++) {
                String temp = value.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * 判断字符串中是否有空白字符，空白符包含：空格、tab键、换行符; 空值和空串返回false
     */
    public static boolean hasBlank(String str) {
        return indexOfLastBlank(str) >= 0;
    }

    /**
     * 最后一个空白符的索引
     */
    public static int indexOfLastBlank(String str) {
        int index = -1, strLen;
        if (str != null && (strLen = str.length()) > 0) {
            for (int i = 0; i < strLen; i++) {
                if (Character.isWhitespace(str.charAt(i))) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * 第一个空白符的索引
     */
    public static int indexOfFirstBlank(String str) {
        int index = -1, strLen;
        if (str != null && (strLen = str.length()) > 0) {
            for (int i = 0; i < strLen; i++) {
                if (Character.isWhitespace(str.charAt(i))) {
                    return i;
                }
            }
        }
        return index;
    }

    /**
     * 判断字符串是否为空，此方法区别于{@link #isEmpty(CharSequence)}之处在于如果是一个只包含空格的空串，也认为是空的
     */
    public static boolean isEmpty2(String str) {
        return (str == null || str.trim().equals(""));
    }

    /**
     * 判断字符串是否不为空，此方法区别于{@link #isNotEmpty(CharSequence)}之处在于如果是一个只包含空格的空串，也认为是空的
     */
    public static boolean isNotEmpty2(String str) {
        return !isEmpty2(str);
    }

    /**
     * 将特定的字符串分割成集合<code>ArrayList<String><code>对象
     * @param str 需要分割的字符串,用{@link #COMMA}作为分割标识符
     */
    public static List<String> splitByComma(String str) {
        if (isEmpty2(str)) {
            return Collections.emptyList();
        }
        String[] strs = str.split(COMMA);
        return CollectionUtil.asList(strs);
    }

    /**
     * 将String类型的集合转换为用英文的','号连接的字符串
     */
    public static String toStringWithComma(Collection<String> collection) {
        return collection.stream().filter(StringUtil::isNotEmpty2).collect(Collectors.joining(","));
    }
}
