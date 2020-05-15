package com.chenframework.common.persistence;

import com.chenframework.common.utils.BooleanUtil;
import com.chenframework.common.utils.StringUtil;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 实体简易查询规范
 */
public class SimpleSpecification<T> implements Specification<T> {
    private static final long serialVersionUID = 1L;

    private static final String PROPERTY_SEPARATOR = StringUtil.DOT;

    @Getter
    private List<QueryParams> andParams = new ArrayList<>();
    @Getter
    private List<QueryParams> orParams = new ArrayList<>();

    public SimpleSpecification<T> and(QueryParams filter) {
        this.andParams.add(filter);
        return this;
    }

    public SimpleSpecification<T> and(QueryParams... filter) {
        this.andParams.addAll(Arrays.asList(filter));
        return this;
    }

    public SimpleSpecification<T> clearAll() {
        if (!this.andParams.isEmpty()) {
            this.andParams.clear();
        }
        if (!this.orParams.isEmpty()) {
            this.orParams.clear();
        }
        return this;
    }

    public SimpleSpecification<T> clearAnd() {
        if (!this.andParams.isEmpty()) {
            this.andParams.clear();
        }
        return this;
    }

    public SimpleSpecification<T> clearOr() {
        if (!this.orParams.isEmpty())
            this.andParams.clear();
        return this;
    }

    public SimpleSpecification<T> or(QueryParams filter) {
        this.orParams.add(filter);
        return this;
    }

    public SimpleSpecification<T> or(QueryParams... filter) {
        this.orParams.addAll(Arrays.asList(filter));
        return this;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate restrictions = cb.and(toAndPredicate(root, cb));
        if (!orParams.isEmpty()) {
            restrictions = cb.and(restrictions, toOrPredicate(root, cb));
        }
        return restrictions;
    }

    @SuppressWarnings("unchecked")
    private <X> Path<X> getPath(Path<?> path, String propertyPath) {
        if (path == null || StringUtil.isEmpty(propertyPath)) {
            return (Path<X>) path;
        }
        String property = StringUtil.substringBefore(propertyPath, PROPERTY_SEPARATOR);
        return getPath(path.get(property), StringUtil.substringAfter(propertyPath, PROPERTY_SEPARATOR));
    }

    @SuppressWarnings("unchecked")
    private Predicate toAndPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate restrictions = criteriaBuilder.conjunction();
        if (root == null || CollectionUtils.isEmpty(andParams)) {
            return restrictions;
        }
        for (QueryParams filter : andParams) {
            String property = filter.getProperty();
            QueryParams.Operator operator = filter.getOperator();
            Object value = filter.getValue();
            boolean ignoreCase = BooleanUtil.isTrue(filter.getIgnoreCase());
            Path<?> path = getPath(root, property);
            if (path == null) {
                continue;
            }
            Class<?> javaType = path.getJavaType();
            switch (operator) {
            case eq:
                if (value != null) {
                    if (ignoreCase && String.class.isAssignableFrom(javaType) && value instanceof String) {
                        restrictions = criteriaBuilder.and(restrictions,
                                criteriaBuilder.equal(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(path, value));
                    }
                } else {
                    restrictions = criteriaBuilder.and(restrictions, path.isNull());
                }
                break;
            case ne:
                if (value != null) {
                    if (ignoreCase && String.class.isAssignableFrom(javaType) && value instanceof String) {
                        restrictions = criteriaBuilder.and(restrictions,
                                criteriaBuilder.notEqual(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.notEqual(path, value));
                    }
                } else {
                    restrictions = criteriaBuilder.and(restrictions, path.isNotNull());
                }
                break;
            case gt:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan((Path<String>) path, (String) value));
                }
                break;
            case lt:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lt((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThan((Path<String>) path, (String) value));
                }
                break;
            case ge:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo((Path<String>) path, (String) value));
                }
                break;
            case le:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo((Path<String>) path, (String) value));
                }
                break;
            case like:
                if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    if (BooleanUtil.isTrue(ignoreCase)) {
                        restrictions = criteriaBuilder.and(restrictions,
                                criteriaBuilder.like(criteriaBuilder.lower((Path<String>) path), "%" + ((String) value).toLowerCase() + "%"));
                    } else {
                        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like((Path<String>) path, "%" + value + "%"));
                    }
                }
                break;
            case in:
                restrictions = criteriaBuilder.and(restrictions, path.in(value));
                break;
            case isNull:
                restrictions = criteriaBuilder.and(restrictions, path.isNull());
                break;
            case isNotNull:
                restrictions = criteriaBuilder.and(restrictions, path.isNotNull());
                break;
            }
        }
        return restrictions;
    }

    @SuppressWarnings("unchecked")
    private Predicate toOrPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
        Predicate restrictions = criteriaBuilder.disjunction();
        if (root == null || CollectionUtils.isEmpty(orParams)) {
            return restrictions;
        }
        for (QueryParams filter : orParams) {
            if (filter == null) {
                continue;
            }
            String property = filter.getProperty();
            QueryParams.Operator operator = filter.getOperator();
            Object value = filter.getValue();
            boolean ignoreCase = BooleanUtil.isTrue(filter.getIgnoreCase());
            Path<?> path = getPath(root, property);
            if (path == null) {
                continue;
            }
            Class<?> javaType = path.getJavaType();
            switch (operator) {
            case eq:
                if (value != null) {
                    if (ignoreCase && String.class.isAssignableFrom(javaType) && value instanceof String) {
                        restrictions = criteriaBuilder.or(restrictions,
                                criteriaBuilder.equal(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.equal(path, value));
                    }
                } else {
                    restrictions = criteriaBuilder.or(restrictions, path.isNull());
                }
                break;
            case ne:
                if (value != null) {
                    if (ignoreCase && String.class.isAssignableFrom(javaType) && value instanceof String) {
                        restrictions = criteriaBuilder.or(restrictions,
                                criteriaBuilder.notEqual(criteriaBuilder.lower((Path<String>) path), ((String) value).toLowerCase()));
                    } else {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.notEqual(path, value));
                    }
                } else {
                    restrictions = criteriaBuilder.or(restrictions, path.isNotNull());
                }
                break;
            case gt:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.gt((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.greaterThan((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.greaterThan((Path<String>) path, (String) value));
                }
                break;
            case lt:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lt((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lessThan((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lessThan((Path<String>) path, (String) value));
                }
                break;
            case ge:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.ge((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.greaterThanOrEqualTo((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.greaterThanOrEqualTo((Path<String>) path, (String) value));
                }
                break;
            case le:
                if (Number.class.isAssignableFrom(javaType) && value instanceof Number) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.le((Path<Number>) path, (Number) value));
                } else if (Date.class.isAssignableFrom(javaType) && value instanceof Date) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lessThanOrEqualTo((Path<Date>) path, (Date) value));
                } else if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.lessThanOrEqualTo((Path<String>) path, (String) value));
                }
                break;
            case like:
                if (String.class.isAssignableFrom(javaType) && value instanceof String) {
                    if (BooleanUtil.isTrue(ignoreCase)) {
                        restrictions = criteriaBuilder.or(restrictions,
                                criteriaBuilder.like(criteriaBuilder.lower((Path<String>) path), "%" + ((String) value).toLowerCase() + "%"));
                    } else {
                        restrictions = criteriaBuilder.or(restrictions, criteriaBuilder.like((Path<String>) path, "%" + value + "%"));
                    }
                }
                break;
            case in:
                restrictions = criteriaBuilder.or(restrictions, path.in(value));
                break;
            case isNull:
                restrictions = criteriaBuilder.or(restrictions, path.isNull());
                break;
            case isNotNull:
                restrictions = criteriaBuilder.or(restrictions, path.isNotNull());
                break;
            }
        }
        return restrictions;
    }

}
