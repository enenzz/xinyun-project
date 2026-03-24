package com.yunbian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;

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
}
