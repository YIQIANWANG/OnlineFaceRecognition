package com.chenframework.common.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.chenframework.common.utils.clazz.BeanUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * 树形结构的实体基础类
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTreeEntity<T> extends BaseEntity {
    private static final long serialVersionUID = -4327746011754328327L;

    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String SORT = "sort";
    public static final String PARENT = "parent";
    public static final String PARENTIDS = "parentIds";
    public static final String CHILDREN = "children";

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "icon")
    private String icon;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    protected T parent;

    @Column(name = "parent_ids", length = 1000)
    protected String parentIds;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy(value = "sort asc")
    @JsonIgnore
    protected Set<T> children = new HashSet<>();

    /**
     * 图标定制
     */
    @SuppressWarnings("unchecked")
    public T resetIcon() {
        return (T) this;
    }

    /**
     * 获取父级节点的ID
     */
    public String findParentId() {
        String id = null;
        if (parent != null) {
            id = (String) BeanUtil.getFieldValue(parent, ID);
        }
        return id;
    }

    /**
     * 默认的排序比较器
     */
    public static class DefaultTreeEntityComparator<E extends BaseTreeEntity<E>> implements Comparator<E> {
        public int compare(E o1, E o2) {
            if (o1.getParent() == null && o2.getParent() != null) {
                return -1;
            }
            if (o1.getParent() != null && o2.getParent() == null) {
                return 1;
            }
            return compareSort(o1.getSort(), o2.getSort());
        }

        private int compareSort(Integer sort1, Integer sort2) {
            if (sort1 == null && sort2 == null) {
                return 0;
            }
            if (sort1 == null && sort2 != null) {
                return -1;
            }
            if (sort1 != null && sort2 == null) {
                return 1;
            }
            return sort1 < sort2 ? -1 : (sort1.equals(sort2) ? 0 : 1);
        }
    }

}
