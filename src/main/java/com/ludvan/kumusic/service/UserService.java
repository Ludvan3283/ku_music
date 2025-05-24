package com.ludvan.kumusic.service;

import com.ludvan.kumusic.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    void registerUser(User user);

    ResponseEntity<?> login(String phone, String password);
}
