package com.ludvan.kumusic.controller;

import com.ludvan.kumusic.entity.Forum;
import com.ludvan.kumusic.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/fetchForumPosts")
    public List<Forum> getAllForums() {
        return forumService.getAllForums();
    }

    @PostMapping("/addPost")
    public void createPost(@RequestBody Forum forum) {
        forumService.createPost(forum.getTitle(), forum.getContent(), forum.getAuthorId());
    }
}
