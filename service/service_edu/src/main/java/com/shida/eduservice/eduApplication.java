package com.shida.eduservice;

import io.swagger.annotations.Api;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient //nacos 注册
@EnableFeignClients
@ComponentScan(basePackages = {"com.shida"})
public class eduApplication {

    public static void main(String[] args) {
        SpringApplication.run(eduApplication.class,args);
    }
}
