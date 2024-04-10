package com.backend.system.basedata.user.view.controller;

import com.backend.system.basedata.user.service.LoginService;
import com.backend.system.basedata.user.view.vo.LoginForm;
import com.backend.system.basedata.user.view.vo.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Resource
    private LoginService loginService;

    @GetMapping("/getCode")
    public ResponseEntity getCode() {
        return ResponseEntity.ok(loginService.getVerifyCode());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUser> login(@RequestBody LoginForm loginForm) {
        LoginUser loginUser = loginService.login(loginForm);
        return ResponseEntity.ok(loginUser);
    }
}
