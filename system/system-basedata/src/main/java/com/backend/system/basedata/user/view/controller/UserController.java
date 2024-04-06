package com.backend.system.basedata.user.view.controller;

import com.backend.system.basedata.user.dao.entity.User;
import com.backend.system.basedata.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("list")
    public ResponseEntity<List<User>> list(){
        List<User> userList = userService.list();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("save")
    public ResponseEntity save(@RequestBody() User user){
        userService.save(user);
        return ResponseEntity.ok("success");
    }
}
