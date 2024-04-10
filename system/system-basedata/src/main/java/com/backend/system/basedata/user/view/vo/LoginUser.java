package com.backend.system.basedata.user.view.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {
    private String token;
    private String loginTime;
}
