package com.yunbian.interceptor;

import com.yunbian.constant.ExceptionConstants;
import com.yunbian.exception.BusinessException;
import com.yunbian.exception.TokenException;
import com.yunbian.utils.JwtUtils;
import com.yunbian.context.UserContext;
import com.yunbian.utils.StrUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 从请求头中解析 Token 并将用户信息存入 ThreadLocal
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    public static final String TOKEN_HEADER = "Authorization";
    
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO 俩个token的疑问

        // 1. 从请求头获取 Token
        String token = request.getHeader(TOKEN_HEADER);

        // 2. Token 为空
        if (StrUtils.isBlank(token)) {
            throw new TokenException(ExceptionConstants.NOT_LOGIN);
        }

        // 3. 验证 Token 有效性
        if (!jwtUtils.isTokenValid(token)) {
            log.warn("无效的 Token: {}", token);
            throw new TokenException(ExceptionConstants.NOT_LOGIN);
        }

        // 4. 验证 Token 是否过期
        if (jwtUtils.isTokenExpired(token)) {
            log.warn("Token 已过期: {}", token);
            throw new TokenException(ExceptionConstants.NOT_LOGIN);
        }

        // 5. 解析 Token，存入 UserContext
        Long userId = jwtUtils.getUserIdFromToken(token);
        String username = jwtUtils.getUsernameFromToken(token);
        UserContext.setUser(userId, username);

        log.debug("用户登录信息已存入上下文: userId={}, username={}", userId, username);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后清除 ThreadLocal，防止内存泄漏
        UserContext.clear();
        log.debug("用户上下文已清除");
    }
}
