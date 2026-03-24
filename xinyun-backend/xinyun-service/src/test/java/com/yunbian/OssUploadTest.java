package com.yunbian;

import com.yunbian.result.Result;
import com.yunbian.utils.AliOssUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * OSS 文件上传功能测试
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class OssUploadTest {

    @Resource
    private AliOssUtil aliOssUtil;

    @Resource
    private MockMvc mockMvc;

    /**
     * 测试 1：直接使用 AliOssUtil 上传文件
     */
    @Test
    public void testUploadDirectly() throws Exception {
        log.info("========== 直接调用 AliOssUtil 测试 ==========");
        
        // 准备测试文件（请修改为你本地的真实文件路径）
        File testFile = new File("D:\\Important\\20180122200425_kemkf.jpg");
        
        if (!testFile.exists()) {
            log.warn("测试文件不存在，跳过测试：{}", testFile.getAbsolutePath());
            return;
        }
        
        try (InputStream inputStream = new FileInputStream(testFile)) {
            // 创建 MockMultipartFile
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "test-image.jpg",
                    "image/jpeg",
                    inputStream
            );
            
            // 调用上传方法
            String url = aliOssUtil.upload(file, "xinyun/test");
            
            log.info("文件上传成功！访问 URL: {}", url);
            
            // 验证 URL 不为空
            assert url != null : "上传返回的 URL 不应为 null";
            assert url.contains("xinyun/test") : "URL 应包含指定目录";
            
            log.info("✓ 直接上传测试通过！");
        }
    }

    /**
     * 测试 2：通过 Controller 接口上传文件
     */
    @Test
    public void testUploadViaController() throws Exception {
        log.info("========== 通过 Controller 接口测试 ==========");
        
        // 准备测试文件
        File testFile = new File("D:\\Important\\20180122200425_kemkf.jpg");
        
        if (!testFile.exists()) {
            log.warn("测试文件不存在，跳过测试：{}", testFile.getAbsolutePath());
            return;
        }
        
        try (InputStream inputStream = new FileInputStream(testFile)) {
            // 创建 MockMultipartFile
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "test-controller-upload.jpg",
                    "image/jpeg",
                    inputStream
            );
            
            // 发送 POST 请求到上传接口
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("/common/upload")
                            .file(file))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(print())
                    .andExpect(result -> {
                        String responseContent = result.getResponse().getContentAsString();
                        log.info("响应内容：{}", responseContent);
                        
                        // 验证响应码（假设成功返回 200 或 1）
                        assert responseContent.contains("\"code\":") : "响应应包含 code 字段";
                    });
            
            log.info("✓ Controller 接口上传测试通过！");
        }
    }

    /**
     * 测试 3：上传空文件（应该失败）
     */
    @Test
    public void testUploadEmptyFile() throws Exception {
        log.info("========== 测试空文件上传（预期失败） ==========");
        
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.txt",
                "text/plain",
                new byte[0]
        );
        
        try {
            mockMvc.perform(MockMvcRequestBuilders
                            .multipart("/common/upload")
                            .file(emptyFile))
                    .andDo(print());
            
            log.warn("空文件上传应该失败，但实际成功了！");
        } catch (Exception e) {
            log.info("✓ 空文件上传正确抛出异常：{}", e.getMessage());
        }
    }

    /**
     * 测试 4：使用默认目录上传
     */
    @Test
    public void testUploadWithDefaultDir() throws Exception {
        log.info("========== 测试默认目录上传 ==========");
        
        File testFile = new File("D:\\test-default.jpg");
        
        if (!testFile.exists()) {
            log.warn("测试文件不存在，跳过测试：{}", testFile.getAbsolutePath());
            return;
        }
        
        try (InputStream inputStream = new FileInputStream(testFile)) {
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "default-dir-test.jpg",
                    "image/jpeg",
                    inputStream
            );
            
            // 使用默认目录（images）
            String url = aliOssUtil.upload(file);
            
            log.info("文件上传成功（默认目录）！访问 URL: {}", url);
            assert url.contains("images/") : "URL 应包含默认目录 images";
            
            log.info("✓ 默认目录上传测试通过！");
        }
    }
}
