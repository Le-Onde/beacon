package com.shida.eduservice.service;

import com.shida.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shida.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-05-03
 */
public interface EduChapterService extends IService<EduChapter> {

    List<ChapterVo> getChapterVideoByCourseId(String courseId);


    boolean deleteChapter(String chapterId);

    void removeChapterByCourseId(String courseId);
}
