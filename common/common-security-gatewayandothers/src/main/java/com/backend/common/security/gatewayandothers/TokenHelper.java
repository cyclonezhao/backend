package com.backend.common.security.gatewayandothers;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.common.redis.RedisHelper;
import com.backend.common.security.gatewayandothers.constant.CacheName;
import com.backend.common.security.gatewayandothers.constant.ErrorConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TokenHelper {
    private static final String TOKEN = "token";
    private static final String SECRET_KEY = "@!^*FSE%32SDF(#$*%$43#$862@KVS@";

    @Value("${jwt.accessToken.expireTime}")
    private long EXPIRE_TIME;
    @Resource
    private RedisHelper redisHelper;

    /**
     * 校验token是否正确
     *
     * @param token 密钥
     * @return 是否正确
     */
    public boolean validateToken(String token, String accessToken) {
        String userName = getLoginName ( accessToken );
        try {
            validateToken0(userName, accessToken);
            return true;
        } catch (TokenExpiredException e) {
            // token失效，执行刷新操作
            String newAccessToken = createAccessToken ( userName );
            redisHelper.set ( CacheName.USER_CACHE_TOKEN, token, newAccessToken );
            redisHelper.expire ( CacheName.USER_CACHE_TOKEN, token, EXPIRE_TIME );
            redisHelper.expire ( CacheName.USER_CACHE_ONLINE_USERS, token, EXPIRE_TIME );
            return true;
        } catch (Exception e) {
            log.error(ErrorConstant.LOGIN_ERROR_INCORRECT);
        }
        return false;
    }

    // TODO 这个方法和上面的区别，后续研究
    public boolean validateToken(String token) {
        try {
            String userName = getLoginName ( token );
            validateToken0(userName, token);
            return true;
        } catch (TokenExpiredException e) {
            log.error ( ErrorConstant.LOGIN_ERROR_EXPIRED );
        } catch (Exception e) {
            log.error ( ErrorConstant.LOGIN_ERROR_INCORRECT );
        }
        return false;
    }

    private void validateToken0(String userName, String token) throws Exception{
        Algorithm algorithm = Algorithm.HMAC256 ( SECRET_KEY );
        JWTVerifier verifier = JWT.require ( algorithm )
                .withClaim ( "username", userName )
                .build ( );
        verifier.verify ( token );
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public String getLoginName(String token) {
        try {
            DecodedJWT jwt = JWT.decode ( token );
            return jwt.getClaim ( "username" ).asString ( );
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param username 用户名
     * @return 加密的token
     */
    public String createAccessToken(String username) {
        Date date = new Date ( System.currentTimeMillis ( ) + EXPIRE_TIME * 1000 );
        Algorithm algorithm = Algorithm.HMAC256 ( SECRET_KEY );
        // 附带username信息
        return JWT.create ( )
                .withClaim ( "username", username )
                .withExpiresAt ( date )
                .sign ( algorithm );
    }

    public String resolveToken(ServerHttpRequest request) {
        String token0 = request.getQueryParams ( ).getFirst ( TOKEN );
        String token1 = request.getHeaders ( ).getFirst ( TOKEN );
        HttpCookie token2 = request.getCookies ( ).getFirst ( TOKEN );
        if ( StrUtil.isNotBlank ( token0 ) ) {
            return token0;
        }
        if ( StrUtil.isNotBlank ( token1 ) ) {
            return token1;
        }
        if ( token2 != null && StrUtil.isNotBlank ( token2.getValue ( ) ) ) {
            return token2.getValue ( );
        }
        return null;
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String token0 = httpServletRequest.getParameter ( TOKEN );
        String token1 = httpServletRequest.getHeader ( TOKEN );
        Cookie token2 = ServletUtil.getCookie ( httpServletRequest, TOKEN );
        if ( StrUtil.isNotBlank ( token0 ) ) {
            return token0;
        }
        if ( StrUtil.isNotBlank ( token1 ) ) {
            return token1;
        }
        if ( token2 != null && StrUtil.isNotBlank ( token2.getValue ( ) ) ) {
            return token2.getValue ( );
        }
        return null;
    }
}
