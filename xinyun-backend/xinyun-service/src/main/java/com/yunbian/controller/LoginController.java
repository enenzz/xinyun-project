package com.yunbian.controller;

import com.yunbian.dto.RegisterDTO;
import com.yunbian.result.Result;
import com.yunbian.service.LoginService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/login")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        log.info("开始注册.....");
        loginService.register(registerDTO);
        return Result.success("注册成功");
    }

    @PostMapping
    public Result login() {
        log.info("开始登录.....");

        return Result.success("登录成功");
    }

    public Result captcha() {
        log.info("获取验证码成功");

        return Result.success("获取验证码成功");
    }

    public Result logout() {
        log.info("退出登录");

        return Result.success("退出登录");
    }
}
