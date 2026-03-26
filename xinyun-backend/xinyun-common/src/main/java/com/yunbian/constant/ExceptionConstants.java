package com.yunbian.constant;

/**
 * 异常信息常量类
 */
public class ExceptionConstants {


    public static final String USER_STATUS_ERROR = "用户状态异常";

    private ExceptionConstants() {}

    public static final int UNAUTHORIZED = 401;

    // ========== 用户相关 ==========
    public static final String USER_NOT_FOUND = "用户不存在";
    public static final String USERNAME_EXISTS = "用户名已存在";
    public static final String PHONE_EXISTS = "手机号已被注册";
    public static final String LOGIN_ERROR = "用户名或密码错误";
    public static final String ACCOUNT_DISABLED = "账号已被禁用";
    public static final String NOT_LOGIN = "请先登录";
    public static final String REGISTER_FAILED = "注册失败";
    public static final String USERNAME_EMPTY = "用户名不能为空";
    public static final String PASSWORD_EMPTY = "密码不能为空";
    public static final String PHONE_ERROR = "手机号格式不正确";
    public static final String USERNAME_LENGTH_ERROR = "用户名长度必须在 3-20 位之间";
    public static final String BIRTHDAY_FUTURE = "生日不能是未来时间";
    public static final String BIRTHDAY_AGE_LIMIT = "年龄必须满 18 岁";
    public static final String BIRTHDAY_OUT_OF_RANGE = "生日超出合理范围";
    public static final String PHONE_EMPTY = "手机号不能为空";
    public static final String USER_NOT_FOUND_LOGIN = "用户名不存在";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String PASSWORD_LENGTH_ERROR = "密码长度必须在 6-20 位之间";
    public static final String CAPTCHA_EMPTY = "验证码不能为空";
    public static final String CAPTCHA_EXPIRED = "验证码已过期";
    public static final String CAPTCHA_ERROR = "验证码错误";
    public static final String TOKEN_INVALID = "Token 已失效";

    // ========== 通用相关 ==========
    public static final String SYSTEM_ERROR = "系统繁忙，请稍后重试";
    public static final String PARAM_ERROR = "参数错误";
}
