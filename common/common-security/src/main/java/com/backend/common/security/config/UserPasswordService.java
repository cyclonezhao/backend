package com.backend.common.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UserPasswordService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 从DB查询用户信息，主要是用户名和密码
        // 权限集合
        List<GrantedAuthority> authorities = new ArrayList<>( );

        return new User(
                "zhangsan",
                "$2a$10$W4KmpQQDfkZqbzaQBok2XuvjxkAWWqb/T.HTOc1ZV5/QyotwgFxYq",
                authorities
        );
    }
}
