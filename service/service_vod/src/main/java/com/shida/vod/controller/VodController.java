package com.shida.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.shida.commonutils.R;
import com.shida.servicebase.exceptionhandler.LeOndeException;
import com.shida.vod.service.VodService;
import com.shida.vod.utils.ConstansVideoUtils;
import com.shida.vod.utils.InitVodClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {
    @Autowired
    private VodService vodService;

    //上传视频
    @PostMapping("uploadAliVideo")
    public R uploadAliVideo(MultipartFile file){
        //返回视频id
        String videoId = vodService.uploadAly(file);
        return R.ok().data("videoId",videoId);
    }

    //删除视频id删除阿里云视频
        @DeleteMapping("removeAlyVideo/{id}")
    public R removeAlyVideo(@PathVariable String id){
        try {
            //初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstansVideoUtils.ACCESS_KEY_ID,ConstansVideoUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //request设置视频id值
            request.setVideoIds(id);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            throw new LeOndeException(20001,"删除视频失败");
        }
    }
    //删除多个阿里云视频
    //参数多个视频id List videoIdList
    @DeleteMapping("delete-batch")
    public R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList){
        vodService.removeMoreAlyVideo(videoIdList);
        return R.ok();
    }

    //根据视频id获取视频播放凭证
    @GetMapping("getPlayAuth/{id}")
    public R getPlayAuth(@PathVariable String id){
        try {
            //创建初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstansVideoUtils.ACCESS_KEY_ID, ConstansVideoUtils.ACCESS_KEY_SECRET);
            //创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(id);
            //调用方法获取凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return R.ok().data("playAuth",playAuth);
        }catch (Exception e){
            throw new LeOndeException(20001,"获取视频凭证失败");
        }
    }
}
