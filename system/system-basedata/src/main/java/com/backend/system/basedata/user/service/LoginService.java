package com.backend.system.basedata.user.service;

import cn.hutool.core.lang.Tuple;
import cn.hutool.extra.spring.SpringUtil;
import com.backend.common.core.exception.CommonException;
import com.backend.common.redis.RedisHelper;
import com.backend.common.security.gatewayandothers.TokenHelper;
import com.backend.common.security.gatewayandothers.constant.CacheName;
import com.backend.common.security.gatewayandothers.constant.ErrorConstant;
import com.backend.common.security.util.VerifyCodeUtil;
import com.backend.system.basedata.user.view.vo.LoginForm;
import com.backend.system.basedata.user.view.vo.LoginUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginService {
    @Resource
    private TokenHelper tokenHelper;
    @Resource
    private RedisHelper redisHelper;

    @Value("${jwt.accessToken.expireTime}")
    private long EXPIRE_TIME;

    public Map<String, Object> getVerifyCode(){
        Tuple tuple = VerifyCodeUtil.getRandomVerifyCode();//随机数字验证码
        String code = tuple.get(0);
        byte[] codeImg = tuple.get(1);

        String uuid = UUID.randomUUID ( ).toString ( );
        //将验证码放入session
        redisHelper.set ( CacheName.SYS_CACHE_VERIFY_CODE, uuid, code);
        redisHelper.expire ( CacheName.SYS_CACHE_VERIFY_CODE, uuid, 60 * 5 );

        Map response = new HashMap<>();
        response.put("codeImg", codeImg);
        response.put("uuid", uuid);
        return response;
    }

    public LoginUser login(LoginForm loginForm) {
        String username = loginForm.getUsername ( );
        String password = loginForm.getPassword ( );
        String code = loginForm.getCode ( );
        String uuid = loginForm.getUuid();

        // 校验验证码
        String correctCode = (String)redisHelper.get ( CacheName.SYS_CACHE_VERIFY_CODE, uuid );
        if ( !code.equals ( correctCode ) ) {
            throw new BadCredentialsException(ErrorConstant.LOGIN_ERROR_ERROR_VALIDATE_CODE);
        }

        // 通过 spring security 验证密码
        AuthenticationManager authenticationManager = SpringUtil.getBean ( AuthenticationManager.class );
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken ( username, password );
        Authentication authentication = authenticationManager.authenticate ( token );
        SecurityContextHolder.getContext ( ).setAuthentication ( authentication );

        // 单一设备登录判断

        // 生成token
        String accessToken = tokenHelper.createAccessToken(username);
        LoginUser loginUser = new LoginUser();
        loginUser.setToken(accessToken);

        // 缓存用户登录状态
        redisHelper.set ( CacheName.USER_CACHE_TOKEN, accessToken, accessToken );
        redisHelper.expire ( CacheName.USER_CACHE_TOKEN, accessToken, EXPIRE_TIME);
        redisHelper.set ( CacheName.USER_CACHE_ONLINE_USERS, accessToken, loginUser);
        redisHelper.expire ( CacheName.USER_CACHE_ONLINE_USERS, accessToken, EXPIRE_TIME);

        return loginUser;
    }
}
