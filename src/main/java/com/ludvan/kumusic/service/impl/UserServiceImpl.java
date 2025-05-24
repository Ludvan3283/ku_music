package com.ludvan.kumusic.service.impl;

import com.ludvan.kumusic.entity.User;
import com.ludvan.kumusic.mapper.UserMapper;
import com.ludvan.kumusic.service.UserService;
import com.ludvan.kumusic.util.JwtUtil;
import com.ludvan.kumusic.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void registerUser(User user) {
        try {
            // 检查电话号码是否已存在
            int count = userMapper.countByPhone(user.getPhone());
            if (count > 0) {
                throw new IllegalStateException("Phone number already exists");
            }

            // 对密码进行SHA-256加密
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);

            // 设置avatarUrl为默认值，如果未设置
            if (user.getAvatar_url() == null) {
                user.setAvatar_url("default");
            }

            // 将用户信息插入数据库
            userMapper.insertUser(user);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);

        }
    }

    @Override
    public ResponseEntity<?> login(String phone, String password) {
        User user = userMapper.findByPhone(phone);

        if (user != null) {
            System.out.println("User found: " + user);
            System.out.println("Stored password: " + user.getPassword());

            try {
                // 使用 SHA-256 对输入的密码进行加密
                String hashedPassword = PasswordUtil.hashPassword(password);

                System.out.println("Hashed password: " + hashedPassword);

                //(String phoneNumber, String nickname, String id, String avatarUrl)
                String nickname = user.getNickname();

                Integer id = user.getId();

                String avatarUrl = user.getAvatar_url();

                // 比较加密后的密码与存储的密码
                if (user.getPassword() != null && user.getPassword().equals(hashedPassword)) {
                    String token = JwtUtil.generateToken(phone,nickname,id,avatarUrl);
                    System.out.println("Generated token: " + token);
                    return ResponseEntity.ok(new JwtUtil.JwtResponse(token));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("手机号或密码错误");
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("手机号或密码错误");
        }
    }
}