package com.yunbian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.vo.CaptchaVO;

public interface LoginService extends IService<User> {

    /**
     * 用户注册
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息（不包含密码）
     */
    User login(String username, String password);

    /**
     * 生成并发送验证码
     * @param phone 手机号
     * @return 验证码 VO
     */
    CaptchaVO generateCaptcha(String phone);
}
