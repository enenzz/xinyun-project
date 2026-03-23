package com.xinyun.interceptor;

import com.xinyun.utils.JwtUtils;
import com.xinyun.context.UserContext;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取 Token
        String token = request.getHeader(TOKEN_HEADER);

        // 2. Token 为空，放行（由 @RequireLogin 注解或后续逻辑处理）
        if (token == null || token.isEmpty()) {
            return true;
        }

        // 3. 验证 Token 有效性
        if (!JwtUtils.isTokenValid(token)) {
            log.warn("无效的 Token: {}", token);
            return true;
        }

        // 4. 解析 Token，存入 UserContext
        Long userId = JwtUtils.getUserIdFromToken(token);
        String username = JwtUtils.getUsernameFromToken(token);
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
