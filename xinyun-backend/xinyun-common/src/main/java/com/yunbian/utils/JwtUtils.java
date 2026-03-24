package com.yunbian.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
public class JwtUtils {

    /**
     * 密钥（实际项目中应该从配置文件读取）
     */
    private static final String SECRET_KEY = "xinyun-jwt-secret-key-2024-min-length-32-characters!";
    
    /**
     * 默认过期时间（毫秒）：7 天
     */
    private static final long DEFAULT_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 获取密钥对象
     */
    private static SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 创建 JWT Token
     *
     * @param userId 用户 ID
     * @param username 用户名
     * @return JWT Token
     */
    public static String createToken(Long userId, String username) {
        return createToken(userId, username, DEFAULT_EXPIRATION_TIME);
    }

    /**
     * 创建 JWT Token（自定义过期时间）
     *
     * @param userId 用户 ID
     * @param username 用户名
     * @param expirationTime 过期时间（毫秒）
     * @return JWT Token
     */
    public static String createToken(Long userId, String username, long expirationTime) {
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
    public static Claims parseToken(String token) {
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
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token
     * @return true-有效，false-无效
     */
    public static boolean isTokenValid(String token) {
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
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
