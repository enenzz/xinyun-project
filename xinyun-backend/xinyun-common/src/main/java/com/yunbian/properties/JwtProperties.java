package com.yunbian.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * JWT 密钥
     */
    private String secretKey;

    /**
     * Access Token 过期时间（毫秒）
     */
    private Long accessExpirationTime; //2小时

    /**
     * Refresh Token 过期时间（毫秒）
     */
    private Long refreshExpirationTime; // 3天
}
