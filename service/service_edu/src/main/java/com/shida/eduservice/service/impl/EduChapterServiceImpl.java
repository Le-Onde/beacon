package com.shida.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shida.eduservice.entity.EduChapter;
import com.shida.eduservice.entity.EduVideo;
import com.shida.eduservice.entity.chapter.ChapterVo;
import com.shida.eduservice.entity.chapter.VideoVo;
import com.shida.eduservice.mapper.EduChapterMapper;
import com.shida.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shida.eduservice.service.EduVideoService;
import com.shida.servicebase.exceptionhandler.LeOndeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-05-03
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1 根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);
        //2 根据课程id查询课程里面所有的小结
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> videoList = videoService.list(wrapperVideo);
        //创建list集合，用于封装最终数据
        List<ChapterVo> finalList = new ArrayList<>();
        //3 遍历查询章节list集合进行封装
        //遍历查询章节list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            EduChapter eduChapter = eduChapterList.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            finalList.add(chapterVo);

            //创建集合，用于封装章节的小节
            List<VideoVo> videoVoList = new ArrayList<>();
            //4 遍历查询小节list集合，进行封装

            for (int m = 0; m < videoList.size(); m++) {
                //得到每个小节
                EduVideo eduVideo = videoList.get(m);
                //判断 小节里面chapterid和章节里面id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    //封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    videoVoList.add(videoVo);
                }

            }
            //把封装之后小节list集合，放到章节对象里面
            chapterVo.setChildren(videoVoList);
        }
        return finalList;
    }

    @Override
    public boolean deleteChapter(String chapterId) {
        //根据chapterId查询小节表，如果查询到数据 不删除，无，删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",chapterId);
        int count = videoService.count(wrapper);
        if(count > 0){
            throw new LeOndeException(20001,"不能删除,请先删除小节后删除章节");
        }else {
            int results = baseMapper.deleteById(chapterId);
            return results>0;
        }
    }

    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
