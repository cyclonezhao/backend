package com.backend.common.security.constant;

public interface ErrorConstant {
    String LOGIN_ERROR_INCORRECT = "用户名或者密码错误！";
    String LOGIN_ERROR_NOT_LOGIN_IN = "您尚未登录，请登录后操作!";
    String LOGIN_ERROR_EXPIRED = "您的登录已过期，请重新登录！";
    String LOGIN_ERROR__KICK_OUT_LOGGED_IN_ELSEWHERE = "您的账号在另一台设备上登录,如非本人操作，请立即修改密码！";
}
