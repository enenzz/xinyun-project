package com.yunbian;

import com.yunbian.properties.AliOssProperties;
import com.yunbian.utils.AliOssUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 测试 AliOss 配置注入
 */
@Slf4j
@SpringBootTest
public class AliOssConfigTest {

    @Resource
    private AliOssProperties aliOssProperties;

    @Resource
    private AliOssUtil aliOssUtil;

    @Test
    public void testAliOssPropertiesInjection() {
        log.info("========== AliOssProperties 注入测试 ==========");
        log.info("endpoint: {}", aliOssProperties.getEndpoint());
        log.info("accessKeyId: {}", aliOssProperties.getAccessKeyId());
        log.info("bucketName: {}", aliOssProperties.getBucketName());
        
        assert aliOssProperties.getEndpoint() != null : "endpoint 不应为 null";
        assert aliOssProperties.getAccessKeyId() != null : "accessKeyId 不应为 null";
        assert aliOssProperties.getBucketName() != null : "bucketName 不应为 null";
        
        log.info("✓ AliOssProperties 注入成功！");
    }

    @Test
    public void testAliOssUtilInjection() {
        log.info("========== AliOssUtil 注入测试 ==========");
        assert aliOssUtil != null : "AliOssUtil 不应为 null";
        log.info("✓ AliOssUtil 注入成功！");
    }
}
