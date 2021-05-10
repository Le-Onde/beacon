package com.shida.servicebase.exceptionhandler;


import com.shida.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    //全局异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message("错误:项目运行遇到异常,执行了全局异常处理...");
    }

    //特定异常
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public R error(NullPointerException e){
        e.printStackTrace();
        return R.error().message("错误:项目运行遇到异常,执行了空指针异常...");
    }

    //自定义异常
    @ExceptionHandler(LeOndeException.class)
    @ResponseBody
    public R error(LeOndeException e){
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
