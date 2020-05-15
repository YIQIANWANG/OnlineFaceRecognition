package com.chenframework.common.persistence;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * 查询参数的定义
 */
@Setter
@Getter
public class QueryParams implements Serializable {
    private static final long serialVersionUID = -8712382358441065075L;

    private Boolean ignoreCase = false;
    private String property;
    private QueryParams.Operator operator;
    private Object value;

    public enum Operator {
        eq(" = "), ge(" >= "), gt(" > "), in(" in "), isNotNull(" is not NULL "), isNull(" is NULL "), le(" <= "), like(" like "), lt(" < "), ne(
                " != ");

        private String operator;

        Operator(String operator) {
            this.operator = operator;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }
    }

    public QueryParams() {
    }

    public QueryParams(String property, QueryParams.Operator operator, Object value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public QueryParams(String property, QueryParams.Operator operator, Object value, boolean ignoreCase) {
        this.property = property;
        this.operator = operator;
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        QueryParams other = (QueryParams) obj;
        return new EqualsBuilder().append(getProperty(), other.getProperty()).append(getOperator(), other.getOperator())
                .append(getValue(), other.getValue()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProperty()).append(getOperator()).append(getValue()).toHashCode();
    }

    public QueryParams ignoreCase() {
        this.ignoreCase = true;
        return this;
    }

    public static QueryParams eq(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.eq, value);
    }

    public static QueryParams eq(String property, Object value, boolean ignoreCase) {
        return new QueryParams(property, QueryParams.Operator.eq, value, ignoreCase);
    }

    public static QueryParams ge(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.ge, value);
    }

    public static QueryParams gt(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.gt, value);
    }

    public static QueryParams in(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.in, value);
    }

    public static QueryParams isNotNull(String property) {
        return new QueryParams(property, QueryParams.Operator.isNotNull, null);
    }

    public static QueryParams isNull(String property) {
        return new QueryParams(property, QueryParams.Operator.isNull, null);
    }

    public static QueryParams le(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.le, value);
    }

    public static QueryParams like(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.like, value);
    }

    public static QueryParams lt(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.lt, value);
    }

    public static QueryParams ne(String property, Object value) {
        return new QueryParams(property, QueryParams.Operator.ne, value);
    }

    public static QueryParams ne(String property, Object value, boolean ignoreCase) {
        return new QueryParams(property, QueryParams.Operator.ne, value, ignoreCase);
    }

}
