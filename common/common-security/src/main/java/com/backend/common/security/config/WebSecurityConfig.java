package com.backend.common.security.config;

import com.backend.common.security.gatewayandothers.TokenHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@Order(0)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级安全验证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserPasswordService userPasswordService;
    @Resource
    private PreAccessDeniedHandler accessDeniedHandler;

    /**
     * 描述：设置授权处理相关的具体类以及加密方式
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider( );
        // 设置密码加密算法
        provider.setPasswordEncoder ( passwordEncoder ( ) );
        provider.setUserDetailsService ( userPasswordService );
        auth.authenticationProvider ( provider );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .headers ( ).frameOptions ( ).disable ( ).and ( )
                .exceptionHandling ( ).accessDeniedHandler ( accessDeniedHandler ).and ( )
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf ( ).disable ( )
                .exceptionHandling ( ).and ( )
                // 基于token，所以不需要session
                .sessionManagement ( ).sessionCreationPolicy ( SessionCreationPolicy.STATELESS ).and ( )
                .authorizeRequests ( )
                .antMatchers ( "/**" ).permitAll ( ) // 允许请求无需认证
                .antMatchers ( HttpMethod.OPTIONS, "/**" ).permitAll ( )
                .anyRequest ( ).authenticated ( ) // 所有请求都需要验证
                .and ( )
//
                .apply ( securityConfigurerAdapter ( ) )
        ;
    }

    /**
     * 描述: 静态资源放行，这里的放行，是不走 Spring Security 过滤器链
     **/
    @Override
    public void configure(WebSecurity web) {
        // 可以直接访问的静态数据
        web.ignoring ( )
                .antMatchers (
                        "/doc.html",
                        "/swagger-ui.html",
                        "/userfiles/**",
                        "/static/**",
                        "/swagger**/**",
                        "/webjars/**" );
    }

    /**
     * 描述: 密码加密算法 BCrypt 推荐使用
     **/
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder( );
    }


    private PreConfigurer securityConfigurerAdapter() throws Exception {
        return new PreConfigurer ( );
    }

    /**
     * 描述: 注入AuthenticationManager管理器
     **/
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager ( );
    }
}
