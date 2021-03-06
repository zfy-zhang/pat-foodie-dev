package com.pat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Project Name: pat-foodie-dev
 * File Name: Application
 * Package Name: com.pat
 * Author: elisha
 * Date: 2020/5/3 23:16
 * Copyright (c) 2020,All Rights Reserved.
 * Description：
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
// 扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.pat.mapper")
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.pat", "org.n3r.idworker"})
//@EnableTransactionManagement
@EnableScheduling       // 开启定时任务
@EnableRedisHttpSession // 开启使用 redis 作为 Spring Session
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
