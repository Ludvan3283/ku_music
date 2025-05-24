package com.ludvan.kumusic.controller;


import com.ludvan.kumusic.entity.User;
import com.ludvan.kumusic.service.UserService;
import com.ludvan.kumusic.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String phone, @RequestParam String password) {
        //log.debug("登录请求, 手机号: {}", phone);
        return userService.login(phone, password);
    }

    @PostMapping("/sendCode")
    public String sendCode(@RequestParam String phone) {
        String key = "sms:code:" + phone;
        if (redisTemplate.hasKey(key)) {
            return "Please wait 60 seconds before requesting another code.";
        }
        String code = generateCode();
        try {
            String response = smsService.sendSms(phone, code);
            if ("验证码发送成功".equals(response)) {
                redisTemplate.opsForValue().set(key, code, 60, TimeUnit.SECONDS);
                return "验证码发送成功";
            } else if ("发送验证码过于频繁，请稍后再试".equals(response)) {
                return "发送验证码过于频繁，请稍后再试.";
            } else {
                return response; // 返回其他错误信息
            }
        } catch (Exception e) {
            //log.error("验证码发送异常, 手机号: {}", phone, e);
            return "验证码发送失败";
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String phone, @RequestParam String code) {
        String key = "sms:code:" + phone;
        String storedCode = redisTemplate.opsForValue().get(key);

        System.out.println(key);
        System.out.println("storedCode: " + storedCode);

        if (storedCode != null && storedCode.equals(code)) {
            // 删除验证码
            redisTemplate.delete(key);
            return ResponseEntity.ok("验证成功.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证失败.");
        }
    }

    private String generateCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}