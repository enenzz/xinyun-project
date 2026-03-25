package com.yunbian.controller;

import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.result.Result;
import com.yunbian.service.LoginService;
import com.yunbian.vo.CaptchaVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public Result login(@RequestBody Map<String, String> params) {
        log.info("开始登录.....");
        
        String username = params.get("username");
        String password = params.get("password");
        
        User user = loginService.login(username, password);
        
        return Result.success(user);
    }

    @GetMapping("/captcha")
    public Result<CaptchaVO> captcha(@RequestParam("phone") String phone) {
        log.info("获取验证码请求 - 手机号：{}", phone);
        
        CaptchaVO captchaVO = loginService.generateCaptcha(phone);
        
        return Result.success(captchaVO);
    }

    private Result logout() {
        log.info("退出登录");

        return Result.success("退出登录");
    }
}
