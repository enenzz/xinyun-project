package com.yunbian.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 验证码响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO {

    /**
     * 验证码
     */
    private String captcha;
}
