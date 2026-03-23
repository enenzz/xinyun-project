package com.xinyun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinyun.dto.RegisterDTO;
import com.xinyun.entity.User;
import com.xinyun.exception.BusinessException;
import com.xinyun.mapper.UserMapper;
import com.xinyun.service.LoginService;
import org.springframework.beans.BeanUtils;

import static com.xinyun.constant.ExceptionConstants.REGISTER_FAILED;

public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    /**
     * 用户注册
     * @param registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(REGISTER_FAILED);
        }
        //TODO 判断用户名是否唯一
    }
}
