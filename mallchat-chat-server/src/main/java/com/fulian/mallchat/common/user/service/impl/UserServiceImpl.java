package com.fulian.mallchat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.mapper.UserMapper;
import com.fulian.mallchat.common.user.service.UserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author fulian
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-10-28 21:06:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public User getByOpenId(String openId) {
        return lambdaQuery().eq(User::getOpenId,openId).one();
    }

    @Override
    @Transactional
    public Long register(User user) {
        boolean save = this.save(user);
        // todo 用户注册的事件
        return save?user.getId():null;
    }
}




