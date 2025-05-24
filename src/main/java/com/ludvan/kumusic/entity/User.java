package com.ludvan.kumusic.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String nickname;
    private String phone;
    private String password;
    private String avatar_url = "default_avatar";
}