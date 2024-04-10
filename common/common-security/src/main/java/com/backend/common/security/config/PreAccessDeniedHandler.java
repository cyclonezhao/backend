package com.backend.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class PreAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        //登陆状态下，权限不足执行该方法
        log.error ( "权限不足：" + e.getMessage ( ) );
        response.setStatus ( HttpStatus.FORBIDDEN.value ( ) );
        response.setCharacterEncoding ( "UTF-8" );
        response.setContentType ( "application/json; charset=utf-8" );
        PrintWriter printWriter = response.getWriter ( );
        printWriter.write ( "权限不足：" + e.getMessage ( ) );
        printWriter.flush ( );
    }
}
