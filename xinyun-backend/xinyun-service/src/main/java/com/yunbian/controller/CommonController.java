package com.yunbian.controller;

import com.yunbian.exception.BusinessException;
import com.yunbian.result.Result;
import com.yunbian.utils.AliOssUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.yunbian.constant.MessageConstants.FILE_UPLOAD_SUCCESS;

/**
 * 公共接口类
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @return
     */
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        // 1. 校验文件
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        try {
            log.info("文件上传：{}", file.getOriginalFilename());
            aliOssUtil.upload(file, "xinyun/images/");
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
        return Result.success(FILE_UPLOAD_SUCCESS);
    }
}
