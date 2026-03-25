package com.yunbian.controller;

import com.yunbian.dto.LoginDTO;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.result.Result;
import com.yunbian.service.LoginService;
import com.yunbian.vo.LoginVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("开始登录.....");
        
        LoginVO loginResponseVO = loginService.login(loginDTO);
        
        return Result.success(loginResponseVO);
    }

    @GetMapping("/captcha")
    public Result<String> captcha(@RequestParam("username") String username) {
        log.info("获取验证码请求 - 用户名：{}", username);
        
        String captcha = loginService.generateCaptcha(username);
        
        return Result.success(captcha);
    }

    private Result logout() {
        log.info("退出登录");

        return Result.success("退出登录");
    }
}
