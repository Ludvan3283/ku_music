package com.ludvan.kumusic.service;

import com.ludvan.kumusic.entity.Forum;
import java.util.List;


public interface ForumService {
    List<Forum> getAllForums();

    void createPost(String title, String content, Integer authorId);
}
