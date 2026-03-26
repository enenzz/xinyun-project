package com.yunbian.dto;

import lombok.Data;

/**
 * 用户登录 DTO
 */
@Data
public class LoginDTO {

    /**
     * 用户名/手机号/邮箱
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 验证码
     */
    private String captcha;
}
