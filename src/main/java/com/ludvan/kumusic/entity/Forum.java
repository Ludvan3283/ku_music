package com.ludvan.kumusic.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Forum {
    private Integer id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer authorId;

    private User author;

    // 添加格式化后的时间属性
    private String formattedCreatedAt;
    private String formattedUpdatedAt;
}
