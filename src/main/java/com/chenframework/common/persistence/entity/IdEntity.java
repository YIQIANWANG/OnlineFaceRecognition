package com.chenframework.common.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 实体的ID映射
 */
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public abstract class IdEntity implements Serializable {
    private static final long serialVersionUID = 4025646643704008605L;
    public final static String ID = "id";

    @Id
    @Column(name = "id", length = 50, unique = true, nullable = false)
    protected String id;

}
