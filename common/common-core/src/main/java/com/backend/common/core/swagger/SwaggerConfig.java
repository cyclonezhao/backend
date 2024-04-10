package com.backend.common.core.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket ( DocumentationType.OAS_30 )
                //资源
                .globalResponses ( HttpMethod.GET, new ArrayList<>( ) )
                .globalResponses ( HttpMethod.PUT, new ArrayList <> ( ) )
                .globalResponses ( HttpMethod.POST, new ArrayList <> ( ) )
                .globalResponses ( HttpMethod.DELETE, new ArrayList <> ( ) )
                //是否启动
                .enable ( true )
                //头部信息
                .apiInfo ( apiInfo ( ) )
                .select ( )
                .apis (
                        RequestHandlerSelectors.basePackage ( "com.backend" )
                )
                //加了ApiOperation注解的类，才生成接口文档
                .apis ( RequestHandlerSelectors.withClassAnnotation ( Api.class ) )
                .apis ( RequestHandlerSelectors.withMethodAnnotation ( ApiOperation.class ) )
                .build ( )
                //协议
                .protocols ( new LinkedHashSet<>( Arrays.asList ( "https", "http" ) ))
                // 加了这个，swagger页面才会出现Authorize菜单
                .securitySchemes ( Collections.singletonList ( new ApiKey( "token", "token", "header" ) ) )
                // 加了这个，每个请求才会自动加上请求头token
                .securityContexts ( securityContexts ( ) );
    }

    /**
     * 授权信息全局应用
     */
    private List<SecurityContext> securityContexts() {
        return Collections.singletonList (
                SecurityContext.builder ( )
                        .securityReferences (
                                Collections.singletonList ( new SecurityReference( "token",
                                        new AuthorizationScope[]{new AuthorizationScope ( "global", "" )}
                                ) ) )
                        .build ( )
        );
    }

    /**
     * API 页面上半部分展示信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder( )
                .title ( "backend微服务接口文档" )
                .description ( "@author backend" )
                .contact ( new Contact( "backend", "http://www.backend.com", "backend@qq.com" ) )
                .version ( "1.0" )
                .build ( );
    }
}
