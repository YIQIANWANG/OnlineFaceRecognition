package com.chenframework.common.model;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.chenframework.common.utils.StringUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 数据查询排序参数
 */
@Getter
@Setter
@NoArgsConstructor
public class SortParams {
    public final static String ASC = "asc";
    public final static String DESC = "desc";

    private String sortName;
    private String sortOrder;

    public SortParams(String sortName) {
        this(sortName, true);
    }

    public SortParams(String sortName, boolean isAsc) {
        this.sortName = sortName;
        this.sortOrder = isAsc ? ASC : DESC;
    }

    /**
     * 设置默认的排序方式，如果当前对象已经有排序方式，则默认值设置无效
     */
    public void setDefaultSort(String property, boolean isAsc) {
        if (StringUtil.isEmpty(this.sortName)) {
            this.sortName = property;
            this.sortOrder = isAsc ? ASC : DESC;
        }
    }

    /**
     * 创建数据查询排序信息
     */
    public Sort createSort() {
        return StringUtil.isEmpty(sortName) ? Sort.unsorted() : new Sort(DESC.equals(sortOrder) ? Direction.DESC : Direction.ASC, sortName);
    }

}
