package com.yunbian.controller;

import com.yunbian.constant.MessageConstants;
import com.yunbian.dto.LoginDTO;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.result.Result;
import com.yunbian.service.LoginService;
import com.yunbian.vo.LoginVO;
import com.yunbian.vo.RefreshTokenVO;
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

    @PostMapping("/refresh")
    public Result<RefreshTokenVO> refresh(@RequestParam("refreshToken") String refreshToken) {
        log.info("刷新 Token 请求");
        
        RefreshTokenVO refreshTokenVO = loginService.refreshToken(refreshToken);
        
        return Result.success(refreshTokenVO);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String authorization) {
        log.info("退出登录请求");
        
        loginService.logout(authorization);
        
        return Result.success(MessageConstants.LOGOUT_SUCCESS);
    }
}
