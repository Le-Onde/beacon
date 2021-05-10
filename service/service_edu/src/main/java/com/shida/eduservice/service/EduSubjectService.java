package com.shida.eduservice.service;

import com.shida.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shida.eduservice.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-05-03
 */
public interface EduSubjectService extends IService<EduSubject> {

    //添加课程分类
    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    //课程分类列表树形
    List<OneSubject> getAllOneTwoSubject();
}
