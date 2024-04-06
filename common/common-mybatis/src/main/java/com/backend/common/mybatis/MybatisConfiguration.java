package com.backend.common.mybatis;

import com.backend.common.mybatis.interceptor.GenIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfiguration {
    @Bean
    public GenIdInterceptor myInterceptor() {
        GenIdInterceptor mybatisInterceptor = new GenIdInterceptor();
        return mybatisInterceptor;
    }
}
