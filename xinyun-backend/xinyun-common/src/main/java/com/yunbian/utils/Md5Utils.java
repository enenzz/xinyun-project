package com.yunbian.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类
 */
public class Md5Utils {

    /**
     * MD5 加密
     * @param str 待加密的字符串
     * @return 加密后的 32 位小写十六进制字符串
     */
    public static String encode(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte[] digest = messageDigest.digest();
            return toHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 加密异常", e);
        }
    }

    /**
     * 将字节数组转换为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 验证密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        String encodedRawPassword = encode(rawPassword);
        return encodedRawPassword.equals(encodedPassword);
    }
}
