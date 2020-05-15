package com.chenframework.common.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

/**
 * 实体通用属性
 */

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 4025646643704008605L;

    public final static String ID = "id";
    public final static String REMARK = "remark";
    public final static String ENABLE = "enable";
    public final static String DELFLAG = "delFlag";
    public final static String CREATE_TIME = "createTime";
    public final static String CREATE_ID = "createId";
    public final static String UPDATE_TIME = "updateTime";
    public final static String UPDATE_ID = "updateId";

    @CreatedDate
    @Column(name = "create_time")
    protected Date createTime;

    @CreatedBy
    @Column(name = "create_id")
    protected String createId;

    @LastModifiedDate
    @Column(name = "update_time")
    protected Date updateTime;

    @LastModifiedBy
    @Column(name = "update_id")
    protected String updateId;

    @Column(name = "enable")
    protected Boolean enable = true;

    @Column(name = "del_flag")
    protected Boolean delFlag = true; // 删除标识: true标识未删除，false标识逻辑删除

    @Column(name = "remark")
    protected String remark;

}
