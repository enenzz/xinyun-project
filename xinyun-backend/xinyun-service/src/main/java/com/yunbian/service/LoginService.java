package com.yunbian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunbian.dto.LoginDTO;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.vo.LoginVO;
import com.yunbian.vo.RefreshTokenVO;

public interface LoginService extends IService<User> {

    /**
     * 用户注册
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录响应（包含 Token、有效期和用户信息）
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 获取并发送验证码
     * @param username 用户名
     * @return 验证码
     */
    String getCaptcha(String username);

    /**
     * 刷新 Token
     * @param oldToken 旧的 token
     * @return 新的 token 和有效期
     */
    RefreshTokenVO refreshToken(String oldToken);

    /**
     * 退出登录
     * @param authorization 请求头中的 Authorization 值
     */
    void logout(String authorization);
}
