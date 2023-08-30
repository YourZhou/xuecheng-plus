package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @description 教师Dto
 */
@Data
@ToString
public class TeacherDto{
    /**
     * 主键
     */
    private Long id;
    /**
     * 课程标识
     */
    @NotEmpty(message = "课程标识不能为空")
    private Long courseId;

    /**
     * 教师标识
     */
    @NotEmpty(message = "教师标识不能为空")
    private String teacherName;

    /**
     * 教师职位
     */
    @NotEmpty(message = "教师职位不能为空")
    private String position;

    /**
     * 教师简介
     */
    private String introduction;

    /**
     * 照片
     */
    private String photograph;

}
