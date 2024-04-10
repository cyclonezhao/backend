package com.backend.common.core.exception.handler;

import com.backend.common.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Configuration
public class CommonExceptionHandler {
    @ExceptionHandler({CommonException.class})
    public ResponseEntity handleCommonsException(CommonException e) {
        log.error ( "{}", e );
        return new ResponseEntity ( e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
