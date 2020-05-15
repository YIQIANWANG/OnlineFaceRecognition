package com.chenframework.modules.base.entity;

import com.chenframework.common.persistence.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 学生信息表
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_b_student")
public class    Student extends IdEntity {
    private static final long serialVersionUID = 3193275122442878538L;

    private String no; // 学号
    private String name; // 姓名
    private String sex; // 性别
    private String birth; //生日
    private String phone; // 手机
    private String photo; // 照片路径
    private String faceToken; // 人脸库标识

}
