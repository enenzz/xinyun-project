package com.yunbian;

import com.yunbian.entity.User;
import com.yunbian.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void test() {
        User user = User.builder()
                .username("test")
                .password("123456")
                .phone("12345678901")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        userService.save(user);
    }
}
