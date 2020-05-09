package com.pat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Description 跨域配置信息
 * @Author 不才人
 * @Create Date 2020/5/9 2:44 下午
 * @Modify
 */
@Configuration
public class CorsConfig {

    public CorsConfig() {

    }

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 cors 配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:8080");

        // 设置是否发送 Cookie 等信息
        corsConfiguration.setAllowCredentials(true);

        // 设置允许请求的方式
        corsConfiguration.addAllowedMethod("*");

        // 设置允许的 header
        corsConfiguration.addAllowedHeader("*");

        // 2. 为 url 添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfiguration);

        // 3. 返回重新定义好的
        return new CorsFilter(corsSource);
    }

}
