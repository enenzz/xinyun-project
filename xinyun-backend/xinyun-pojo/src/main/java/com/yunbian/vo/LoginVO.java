package com.yunbian.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 VO（单 Token）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    /**
     * JWT 访问令牌
     */
    private String token;

    /**
     * token 有效期（秒）
     */
    private Long expiresIn;

    /**
     * 用户信息
     */
    private UserVO userVO;
}
