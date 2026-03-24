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
}
