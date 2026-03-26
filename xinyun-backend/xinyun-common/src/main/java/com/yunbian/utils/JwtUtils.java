package com.yunbian.utils;

import com.yunbian.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT 配置属性
     */
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 获取 JWT 配置属性
     * @return JWT 配置属性
     */
    public JwtProperties getJwtProperties() {
        return jwtProperties;
    }
    
    /**
     * 获取密钥对象
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 创建 JWT AccessToken
     * 过期时间为2小时
     * @param userId 用户 ID
     * @param username 用户名
     * @return JWT Token
     */
    public String createToken(Long userId, String username) {
        return createToken(userId, username, jwtProperties.getAccessExpirationTime());
    }

    /**
     * 创建 Refresh Token
     * 过期时间为3天
     * @param userId 用户 ID
     * @param username 用户名
     * @return Refresh Token
     */
    public String createRefreshToken(Long userId, String username) {
        return createToken(userId, username, jwtProperties.getRefreshExpirationTime());
    }

    /**
     * 创建 JWT Token（自定义过期时间）
     *
     * @param userId 用户 ID
     * @param username 用户名
     * @param expirationTime 过期时间（毫秒）
     * @return JWT Token
     */
    public String createToken(Long userId, String username, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析 JWT Token
     *
     * @param token JWT Token
     * @return Claims 包含声明信息的对象
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Token 中获取用户 ID（即使 token 已过期）
     * 用于退出登录等场景
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public Long getUserIdFromExpiredToken(String token) {
        try {
            return getUserIdFromToken(token);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // token 已过期，但从异常中获取 claims 信息
            Claims claims = e.getClaims();
            return claims.get("userId", Long.class);
        }
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token
     * @return true-有效，false-无效
     */
    public boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.error("Token 验证失败：{}", e.getMessage());
            return false;
        }
    }

    /**
     * 检查 Token 是否已过期
     *
     * @param token JWT Token
     * @return true-已过期，false-未过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
