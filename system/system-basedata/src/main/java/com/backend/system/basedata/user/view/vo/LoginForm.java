package com.backend.system.basedata.user.view.vo;

import lombok.Data;

//@ApiModel("登录表单对象")
@Data
public class LoginForm {
    /**
     * 用户名
     */
//    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
//    @ApiModelProperty("密码")
    private String password;

    /**
     * 验证码
     */
//    @ApiModelProperty("验证码")
    private String code;

    /**
     * uuid
     */
//    @ApiModelProperty("验证码对应的唯一UUID")
    private String uuid;
}
