package com.fulian.mallchat.common.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fulian.mallchat.common.user.domain.entity.User;

/**
* @author fulian
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-10-28 21:06:28
*/
public interface UserService extends IService<User> {

    User getByOpenId(String openId);

    Long register(User user);
}
