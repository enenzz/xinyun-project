package com.yunbian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunbian.constant.ExceptionConstants;
import com.yunbian.constant.RedisConstants;
import com.yunbian.constant.SystemConstants;
import com.yunbian.context.UserContext;
import com.yunbian.dto.LoginDTO;
import com.yunbian.dto.RegisterDTO;
import com.yunbian.entity.User;
import com.yunbian.entity.UserLocation;
import com.yunbian.exception.BusinessException;
import com.yunbian.mapper.UserLocationMapper;
import com.yunbian.mapper.UserMapper;
import com.yunbian.service.LoginService;
import com.yunbian.utils.*;
import com.yunbian.vo.LoginVO;
import com.yunbian.vo.RefreshTokenVO;
import com.yunbian.vo.UserVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.yunbian.constant.ExceptionConstants.REGISTER_FAILED;

@Service
@Slf4j
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Resource
    private AliOssUtil aliOssUtil;
    @Resource
    private UserLocationMapper userLocationMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JwtUtils jwtUtils;


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
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        // 校验用户名
        if (StrUtils.isBlank(registerDTO.getUsername())) {
            throw new BusinessException(ExceptionConstants.USERNAME_EMPTY);
        }
    
        // 校验用户名长度
        if (registerDTO.getUsername().length() < 3 || registerDTO.getUsername().length() > 20) {
            throw new BusinessException(ExceptionConstants.USERNAME_LENGTH_ERROR);
        }
    
        // 校验用户名是否已存在
        LambdaQueryWrapper<User> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(User::getUsername, registerDTO.getUsername());
        User existUser = this.getOne(usernameWrapper);
    
        if (existUser != null) {
            throw new BusinessException(ExceptionConstants.USERNAME_EXISTS);
        }
    
        // 校验密码
        if (StrUtils.isBlank(registerDTO.getPassword())) {
            throw new BusinessException(ExceptionConstants.PASSWORD_EMPTY);
        }
    
        // 校验密码长度
        if (registerDTO.getPassword().length() < 6 || registerDTO.getPassword().length() > 20) {
            throw new BusinessException(ExceptionConstants.PASSWORD_LENGTH_ERROR);
        }
    
        // 校验手机号（如果提供了）
        String phone = registerDTO.getPhone();
        if (StrUtils.isNotBlank(phone)) {
            // 校验手机号格式
            if (!PhoneUtils.isValidPhone(phone)) {
                throw new BusinessException(ExceptionConstants.PHONE_ERROR);
            }
    
            // 校验手机号是否已存在
            LambdaQueryWrapper<User> phoneWrapper = new LambdaQueryWrapper<>();
            phoneWrapper.eq(User::getPhone, phone);
            User existPhoneUser = this.getOne(phoneWrapper);
                
            if (existPhoneUser != null) {
                throw new BusinessException(ExceptionConstants.PHONE_EXISTS);
            }
        }
                
        // 校验生日合理性（如果提供了生日）
        LocalDate birthday = registerDTO.getBirthday();
        if (birthday != null) {
            // 1. 不能是未来时间
            if (birthday.isAfter(LocalDate.now())) {
                throw new BusinessException(ExceptionConstants.BIRTHDAY_FUTURE);
            }
                    
            // 2. 年龄限制（可选，比如必须满 18 岁）
            int age = Period.between(birthday, LocalDate.now()).getYears();
            if (age < 18) {
                throw new BusinessException(ExceptionConstants.BIRTHDAY_AGE_LIMIT);
            }
                    
            // 3. 合理的年龄范围（比如 0-150 岁）
            LocalDate minBirthday = LocalDate.now().minusYears(150);
            if (birthday.isBefore(minBirthday)) {
                throw new BusinessException(ExceptionConstants.BIRTHDAY_OUT_OF_RANGE);
            }
        }
        
        // 校验通过后再处理头像上传
        User user = new User();
        if (StrUtils.isNotBlank(registerDTO.getAvatarUrl())) {
            String avatar = processAvatarUrl(registerDTO.getAvatarUrl());
            user.setAvatar(avatar);
        } else {
            // 默认头像
            user.setAvatar(SystemConstants.DEFAULT_AVATAR);
        }

        BeanUtils.copyProperties(registerDTO, user);

        //插入默认昵称（如果前端未传递）
        if (StrUtils.isBlank(user.getNickname())) {
            user.setNickname(SystemConstants.PROJECT_NAME + UUID.randomUUID().toString().substring(0, 5));
        }
        //设置默认性别（如果前端未传递）
        if (user.getGender() == null) {
            user.setGender(SystemConstants.DEFAULT_GENDER);
        }
        //设置账号状态
        user.setStatus(SystemConstants.USER_STATUS_NORMAL);

        // 对密码进行 MD5 加密
        user.setPassword(Md5Utils.encode(registerDTO.getPassword()));

        boolean result = this.save(user);
        if (!result) {
            throw new BusinessException(REGISTER_FAILED);
        }
                
        // 将用户地址插入用户地址表中（只有当提供了位置信息时才保存）
        if (StrUtils.isNotBlank(registerDTO.getProvince()) || 
            StrUtils.isNotBlank(registerDTO.getCity()) ||
            StrUtils.isNotBlank(registerDTO.getDistrict()) ||
            registerDTO.getLongitude() != null || 
            registerDTO.getLatitude() != null) {
            UserLocation userLocation = UserLocation.builder()
                    .userId(user.getId())
                    .longitude(registerDTO.getLongitude())
                    .latitude(registerDTO.getLatitude())
                    .province(StrUtils.trimToNull(registerDTO.getProvince()))
                    .city(StrUtils.trimToNull(registerDTO.getCity()))
                    .district(StrUtils.trimToNull(registerDTO.getDistrict()))
                    .build();
            
            userLocationMapper.insert(userLocation);
            log.info("用户位置信息已保存：userId={}", user.getId());
        } else {
            log.info("用户未提供位置信息，跳过保存：userId={}", user.getId());
        }
        
        log.info("用户注册成功：userId={}, username={}", user.getId(), user.getUsername());
    }

    /**
     * 返回 OSS URL
     * @param avatarUrl
     * @return
     */
    private String processAvatarUrl(String avatarUrl) {
        // 从完整 URL 中提取文件名
        // 例如：http://localhost:8080/images/abc123.jpg → abc123.jpg
        try {
            String fileName = avatarUrl.substring(avatarUrl.lastIndexOf('/') + 1);  // 获取文件名：abc123.jpg
            //从 images 目录下获取图片
            String localImagePath = SystemConstants.IMAGES_PATH + fileName;
            File localFile = new File(localImagePath);
    
            if (localFile.exists()) {
                //上传至 aliyun
                MultipartFile imageFile = new FileMultipartFileAdapter(localFile, fileName);
                String ossUrl = aliOssUtil.upload(imageFile, SystemConstants.OSS_BASE_PATH);
                    
                //删除本地图片
                if (localFile.delete()) {
                    log.info("本地临时图片已删除：{}", localImagePath);
                } else {
                    log.warn("本地临时图片删除失败：{}", localImagePath);
                }
                    
                //返回 OSS URL
                return ossUrl;
            } else {
                log.warn("本地图片文件不存在: {}", localImagePath);
                //返回默认头像
                return SystemConstants.DEFAULT_AVATAR;
            }
    
        } catch (Exception e) {
            log.warn("解析头像 URL 失败: {}", avatarUrl);
            //返回默认头像
            return SystemConstants.DEFAULT_AVATAR;
        }
    }

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return 登录响应（包含 Token、有效期和用户信息）
     */
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String captcha = loginDTO.getCaptcha();
            
        // 校验验证码
        if (StrUtils.isBlank(captcha)) {
            throw new BusinessException(ExceptionConstants.CAPTCHA_ERROR);
        }
        
        // 从 Redis 中获取验证码
        String savedCaptcha = stringRedisTemplate.opsForValue().get(RedisConstants.CAPTCHA_USERNAME + username);
        if (savedCaptcha == null) {
            throw new BusinessException(ExceptionConstants.CAPTCHA_ERROR);
        }
        
        // 比对验证码
        if (!captcha.equals(savedCaptcha)) {
            throw new BusinessException(ExceptionConstants.CAPTCHA_ERROR);
        }
        
        // 验证成功后删除验证码，防止重复使用
        stringRedisTemplate.delete(RedisConstants.CAPTCHA_USERNAME + username);
            
        // 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);
    
        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(ExceptionConstants.USER_NOT_FOUND_LOGIN);
        }
    
        // 验证密码
        if (!Md5Utils.matches(password, user.getPassword())) {
            throw new BusinessException(ExceptionConstants.PASSWORD_ERROR);
        }
    
        // 生成 Token
        Long userId = user.getId();
        String token = jwtUtils.createToken(userId, username);
        Long expiresIn = jwtUtils.getJwtProperties().getAccessExpirationTime() / 1000; // 转换为秒
        
        // 将 token 存入 Redis，设置过期时间（值为空字符串，只利用 key 的存在性）
        stringRedisTemplate.opsForValue().set(
            RedisConstants.USER_TOKEN + userId,
            "",
            jwtUtils.getJwtProperties().getAccessExpirationTime(),
            TimeUnit.MILLISECONDS
        );
        
        // 构建 UserVO
        UserVO userVO = UserVO.builder()
                .id(userId)
                .username(username)
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .build();
    
        // 返回登录响应
        return LoginVO.builder()
                .token(token)
                .expiresIn(expiresIn)
                .userVO(userVO)
                .build();
    }
    
    /**
     * 刷新 Token
     * @param oldToken 旧的 token
     * @return 新的 token 和有效期
     */
    @Override
    public RefreshTokenVO refreshToken(String oldToken) {
        // 验证旧 token 是否有效（签名验证）
        if (!jwtUtils.isTokenValid(oldToken)) {
            throw new BusinessException(ExceptionConstants.TOKEN_INVALID);
        }
            
        // 从旧 token 中获取用户信息
        Long userId = jwtUtils.getUserIdFromToken(oldToken);
        String username = jwtUtils.getUsernameFromToken(oldToken);
            
        // 检查 Redis 中的 token 是否存在（判断是否已退出登录）
        String savedToken = stringRedisTemplate.opsForValue().get(RedisConstants.USER_TOKEN + userId);
        if (savedToken == null) {
            throw new BusinessException(ExceptionConstants.NOT_LOGIN);
        }
            
        // 根据用户 ID 查询用户，确认用户状态正常
        User user = this.getById(userId);
        if (user == null || user.getStatus() == null || user.getStatus() != SystemConstants.USER_STATUS_NORMAL) {
            throw new BusinessException(ExceptionConstants.USER_STATUS_ERROR);
        }
            
        // 生成新的 token（不更新 Redis，让新旧 token 并存一段时间）
        String newToken = jwtUtils.createToken(userId, username);
        Long expiresIn = jwtUtils.getJwtProperties().getAccessExpirationTime() / 1000; // 转换为秒
            
        log.info("Token 刷新成功 - userId: {}, username: {}", userId, username);
            
        // 返回新的 token 信息
        return RefreshTokenVO.builder()
                .token(newToken)
                .expiresIn(expiresIn)
                .build();
    }

    /**
     * 退出登录
     * @param authorization 请求头中的 Authorization 值
     */
    @Override
    public void logout(String authorization) {
        // 从 Authorization header 中提取 token
        String token = authorization.replace("Bearer ", "");
        
        // 从 token 中获取用户 ID
        Long userId = jwtUtils.getUserIdFromExpiredToken(token);
        
        // 删除 Redis 中的 token
        stringRedisTemplate.delete(RedisConstants.USER_TOKEN + userId);
        
        log.info("用户退出登录 - userId: {}", userId);
    }
    
    /**
     * 获取并发送验证码
     * @param username 用户名
     * @return 验证码
     */
    @Override
    public String getCaptcha(String username) {
        // 校验用户名
        if (StrUtils.isBlank(username)) {
            throw new BusinessException(ExceptionConstants.USERNAME_EMPTY);
        }
        
        // 生成 6 位数字验证码
        String captcha = String.valueOf(100000 + new java.util.Random().nextInt(900000));
        
        // 通过日志输出验证码（模拟发送）
        log.info("===========================================");
        log.info("验证码发送 - 用户名：{}", username);
        log.info("验证码：{}", captcha);
        log.info("===========================================");

        // 将验证码保存到 Redis 中，并设置过期时间（60 秒）
        stringRedisTemplate.opsForValue().set(
            RedisConstants.CAPTCHA_USERNAME + username, 
            captcha, 
            60, 
            TimeUnit.SECONDS
        );
        
        return captcha;
    }
}
