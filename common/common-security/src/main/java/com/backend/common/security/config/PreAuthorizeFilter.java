package com.backend.common.security.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.backend.common.redis.RedisHelper;
import com.backend.common.security.gatewayandothers.TokenHelper;
import com.backend.common.security.gatewayandothers.constant.CacheName;
import com.backend.common.security.gatewayandothers.constant.LogicConstant;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a valid user is
 * found.
 */
public class PreAuthorizeFilter extends GenericFilterBean {


    private final TokenHelper tokenHelper = SpringUtil.getBean(TokenHelper.class);
    private final RedisHelper redisHelper = SpringUtil.getBean(RedisHelper.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = tokenHelper.resolveToken ( request );
        String accessToken = null;
        if ( StrUtil.isNotEmpty ( token ) ) {
            String loginName = tokenHelper.getLoginName ( token );
            if ( StrUtil.isNotEmpty ( loginName ) ) {
                Object accessTokenObj = redisHelper.get ( CacheName.USER_CACHE_TOKEN, token );
                if ( accessTokenObj != null ) {
                    accessToken = accessTokenObj.toString ( );
                }
            }
        }
        boolean needPermission = StrUtil.isNotBlank ( accessToken ) && !accessToken.equals (LogicConstant.KICK_OUT) && this.tokenHelper.validateToken ( accessToken );

        if ( needPermission ) {
            Object authentication = redisHelper.get (CacheName.USER_CACHE_AUTHENTICATION,  token );
            if ( authentication == null ) { // 需要登录

                // 权限集合
                List<GrantedAuthority> authorities = new ArrayList<>( );
                String loginName = this.tokenHelper.getLoginName ( accessToken );

                /* TODO 查询用户拥有的权限
                Set<String> permissions = SpringUtil.getBean ( IUserApi.class ).getPermissions ( userName );
                // 添加基于Permission的权限信息
                for (String permission : permissions) {
                    authorities.add ( new SimpleGrantedAuthority( permission ) );
                }
                 */
                authorities.add ( new SimpleGrantedAuthority( "a123456" ) );

                User principal = new User ( loginName, "", authorities );
                authentication = new UsernamePasswordAuthenticationToken( principal, token, authorities );

                redisHelper.set (CacheName.USER_CACHE_AUTHENTICATION,  token, authentication );

            }
            SecurityContextHolder.getContext ( ).setAuthentication ( (Authentication)authentication );
        }
        filterChain.doFilter ( servletRequest, servletResponse );
    }

}
