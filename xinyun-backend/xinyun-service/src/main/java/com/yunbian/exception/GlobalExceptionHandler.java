package com.yunbian.exception;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yunbian.constant.ExceptionConstants;
import com.yunbian.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }
    
    /**
     * 处理数据库唯一约束异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Void> handleDuplicateKeyException(DuplicateKeyException e) {
        log.warn("数据库唯一约束异常：{}", e.getMessage());
            
        // 根据异常信息判断是哪个字段重复
        String message = e.getMessage();
        if (message != null) {
            if (message.contains("uk_username") || message.contains("idx_username")) {
                return Result.error("用户名已存在");
            }
            if (message.contains("uk_phone")) {
                return Result.error("手机号已被使用");
            }
        }
            
        return Result.error("数据已存在，请勿重复提交");
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(ExceptionConstants.SYSTEM_ERROR);
    }
}
