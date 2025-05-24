package com.ludvan.kumusic.service.impl;

import com.ludvan.kumusic.entity.Forum;
import com.ludvan.kumusic.mapper.ForumMapper;
import com.ludvan.kumusic.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private ForumMapper forumMapper;

    @Override
    public List<Forum> getAllForums() {
        List<Forum> forums = forumMapper.getAllForumsWithAuthors();

        // 定义时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化每个论坛帖子的创建时间和更新时间
        for (Forum forum : forums) {
            if (forum.getCreatedAt() != null) {
                forum.setFormattedCreatedAt(forum.getCreatedAt().format(formatter));
            }
            if (forum.getUpdatedAt() != null) {
                forum.setFormattedUpdatedAt(forum.getUpdatedAt().format(formatter));
            }
        }

        System.out.println(forums);

        return forums;
    }

    @Override
    public void createPost(String title, String content, Integer authorId) {
        Forum forum = new Forum();
        forum.setTitle(title);
        forum.setContent(content);
        forum.setCreatedAt(LocalDateTime.now());
        forum.setAuthorId(authorId);
        forumMapper.insertForum(forum);
    }
}
