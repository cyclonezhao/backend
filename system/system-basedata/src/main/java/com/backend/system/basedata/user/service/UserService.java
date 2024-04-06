package com.backend.system.basedata.user.service;

import com.backend.system.basedata.user.dao.entity.User;
import com.backend.system.basedata.user.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public List<User> list() {
        return userMapper.list();
    }

    public void save(User user) {
        userMapper.insert(user);
    }
}
