package com.backend.system.basedata.user.dao.mapper;

import com.backend.system.basedata.user.dao.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> list();
    void insert(User user);
}
