package com.backend.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.common.redistemplate.RedisHelper;
import com.backend.common.security.constant.CacheName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class TokenHelper {
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

            Algorithm algorithm = Algorithm.HMAC256 ( SECRET_KEY );
            JWTVerifier verifier = JWT.require ( algorithm )
                    .withClaim ( "username", userName )
                    .build ( );
            verifier.verify ( accessToken );
            return true;
        } catch (TokenExpiredException e) {
            // token失效，执行刷新操作
            String newAccessToken = createAccessToken ( userName );
            redisHelper.set ( CacheName.USER_CACHE_TOKEN, token, newAccessToken );
            redisHelper.expire ( CacheName.USER_CACHE_TOKEN, token, EXPIRE_TIME );
            redisHelper.expire ( CacheName.USER_CACHE_ONLINE_USERS, token, EXPIRE_TIME );
            return true;
        } catch (Exception e) {
//            log.error ( ErrorConstants.LOGIN_ERROR_INCORRECT );
        }
        return false;
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
}
