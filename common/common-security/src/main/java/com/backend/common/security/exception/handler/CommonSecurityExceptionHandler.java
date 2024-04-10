package com.backend.common.security.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
@Configuration
public class CommonSecurityExceptionHandler {
    /**
     * 登录过期，或者用户名密码错误
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAuthenticationException(Throwable e) {

        String errMsg = e.getMessage ( );
        log.error ( "{}", e );
        return new ResponseEntity ( errMsg, HttpStatus.UNAUTHORIZED );
    }
}
