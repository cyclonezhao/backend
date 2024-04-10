package com.backend.system.gateway.security;

import cn.hutool.core.util.StrUtil;
import com.backend.common.redis.RedisHelper;
import com.backend.common.security.gatewayandothers.TokenHelper;
import com.backend.common.security.gatewayandothers.constant.CacheName;
import com.backend.common.security.gatewayandothers.constant.ErrorConstant;
import com.backend.common.security.gatewayandothers.constant.LogicConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Resource
    private TokenHelper tokenHelper;
    @Resource
    private RedisHelper redisHelper;
    @Resource
    private ObjectMapper mapper;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse ( );
        //验证为未登陆状态会进入此方法，认证错误
        String errMsg;
        String token = tokenHelper.resolveToken ( exchange.getRequest ( ) );
        if ( StrUtil.isEmpty ( token ) ) { // 没有token抛出的异常
            errMsg = ErrorConstant.LOGIN_ERROR_NOT_LOGIN_IN;
            response.setStatusCode ( HttpStatus.UNAUTHORIZED );
        } else { // token过期抛出的异常
            Object accessTokenObj = redisHelper.get ( CacheName.USER_CACHE_TOKEN, token );
            if ( accessTokenObj != null && accessTokenObj.toString ( ).equals ( LogicConstant.KICK_OUT ) ) {
                redisHelper.delete ( CacheName.USER_CACHE_TOKEN, token );
                redisHelper.delete ( CacheName.USER_CACHE_ONLINE_USERS, token );
                errMsg = ErrorConstant.LOGIN_ERROR__KICK_OUT_LOGGED_IN_ELSEWHERE;
            } else {
                errMsg = ErrorConstant.LOGIN_ERROR_EXPIRED;
            }
            response.setStatusCode ( HttpStatus.REQUEST_TIMEOUT );
        }
        response.getHeaders ( ).setContentType ( MediaType.APPLICATION_JSON );
        log.error ( errMsg );
        try {
            DataBuffer buffer = response.bufferFactory ( ).wrap ( mapper.writeValueAsBytes ( errMsg ) );
            return response.writeWith ( Mono.just ( buffer ) ).doOnError ( (error) -> {
                DataBufferUtils.release ( buffer );
            } );
        } catch (JsonProcessingException e) {
            return Mono.error ( e );
        }
    }
}
