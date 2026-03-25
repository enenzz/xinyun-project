package com.yunbian.utils;

import java.util.regex.Pattern;

/**
 * 手机号校验工具类
 */
public class PhoneUtils {

    private PhoneUtils() {}

    /**
     * 中国大陆手机号正则表达式
     * 规则：1 开头，第二位 3-9，后面 9 位数字
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /**
     * 校验手机号格式是否正确
     * @param phone 手机号
     * @return true-格式正确，false-格式错误
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 校验手机号，如果格式不正确则抛出异常
     * @param phone 手机号
     * @throws IllegalArgumentException 手机号格式不正确时抛出
     */
    public static void validatePhone(String phone) {
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("手机号格式不正确：" + phone);
        }
    }
}
