package com.backend.system.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;

import javax.annotation.Resource;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@EnableWebSecurity  // 有了这个 securityMatcher 才起作用
public class SecurityConfiguration {
    /**
     * 放行白名单
     */
    @Value("${ignore.whiteList}")
    private String whiteList;

    @Resource
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    @RefreshScope
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher ( new NegatedServerWebExchangeMatcher( new OrServerWebExchangeMatcher(
                        pathMatchers ( whiteList.split ( "," ) ),
                        pathMatchers ( HttpMethod.OPTIONS, "/**" )
                ) ) )
                /*
                这里要注意，JWTFilter不能定义成Component，可能是因为它实现了WebFilter，会被自动加到过滤里，
                从而令上面的securityMatcher失效
                 */
                .addFilterAt ( new JWTFilter(), SecurityWebFiltersOrder.HTTP_BASIC )
                .exceptionHandling ( )
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and ( )
                .authorizeExchange ( )
                .anyExchange ( ).authenticated ( );
        return http.build();
    }
}
