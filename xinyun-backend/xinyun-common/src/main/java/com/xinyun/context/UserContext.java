package com.xinyun.context;

/**
 * 用户上下文工具类
 * 基于ThreadLocal实现，用于在同一线程中传递用户信息
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> USERNAME = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取当前用户ID
     * @return 用户ID，未设置时返回null
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 设置当前用户名
     * @param username 用户名
     */
    public static void setUsername(String username) {
        USERNAME.set(username);
    }

    /**
     * 获取当前用户名
     * @return 用户名，未设置时返回null
     */
    public static String getUsername() {
        return USERNAME.get();
    }

    /**
     * 设置当前用户信息
     * @param userId 用户ID
     * @param username 用户名
     */
    public static void setUser(Long userId, String username) {
        USER_ID.set(userId);
        USERNAME.set(username);
    }

    /**
     * 清除当前线程的用户信息
     * 必须在请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID.remove();
        USERNAME.remove();
    }
}
