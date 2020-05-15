package com.chenframework.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 */
public final class CollectionUtil extends CollectionUtils {

    private CollectionUtil() {

    }

    /**
     * 转换为List集合
     */
    @SafeVarargs
    public static <T> List<T> asList(T... a) {
        return new ArrayList<>(Arrays.asList(a));
    }

    /***
     * 判断数组是否为空
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 创建一个空的ArrayList
     */
    public static <E> ArrayList<E> emptyArrayList() {
        return newArrayList();
    }

    /**
     * 创建一个ArrayList数组See:Lists.newArrayList()
     */
    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... es) {
        ArrayList<E> list = new ArrayList<>();
        if (es != null && es.length > 0) {
            Collections.addAll(list, es);
        }
        return list;
    }

    /**
     * 创建一个HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * 创建一个有序的LinkedHashMap
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<>();
    }

    /**
     * 从list2中找出不在list1中的元素<br>
     * 元素T需要实现equals方法
     */
    public static <T> List<T> getDiffrent(List<T> list1, List<T> list2) {
        if (isEmpty(list1)) {
            return isEmpty(list2) ? emptyArrayList() : list2;
        }
        if (isEmpty(list2)) {
            return emptyArrayList();
        }

        List<T> list = new ArrayList<>();
        Map<T, Integer> map = new HashMap<>();
        list1.forEach(l1 -> {
            Integer times = map.get(l1);
            times = times == null ? 1 : times + 1;
            map.put(l1, times);
        });

        list2.forEach(l2 -> {
            Integer times = map.get(l2);
            if (times == null && !list.contains(l2)) {
                list.add(l2);
            }
        });

        return list;
    }
}
