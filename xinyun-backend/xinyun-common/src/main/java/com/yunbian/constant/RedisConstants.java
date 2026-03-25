package com.yunbian.constant;

/**
 * Redis Key 常量类
 * 用于统一管理 Redis 键名前缀，避免硬编码
 */
public class RedisConstants {

    private RedisConstants() {}

    // ========== 项目前缀 ==========
    public static final String KEY_PREFIX = "xinyun:";

    // ========== 验证码相关 ==========
    /**
     * 短信验证码 key
     * 格式：xinyun:captcha:手机号
     * 示例：xinyun:captcha:13800138000
     */
    public static final String CAPTCHA_USERNAME = KEY_PREFIX + "login:captcha:";

    // ========== 用户相关 ==========
    /**
     * 用户 Token key
     * 格式：xinyun:user:token:用户 ID
     * 示例：xinyun:user:token:123
     */
    public static final String USER_TOKEN = KEY_PREFIX + "user:token:";

    /**
     * 用户信息 key
     * 格式：xinyun:user:info:用户 ID
     * 示例：xinyun:user:info:123
     */
    public static final String USER_INFO = KEY_PREFIX + "user:info:";

    // ========== 通用前缀 ==========
    /**
     * 获取完整的 Redis key
     * @param prefix key 前缀
     * @param identifier 唯一标识（如用户 ID、手机号等）
     * @return 完整的 Redis key
     */
    public static String buildKey(String prefix, Object identifier) {
        return prefix + identifier.toString();
    }
}
