package com.backend.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取验证码
     * @return
     */
    @GetMapping("/getCode")
    public ResponseEntity getCode() {
        redisTemplate.opsForValue ( ).set ( "test1", "hahaha test1" );
        return ResponseEntity.ok(redisTemplate.opsForValue ( ).get("test1"));
    }
}
