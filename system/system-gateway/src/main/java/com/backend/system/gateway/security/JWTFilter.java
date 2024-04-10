package com.backend.system.gateway.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.backend.common.redis.RedisHelper;
import com.backend.common.security.gatewayandothers.TokenHelper;
import com.backend.common.security.gatewayandothers.constant.CacheName;
import com.backend.common.security.gatewayandothers.constant.LogicConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class JWTFilter implements WebFilter, Ordered {
    private TokenHelper tokenHelper = SpringUtil.getBean(TokenHelper.class);
    private RedisHelper redisHelper = SpringUtil.getBean(RedisHelper.class);

    private long EXPIRE_TIME = Long.parseLong(SpringUtil.getProperty("jwt.accessToken.expireTime"));

    @Override
    public int getOrder() {
        return -1000;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = tokenHelper.resolveToken ( exchange.getRequest ( ) );

        String accessToken = null;
        if ( StrUtil.isNotEmpty ( token ) ) {
            String loginName = tokenHelper.getLoginName ( token );
            if ( StrUtil.isNotEmpty ( loginName ) ) {
                Object accessTokenObj = redisHelper.get ( CacheName.USER_CACHE_TOKEN, token );
                if ( accessTokenObj != null ) {
                    accessToken = accessTokenObj.toString ( );
                    // 刷新token有效时长
                    redisHelper.expire ( CacheName.USER_CACHE_TOKEN, token, EXPIRE_TIME);
                    redisHelper.expire ( CacheName.USER_CACHE_ONLINE_USERS, token, EXPIRE_TIME);
                }
            }
        }

        boolean isLogin = StrUtil.isNotBlank ( accessToken ) && !accessToken.equals (LogicConstant.KICK_OUT );

        if ( isLogin && this.tokenHelper.validateToken ( token, accessToken ) ) {
            String loginName = this.tokenHelper.getLoginName ( accessToken );
            List<GrantedAuthority> authorities = new ArrayList<>( );
            User principal = new User( loginName, "", authorities );
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, token, authorities);

            SecurityContextHolder.getContext ( ).setAuthentication ( authentication );
            return chain.filter ( exchange ).contextWrite ( ReactiveSecurityContextHolder.withAuthentication ( authentication ) );
        }
        return chain.filter ( exchange );
    }
}
