package com.yunbian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.exception.BusinessException;
import com.yunbian.mapper.UserMapper;
import com.yunbian.service.LoginService;
import com.yunbian.utils.Md5Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.yunbian.constant.ExceptionConstants.REGISTER_FAILED;
import static com.yunbian.constant.ExceptionConstants.USERNAME_EXISTS;

@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    /**
     * 用户注册
     * @param registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        // 先判断用户名是否已存在
        String username = registerDTO.getUsername();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User existUser = this.getOne(queryWrapper);
        
        if (existUser != null) {
            throw new BusinessException(USERNAME_EXISTS);
        }
        
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);

        //插入默认昵称
        user.setNickname("xinyun" + UUID.randomUUID().toString().substring(0, 5));
        //user.setAvatar();
        if (user.getGender() == null) {
            user.setGender(0);
        }

        // 对密码进行 MD5 加密
        user.setPassword(Md5Utils.encode(registerDTO.getPassword()));

        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(REGISTER_FAILED);
        }
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息（不包含密码）
     */
    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);

        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }

        // 验证密码
        if (!Md5Utils.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 返回用户信息（不返回密码）
        user.setPassword(null);
        return user;
    }
}
