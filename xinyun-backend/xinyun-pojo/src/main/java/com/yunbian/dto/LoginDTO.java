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
     * 密码（MD5 加密后传输）
     */
    private String password;

    /**
     * 验证码（如需开启）
     */
    private String captcha;
}
