package com.backend.system.basedata.user.view.controller;

import com.backend.system.basedata.user.dao.entity.User;
import com.backend.system.basedata.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    @ApiOperation(value = "查询用户列表")
    public ResponseEntity<List<User>> list(){
        List<User> userList = userService.list();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("save")
    @ApiOperation(value = "保存用户")
    public ResponseEntity save(@RequestBody() User user){
        userService.save(user);
        return ResponseEntity.ok("success");
    }
}
