package com.yunbian.dto;

import lombok.Data;

import java.math.BigDecimal;
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
     * 密码（MD5 加密后传输）
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像 URL
     */
    private String avatarUrl;

    /**
     * 性别：0-未知 1-男 2-女
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
     * 经度（用于附近的人功能，可为空）
     */
    private BigDecimal longitude;

    /**
     * 纬度（用于附近的人功能，可为空）
     */
    private BigDecimal latitude;

    /**
     * 省（用于同城匹配，可为空）
     */
    private String province;

    /**
     * 市（用于同城匹配，可为空）
     */
    private String city;

    /**
     * 区/县（可选，可为空）
     */
    private String district;
}
