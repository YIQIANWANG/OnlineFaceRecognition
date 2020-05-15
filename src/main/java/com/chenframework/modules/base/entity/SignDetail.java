package com.chenframework.modules.base.entity;

import com.chenframework.common.persistence.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 签到明细表
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_b_sign_detail")
public class SignDetail extends IdEntity {
    private static final long serialVersionUID = 3193275122442878538L;

    private String signId; // 对应的课堂签到记录表id

    private String no; // 学号
    private String name; // 姓名
    private String sex; // 性别
    private String birth; //生日
    private String phone; // 手机
    private String photo; // 照片路径

    private Boolean isSuccess; // 是否签到成功
    private Date signTime; // 签到时间

    @Transient
    private Integer times; // 缺勤次数

}
