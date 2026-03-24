package com.yunbian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunbian.entity.User;
import com.yunbian.mapper.UserMapper;
import com.yunbian.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
