package com.yunbian.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 刷新 Token 响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenVO {

    /**
     * 新的 JWT 访问令牌
     */
    private String token;

    /**
     * token 有效期（秒）
     */
    private Long expiresIn;
}
