package com.yunbian.controller;

import com.yunbian.exception.BusinessException;
import com.yunbian.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


/**
 * 公共接口类
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    // 图片存储目录 - 使用类路径下的 static/images 目录
    private String uploadPath = "src/main/resources/static/images";

    /**
     * 图片上传
     * @return
     */
    @PostMapping("/upload/image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        //1. 校验文件
        if (file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
            
        try {
            //2. 将文件保存到本地 images 目录下
            // 获取原始文件名
            String originalFilename = file.getOriginalFilename();
            // 获取文件后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成唯一文件名，防止重名
            String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
                
            // 创建上传目录（如果不存在）
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
                log.info("创建目录：{}", dir.getAbsolutePath());
            }
                
            // 保存文件到本地
            Path filePath = Paths.get(uploadPath, fileName);
            Files.write(filePath, file.getBytes());
                
            log.info("文件上传成功：{}", filePath.toString());
            log.info("文件绝对路径：{}", filePath.toAbsolutePath().toString());
                
            //3. 返回图片的 URL
            // Spring Boot 默认会映射 classpath:/static/ 目录下的资源
            String imageUrl = "http://localhost:8080/images/" + fileName;
            return Result.success(imageUrl);
                
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }
}
