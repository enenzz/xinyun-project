package com.yunbian.utils;

/**
 * 字符串校验工具类
 */
public class StrUtils {

    private StrUtils() {}

    /**
     * 判断字符串是否为空
     * 为空的情况：null、空字符串""、纯空格"   "
     * @param str 待校验的字符串
     * @return true-为空，false-不为空
     */
    public static boolean isBlank(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        // 检查是否全是空白字符（空格、制表符等）
        return str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空
     * @param str 待校验的字符串
     * @return true-不为空，false-为空
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 校验字符串，如果为空则抛出异常
     * @param str 待校验的字符串
     * @param message 异常信息
     * @throws IllegalArgumentException 当字符串为空时抛出
     */
    public static void validateNotBlank(String str, String message) {
        if (isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 去除字符串首尾空白，如果为 null 则返回空字符串
     * @param str 待处理的字符串
     * @return 处理后的字符串
     */
    public static String trimToEmpty(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }

    /**
     * 去除字符串首尾空白，如果为 null 或全空格则返回 null
     * @param str 待处理的字符串
     * @return 处理后的字符串，如果结果为空则返回 null
     */
    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
