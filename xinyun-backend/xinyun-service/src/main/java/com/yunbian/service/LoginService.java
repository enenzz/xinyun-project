package com.yunbian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunbian.dto.LoginDTO;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.vo.LoginVO;

public interface LoginService extends IService<User> {

    /**
     * 用户注册
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录响应（包含 Token、Refresh Token、有效期和用户信息）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 生成并发送验证码
     * @param username 用户名
     * @return 验证码 VO
     */
    String generateCaptcha(String username);
}
