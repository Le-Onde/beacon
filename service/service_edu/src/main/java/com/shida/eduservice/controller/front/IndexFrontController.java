package com.shida.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shida.commonutils.R;
import com.shida.eduservice.entity.EduCourse;
import com.shida.eduservice.entity.EduTeacher;
import com.shida.eduservice.service.EduCourseService;
import com.shida.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduTeacherService teacherService;
    //查询前8条课程 前4名老师
    @GetMapping("index")
    public R index(){
        //查询前8条课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> eduCourseList = courseService.list(wrapper);

        //查询前4条老师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> ListTeacher = teacherService.list(wrapperTeacher);

        return R.ok().data("eduList",eduCourseList).data("teacherList",ListTeacher);
    }

}
