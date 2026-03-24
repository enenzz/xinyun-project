package com.yunbian.controller;

import com.yunbian.context.UserContext;
import com.yunbian.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * 测试拦截器是否生效
     * 如果拦截器生效，能从 UserContext 获取到用户信息
     */
    @GetMapping("/test-interceptor")
    public Result testInterceptor() {
        Long userId = UserContext.getUserId();
        String username = UserContext.getUsername();
        
        log.info("测试拦截器 - userId={}, username={}", userId, username);
        
        if (userId == null) {
            return Result.error("拦截器未生效，UserContext 中无用户信息");
        }
        
        return Result.success(new TestUserVO(userId, username));
    }
    
    /**
     * 测试 DTO 类
     */
    private record TestUserVO(Long userId, String username) {}
}
