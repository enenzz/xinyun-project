package com.yunbian;

import java.io.*;

import com.aliyun.oss.*;
import com.yunbian.properties.AliOssProperties;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OssJavaSdkQuickStart {

    @Resource
    private AliOssProperties aliOssProperties;

    @Test
    public void testOss() throws Exception {

        OSS ossClient = new OSSClientBuilder()
                .build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
        try {
            String bucketName = aliOssProperties.getBucketName();
            
            // 上传图片
            String objectName = "images/20180122200425_kemkf.jpg";  // OSS 中的文件路径
            // 使用绝对路径，把图片放到这个位置
            File localFile = new File("D:\\Important\\20180122200425_kemkf.jpg");
            
            ossClient.putObject(bucketName, objectName, localFile);
            System.out.println("图片上传成功！");
            
            // 拼接访问 URL
            String url = "https://" + bucketName + "." + aliOssProperties.getEndpoint().replace("https://", "") + "/" + objectName;
            System.out.println("访问地址：" + url);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}