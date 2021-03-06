package com.shida.eduservice.client;

import com.shida.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFileDegradeFeignClient implements VodClient{
    //出错之后执行
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("请求超时,内存可能溢出");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("请求超时,内存可能溢出");
    }


}
