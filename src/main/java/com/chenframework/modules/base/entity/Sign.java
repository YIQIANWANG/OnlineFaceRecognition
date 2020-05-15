package com.chenframework.modules.base.entity;

import com.chenframework.common.persistence.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 课堂签到记录表
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_b_sign")
public class Sign extends IdEntity {
    private static final long serialVersionUID = 3193275122442878538L;

    private String name;  // 课程名称
    private Date createTime; // 创建时间
    private Integer totalNum; // 总人数
    private Integer signNum;// 已签到人数

}
