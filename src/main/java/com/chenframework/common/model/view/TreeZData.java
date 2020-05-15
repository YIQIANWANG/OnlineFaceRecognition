package com.chenframework.common.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * ZTree视图数据节点模型
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class TreeZData {

    @NonNull
    private Object id;
    @NonNull
    private Object pId;
    @NonNull
    private String name;
    private Boolean open = true;
    private Boolean checked = false;

    public Object getpId() {
        return pId;
    }

}
