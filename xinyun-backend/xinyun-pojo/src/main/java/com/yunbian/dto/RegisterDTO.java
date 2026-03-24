package com.yunbian.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户注册请求参数
 */
@Data
public class RegisterDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（MD5加密后传输）
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 性别: 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
