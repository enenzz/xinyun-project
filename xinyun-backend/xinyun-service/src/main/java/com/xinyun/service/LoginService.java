package com.xinyun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinyun.dto.RegisterDTO;
import com.xinyun.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface LoginService extends IService<User> {

    /**
     * 用户注册
     * @param registerDTO
     */
    void register(RegisterDTO registerDTO);
}
