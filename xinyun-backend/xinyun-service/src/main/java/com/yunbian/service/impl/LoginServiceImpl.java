package com.yunbian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.exception.BusinessException;
import com.yunbian.mapper.UserMapper;
import com.yunbian.service.LoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.yunbian.constant.ExceptionConstants.REGISTER_FAILED;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    /**
     * 用户注册
     * @param registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);

        //插入默认昵称
        user.setNickname("xinyun" + UUID.randomUUID().toString().substring(0, 5));
        //user.setAvatar();
        if (user.getGender() == null) {
            user.setGender(0);
        }

        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(REGISTER_FAILED);
        }
        //TODO 判断用户名是否唯一
    }
}
