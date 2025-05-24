package com.ludvan.kumusic.mapper;

import com.ludvan.kumusic.entity.Forum;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ForumMapper {
    @Select("SELECT f.*, u.id as user_id, u.nickname, u.phone, u.avatar_url " +
            "FROM forum_posts f " +
            "LEFT JOIN users u ON f.author_id = u.id")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "title", property = "title"),
            @Result(column = "content", property = "content"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "author_id", property = "authorId"),
            @Result(column = "nickname", property = "author.nickname"),
            @Result(column = "phone", property = "author.phone"),
            @Result(column = "avatar_url", property = "author.avatar_url")
    })
    List<Forum> getAllForumsWithAuthors();

    @Insert("INSERT INTO forum_posts(title, content, created_at, author_id) VALUES(#{title}, #{content}, #{createdAt}, #{authorId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertForum(Forum forum);
}
