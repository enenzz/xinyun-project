package com.yunbian.constant;

/**
 * 异常信息常量类
 */
public class ExceptionConstants {

    private ExceptionConstants() {}

    // ========== 用户相关 ==========
    public static final String USER_NOT_FOUND = "用户不存在";
    public static final String USERNAME_EXISTS = "用户名已存在";
    public static final String PHONE_EXISTS = "手机号已被注册";
    public static final String LOGIN_ERROR = "用户名或密码错误";
    public static final String ACCOUNT_DISABLED = "账号已被禁用";
    public static final String NOT_LOGIN = "用户未登录";
    public static final String REGISTER_FAILED = "注册失败";

    // ========== 通用相关 ==========
    public static final String SYSTEM_ERROR = "系统繁忙，请稍后重试";
    public static final String PARAM_ERROR = "参数错误";
}
