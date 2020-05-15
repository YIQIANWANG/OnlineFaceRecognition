package com.chenframework.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形模型
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class TreeModel<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @NonNull
    private T bean;
    private List<TreeModel<T>> children = new ArrayList<>();

}
