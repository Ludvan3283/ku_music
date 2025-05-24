package com.ludvan.kumusic.mapper;

import com.ludvan.kumusic.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (password, nickname, phone, avatar_url) VALUES (#{password}, #{nickname}, #{phone}, #{avatar_url})")
    void insertUser(User user);

    //注册时会检查该号码是否被注册
    @Select("SELECT COUNT(phone) FROM users WHERE phone = #{phone}")
    int countByPhone(String phone);

    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User findByPhone(String phone);
}