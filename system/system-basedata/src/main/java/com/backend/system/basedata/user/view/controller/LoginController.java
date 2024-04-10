package com.backend.system.basedata.user.view.controller;

import com.backend.system.basedata.user.service.LoginService;
import com.backend.system.basedata.user.view.vo.LoginForm;
import com.backend.system.basedata.user.view.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "登录管理")
public class LoginController {
    @Resource
    private LoginService loginService;

    @GetMapping("/getCode")
    @ApiOperation(value = "获取验证码")
    public ResponseEntity getCode() {
        return ResponseEntity.ok(loginService.getVerifyCode());
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录")
    public ResponseEntity<LoginUser> login(@RequestBody LoginForm loginForm) {
        LoginUser loginUser = loginService.login(loginForm);
        return ResponseEntity.ok(loginUser);
    }
}
