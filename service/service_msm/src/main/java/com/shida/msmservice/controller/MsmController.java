package com.shida.msmservice.controller;

import com.shida.commonutils.R;
import com.shida.msmservice.service.MsmService;
import com.shida.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        // 从redis获取验证码，如果获取数据直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }
        //如果redis获取不到，传递到阿里云进行发送
        //生成随机数，传递阿里云进行发送
        code = RandomUtil.getFourBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code",code);
        //调用service发送短信的方法
        boolean isSend = msmService.send(param,phone);
        if(isSend) {
            //发送成功，把验证码放到redis里
            //设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.DAYS);

            return R.ok();
        }else {
            return R.error().message("短信发送失败");
        }
    }
}
