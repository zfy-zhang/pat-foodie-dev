package com.pat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: Swagger2 接口文档类
 * @Author 不才人
 * @Create Date 2020/5/9 1:38 下午
 * @Modify
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    // http://localhost:8088/swagger-ui.html
    // http://localhost:8088/doc.html
    // 配置 swagger2 核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // 制指定 api 类型为 SWAGGER_2
                    .apiInfo(apiInfo())                 // 定义 API 文档信息
                    .select().apis(RequestHandlerSelectors.basePackage("com.pat.controller"))  // 指定controller包
                    .paths(PathSelectors.any())
                    .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pat 电商平台接口api") //文档页标题
                .contact(new Contact("pat", "https://wwww.pat.com", "pat@163.com")) // 联系人信息
                .description("为Pat 电商平台提供的 API 文档") // 详细信息
                .version("1.0.1")       //文档版本号
                .termsOfServiceUrl("https://wwww.pat.com") // 网站地址
                .build();
    }
}
