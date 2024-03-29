package com.backend.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    /**
     * 获取验证码
     * @return
     */
    @GetMapping("/getCode")
    public ResponseEntity getCode() {
        return null;
    }
}
