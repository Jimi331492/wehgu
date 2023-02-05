package com.wehgu.admin.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author isMiaoA
 */
@Configuration
public class SwaggerConfig {

    /**
     * 是否开启swagger配置，生产环境需关闭
     */
    @Value("${knife4j.enable}")
    private boolean enable;

    /**
     * 创建API
     * http:IP:端口号/swagger-ui/index.html 原生地址
     * http:IP:端口号/doc.html bootStrap-UI地址
     */
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.OAS_30)
                .pathMapping("/")
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .enable(enable)
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
                // .apis(RequestHandlerSelectors.basePackage("com.doctorcloud.product.web.controller"))
                // 扫描所有
                .apis(RequestHandlerSelectors.any())
                // 忽略以"/ApiError"开头的路径,可以防止显示如404错误接口
                .paths(PathSelectors.regex("(?!/ApiError.*).*"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());


    }

    private List securitySchemes() {

        List apiKeyList= new ArrayList();
        apiKeyList.add(new ApiKey("X-Token", "X-Token", "header"));
        return apiKeyList;

    }

    private List securityContexts() {
        List securityContexts=new ArrayList<>();

        securityContexts.add(

                SecurityContext.builder()

                        .securityReferences(defaultAuth())

                        .forPaths(PathSelectors.regex("^(?!auth).*$"))

                        .build());

        return securityContexts;

    }

    List defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");

        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];

        authorizationScopes[0] = authorizationScope;

        List securityReferences=new ArrayList<>();

        securityReferences.add(new SecurityReference("X-Token", authorizationScopes));

        return securityReferences;

    }




    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title("服务接口文档")
                // 描述
                .description("相关操作接口")
                // 作者信息
                .contact(new Contact("isMiaoA", "my3iao.com", "1592043271@qq.com"))
                // 版本
                .version("版本号:V.1")
                //协议
                .license("Apache LICENSE 2.0")
                .build();
    }


}
