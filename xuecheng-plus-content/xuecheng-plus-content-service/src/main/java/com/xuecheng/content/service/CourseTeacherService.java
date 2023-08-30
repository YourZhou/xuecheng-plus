package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.content.model.dto.TeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务类
 * </p>
 *
 * @author itcast
 * @since 2022-10-07
 */
public interface CourseTeacherService extends IService<CourseTeacher> {

    List<CourseTeacher> listCourseTeacher(PageParams pageParams, Long courseId);

    CourseTeacher createCourseTeacher(TeacherDto teacherDto);

    CourseTeacher updateCourseTeacher(TeacherDto teacherDto);

    void removeCourseTeacher(Long courseId, Long teacherId);
}
