package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.execption.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 课程计划 服务实现类
 * </p>
 *
 * @author itcast
 */
@Slf4j
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {

    @Resource
    private TeachplanMapper teachplanMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        //课程计划id
        Long id = teachplanDto.getId();
        //修改课程计划
        if (id != null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        } else {
            //取出同父同级别的课程计划数量
            int count = getTeachplanCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            //设置排序号
            teachplanNew.setOrderby(count+1);
            BeanUtils.copyProperties(teachplanDto,teachplanNew);
            teachplanMapper.insert(teachplanNew);
        }
    }

    @Transactional
    @Override
    public void removeTeachplan(Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        if (teachplan.getGrade()==1) {
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, teachplanId);
            Integer count = teachplanMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new XueChengPlusException("课程计划信息还有子级信息，无法操作");
            } else if (count == 0) {
                teachplanMapper.deleteById(teachplanId);
            }
        }else {
            teachplanMapper.deleteById(teachplanId);
        }
    }

    @Transactional
    @Override
    public void moveTeachplan(String state, Long teachplanId) {
        Teachplan currentTeachplan = teachplanMapper.selectById(teachplanId);
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        if ("moveup".equals(state)) {
            queryWrapper.eq(Teachplan::getParentid,currentTeachplan.getParentid())
                    .eq(Teachplan::getCourseId,currentTeachplan.getCourseId())
                    .lt(Teachplan::getOrderby,currentTeachplan.getOrderby())
                    .orderByDesc(Teachplan::getOrderby)
                    .last("LIMIT 1");
        }else if("movedown".equals(state)){
            queryWrapper.eq(Teachplan::getParentid,currentTeachplan.getParentid())
                    .eq(Teachplan::getCourseId,currentTeachplan.getCourseId())
                    .gt(Teachplan::getOrderby,currentTeachplan.getOrderby())
                    .orderByAsc(Teachplan::getOrderby)
                    .last("LIMIT 1");
        }
        Teachplan previousTeachplan = teachplanMapper.selectOne(queryWrapper);
        if (previousTeachplan == null) {
            throw new XueChengPlusException("No Teachplan found with an 'orderby' value less than " + currentTeachplan.getOrderby());
        }
        Integer currentTeachplanOrderby = currentTeachplan.getOrderby();
        Integer previousTeachplanOrderby = previousTeachplan.getOrderby();
        previousTeachplan.setOrderby(currentTeachplanOrderby);
        currentTeachplan.setOrderby(previousTeachplanOrderby);
        teachplanMapper.updateById(currentTeachplan);
        teachplanMapper.updateById(previousTeachplan);
    }

    /**
     * @param courseId 课程id
     * @param parentId 父课程计划id
     * @return int 最新排序号
     * @description 获取最新的排序号
     * @author Mr.M
     * @date 2022/9/9 13:43
     */
    private int getTeachplanCount(long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;

    }
}
