package com.yunbian.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class TokenException extends RuntimeException {

    public TokenException(String message) {
        super(message);
    }
}
