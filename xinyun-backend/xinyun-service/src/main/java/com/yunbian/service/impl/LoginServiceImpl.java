package com.yunbian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.exception.BusinessException;
import com.yunbian.mapper.UserMapper;
import com.yunbian.service.LoginService;
import com.yunbian.utils.AliOssUtil;
import com.yunbian.utils.Md5Utils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.yunbian.constant.ExceptionConstants.REGISTER_FAILED;
import static com.yunbian.constant.ExceptionConstants.USERNAME_EXISTS;

@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Resource
    private AliOssUtil aliOssUtil;

    /**
     * 静态内部类：将本地 File 文件转换为 MultipartFile
     */
    private static class FileMultipartFileAdapter implements MultipartFile {
        private final File file;
        private final String originalFilename;

        public FileMultipartFileAdapter(File file, String originalFilename) {
            this.file = file;
            this.originalFilename = originalFilename;
        }

        @Override
        public String getName() {
            return "file";
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            if (originalFilename == null) {
                return "application/octet-stream";
            }
            String lowerName = originalFilename.toLowerCase();
            if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (lowerName.endsWith(".png")) {
                return "image/png";
            } else if (lowerName.endsWith(".gif")) {
                return "image/gif";
            }
            return "application/octet-stream";
        }

        @Override
        public boolean isEmpty() {
            return file.length() == 0;
        }

        @Override
        public long getSize() {
            return file.length();
        }

        @Override
        public byte[] getBytes() throws IOException {
            return java.nio.file.Files.readAllBytes(file.toPath());
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            java.nio.file.Files.copy(file.toPath(), dest.toPath());
        }
    }

    /**
     * 用户注册
     * @param registerDTO
     */
    @Override
    public void register(RegisterDTO registerDTO) {
        // 先判断用户名是否已存在
        String username = registerDTO.getUsername();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User existUser = this.getOne(queryWrapper);
        
        if (existUser != null) {
            throw new BusinessException(USERNAME_EXISTS);
        }

        User user = new User();
        // 从 avatarUrl 中提取文件名（如果提供了头像 URL）
        String avatarUrl = registerDTO.getAvatarUrl();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            // 从完整 URL 中提取文件名
            // 例如：http://localhost:8080/images/abc123.jpg → abc123.jpg
            try {
                String fileName = avatarUrl.substring(avatarUrl.lastIndexOf('/') + 1);  // 获取文件名：abc123.jpg
                //从 images 目录下获取图片
                String localImagePath = "xinyun-service/src/main/resources/static/images/" + fileName;
                File localFile = new File(localImagePath);
                            
                if (localFile.exists()) {
                    //上传至 aliyun
                    MultipartFile imageFile = new FileMultipartFileAdapter(localFile, fileName);
                    String ossUrl = aliOssUtil.upload(imageFile, "xinyun/images/");
                    user.setAvatar(ossUrl);
                    log.info("头像上传至 OSS 成功：{}", ossUrl);
                } else {
                    log.warn("本地图片文件不存在，使用原始 URL: {}", localImagePath);
                    user.setAvatar(avatarUrl);
                }

            } catch (Exception e) {
                log.warn("解析头像 URL 失败，使用原始 URL: {}", avatarUrl);
                user.setAvatar(avatarUrl);
            }
        }

        BeanUtils.copyProperties(registerDTO, user);

        //插入默认昵称（如果前端未传递）
        if (user.getNickname() == null || user.getNickname().isEmpty()) {
            user.setNickname("xinyun" + UUID.randomUUID().toString().substring(0, 5));
        }
        
        //设置默认性别（如果前端未传递）
        if (user.getGender() == null) {
            user.setGender(0);
        }

        // 对密码进行 MD5 加密
        user.setPassword(Md5Utils.encode(registerDTO.getPassword()));

        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(REGISTER_FAILED);
        }
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户信息（不包含密码）
     */
    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);

        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException("用户名不存在");
        }

        // 验证密码
        if (!Md5Utils.matches(password, user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        // 返回用户信息（不返回密码）
        user.setPassword(null);
        return user;
    }
}
