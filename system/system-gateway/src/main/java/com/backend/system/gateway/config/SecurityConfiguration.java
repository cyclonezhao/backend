package com.backend.system.gateway.config;

import com.alibaba.nacos.common.utils.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

@EnableWebFluxSecurity
public class SecurityConfiguration {
    /**
     * 放行白名单
     */
    @Value("${ignore.whiteList}")
    private String whiteList;

    @Bean
    @RefreshScope
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .securityMatcher ( new NegatedServerWebExchangeMatcher( new OrServerWebExchangeMatcher(
                        pathMatchers ( whiteList.split ( "," ) ),
                        pathMatchers ( HttpMethod.OPTIONS, "/**" )
                ) ) )
                .authorizeExchange ( )
                .anyExchange ( ).authenticated ( );
        return http.build();
    }
}
