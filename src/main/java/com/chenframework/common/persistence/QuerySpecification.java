package com.chenframework.common.persistence;

/**
 * 实体查询条件组装
 */
public class QuerySpecification<T> extends SimpleSpecification<T> {
    private static final long serialVersionUID = 1L;

    public QuerySpecification<T> andEq(String property, Object value) {
        this.and(QueryParams.eq(property, value));
        return this;
    }

    public QuerySpecification<T> andEq(String property, Object value, boolean ignoreCase) {
        this.and(QueryParams.eq(property, value, ignoreCase));
        return this;
    }

    public QuerySpecification<T> andEqNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.andEq(property, value);
        }
        return this;
    }

    public QuerySpecification<T> andEqNotEmpty(String property, Object value, boolean ignoreCase) {
        if (this.checkNotEmpty(value)) {
            this.andEq(property, value, ignoreCase);
        }
        return this;
    }

    public QuerySpecification<T> andGe(String property, Object value) {
        this.and(QueryParams.ge(property, value));
        return this;
    }

    public QuerySpecification<T> andGeNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.andGe(property, value);
        }
        return this;
    }

    public QuerySpecification<T> andGt(String property, Object value) {
        this.and(QueryParams.gt(property, value));
        return this;
    }

    public QuerySpecification<T> andGtNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.andGt(property, value);
        }
        return this;
    }

    public QuerySpecification<T> andIn(String property, Object value) {
        this.and(QueryParams.in(property, value));
        return this;
    }

    public QuerySpecification<T> andIsNotNull(String property) {
        this.and(QueryParams.isNotNull(property));
        return this;
    }

    public QuerySpecification<T> andIsNull(String property) {
        this.and(QueryParams.isNull(property));
        return this;
    }

    public QuerySpecification<T> andLe(String property, Object value) {
        this.and(QueryParams.le(property, value));
        return this;
    }

    public QuerySpecification<T> andLeNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.andLe(property, value);
        }
        return this;
    }

    public QuerySpecification<T> andLike(String property, Object value) {
        this.and(QueryParams.like(property, value));
        return this;
    }

    public QuerySpecification<T> andLikeNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.andLike(property, value);
        }
        return this;
    }

    public QuerySpecification<T> andLt(String property, Object value) {
        this.and(QueryParams.lt(property, value));
        return this;
    }

    public QuerySpecification<T> andLtNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.andLt(property, value);
        }
        return this;
    }

    public QuerySpecification<T> andNe(String property, Object value) {
        this.and(QueryParams.ne(property, value));
        return this;
    }

    public QuerySpecification<T> andNe(String property, Object value, boolean ignoreCase) {
        this.and(QueryParams.ne(property, value, ignoreCase));
        return this;
    }

    public QuerySpecification<T> orEq(String property, Object value) {
        this.or(QueryParams.eq(property, value));
        return this;
    }

    public QuerySpecification<T> orEqNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.orEq(property, value);
        }
        return this;
    }

    public QuerySpecification<T> orEq(String property, Object value, boolean ignoreCase) {
        this.or(QueryParams.eq(property, value, ignoreCase));
        return this;
    }

    public QuerySpecification<T> orEqNotEmpty(String property, Object value, boolean ignoreCase) {
        if (this.checkNotEmpty(value)) {
            this.orEq(property, value, ignoreCase);
        }
        return this;
    }

    public QuerySpecification<T> orGe(String property, Object value) {
        this.or(QueryParams.ge(property, value));
        return this;
    }

    public QuerySpecification<T> orGeNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.orGe(property, value);
        }
        return this;
    }

    public QuerySpecification<T> orGt(String property, Object value) {
        this.or(QueryParams.gt(property, value));
        return this;
    }

    public QuerySpecification<T> orGtNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.orGt(property, value);
        }
        return this;
    }

    public QuerySpecification<T> orIn(String property, Object value) {
        this.or(QueryParams.in(property, value));
        return this;
    }

    public QuerySpecification<T> orIsNotNull(String property) {
        this.or(QueryParams.isNotNull(property));
        return this;
    }

    public QuerySpecification<T> orIsNull(String property) {
        this.or(QueryParams.isNull(property));
        return this;
    }

    public QuerySpecification<T> orLe(String property, Object value) {
        this.or(QueryParams.le(property, value));
        return this;
    }

    public QuerySpecification<T> orLeNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.orLe(property, value);
        }
        return this;
    }

    public QuerySpecification<T> orLike(String property, Object value) {
        this.or(QueryParams.like(property, value));
        return this;
    }

    public QuerySpecification<T> orLikeNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.orLike(property, value);
        }
        return this;
    }

    public QuerySpecification<T> orLt(String property, Object value) {
        this.or(QueryParams.lt(property, value));
        return this;
    }

    public QuerySpecification<T> orLtNotEmpty(String property, Object value) {
        if (this.checkNotEmpty(value)) {
            this.orLt(property, value);
        }
        return this;
    }

    public QuerySpecification<T> orNe(String property, Object value) {
        this.or(QueryParams.ne(property, value));
        return this;
    }

    public QuerySpecification<T> orNe(String property, Object value, boolean ignoreCase) {
        this.or(QueryParams.ne(property, value, ignoreCase));
        return this;
    }

    /*
     * 判断值是否为空或者空串
     */
    private boolean checkNotEmpty(Object value) {
        return value != null && !value.toString().equals("");

    }
}
