package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.content.model.dto.TeacherDto;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程-教师关系表 前端控制器
 * </p>
 *
 * @author itcast
 */
@Api(value = "教师编辑接口", tags = "教师编辑接口")
@Slf4j
@RestController
@RequestMapping
public class CourseTeacherController {

    @Resource
    private CourseTeacherService courseTeacherService;

    @GetMapping("/courseTeacher/list/{courseId}")
    @ApiOperation("教师查询接口")
    public List<CourseTeacher> list(PageParams pageParams, @PathVariable Long courseId) {
        List<CourseTeacher> pageResult = courseTeacherService.listCourseTeacher(pageParams, courseId);
        return pageResult;
    }

    @ApiOperation("新增教师信息")
    @PostMapping("/courseTeacher")
    public CourseTeacher createCourseTeacher(@RequestBody TeacherDto teacherDto){
        return courseTeacherService.createCourseTeacher(teacherDto);
    }

    @ApiOperation("修改教师信息")
    @PutMapping("/courseTeacher")
    public CourseTeacher updateCourseTeacher(@RequestBody TeacherDto teacherDto){
        return courseTeacherService.updateCourseTeacher(teacherDto);
    }

    @ApiOperation("删除教师信息")
    @DeleteMapping("/courseTeacher/course/{courseId}/{teacherId}")
    public void removeCourseTeacher(@PathVariable Long courseId,@PathVariable Long teacherId){
        courseTeacherService.removeCourseTeacher(courseId,teacherId);
    }
}
