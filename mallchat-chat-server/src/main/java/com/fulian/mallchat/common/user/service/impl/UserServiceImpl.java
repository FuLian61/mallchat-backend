package com.fulian.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fulian.mallchat.common.common.exception.BusinessException;
import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.domain.enums.ItemEnum;
import com.fulian.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.fulian.mallchat.common.user.mapper.UserMapper;
import com.fulian.mallchat.common.user.service.UserBackpackService;
import com.fulian.mallchat.common.user.service.UserService;

import com.fulian.mallchat.common.user.service.adapter.UserAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
* @author fulian
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-10-28 21:06:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Autowired
    private UserBackpackService UserBackpackService;

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

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = this.getById(uid);
        Integer modifyNameCount = UserBackpackService.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user,modifyNameCount);
    }

    @Override
    public void modifyName(Long uid, String name) {
        User oldUser = getByName(name);
//        if (Objects.nonNull(oldUser)) {
            throw new BusinessException("名字重复换个名字吧");
//        }
    }

    @Override
    public User getByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();
    }


}




