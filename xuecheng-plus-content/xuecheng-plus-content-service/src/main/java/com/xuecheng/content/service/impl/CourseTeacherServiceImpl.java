package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.TeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程-教师关系表 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class CourseTeacherServiceImpl extends ServiceImpl<CourseTeacherMapper, CourseTeacher> implements CourseTeacherService {

    @Resource
    private CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> listCourseTeacher(PageParams pageParams, Long courseId) {
        //构建查询条件对象
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        //构建查询条件，根据课程名称查询
        queryWrapper.eq(CourseTeacher::getCourseId, courseId);

        //分页对象
        Page<CourseTeacher> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 查询数据内容获得结果
        Page<CourseTeacher> pageResult = courseTeacherMapper.selectPage(page, queryWrapper);
        // 获取数据列表
        List<CourseTeacher> list = pageResult.getRecords();
        // 获取数据总数
        long total = pageResult.getTotal();
        // 构建结果集
        PageResult<CourseTeacher> courseTeacherPageResult = new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());

        return list;
    }

    @Override
    public CourseTeacher createCourseTeacher(TeacherDto teacherDto) {
        CourseTeacher courseTeacherNew = new CourseTeacher();
        BeanUtils.copyProperties(teacherDto,courseTeacherNew);
        if (teacherDto.getId()!=null){
            courseTeacherMapper.updateById(courseTeacherNew);
        }else {
            courseTeacherMapper.insert(courseTeacherNew);
        }
        return courseTeacherNew;
    }

    @Override
    public CourseTeacher updateCourseTeacher(TeacherDto teacherDto) {
        CourseTeacher courseTeacherNew = new CourseTeacher();
        BeanUtils.copyProperties(teacherDto,courseTeacherNew);
        courseTeacherMapper.updateById(courseTeacherNew);
        return courseTeacherNew;
    }

    @Override
    public void removeCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseTeacher::getCourseId,courseId).eq(CourseTeacher::getId,teacherId);
        courseTeacherMapper.delete(queryWrapper);
    }
}
