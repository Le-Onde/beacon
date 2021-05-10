package com.shida.eduservice.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.shida.eduservice.mapper")
public class eduConfig {
    /*
     * @Author liu-miss
     * @Description //TODO 逻辑删除插件配置
     * @Date 16:26 2021/4/29
     * @Param
     * @return
     **/
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }


    /*
     * @Author liu-miss
     * @Description //TODO 分页插件
     * @Date 18:14 2021/4/29
     * @Param
     * @return
     **/
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
