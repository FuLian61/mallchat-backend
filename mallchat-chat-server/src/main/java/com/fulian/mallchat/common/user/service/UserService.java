package com.fulian.mallchat.common.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.fulian.mallchat.common.user.domain.vo.resp.UserInfoResp;

import java.util.List;

/**
* @author fulian
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-10-28 21:06:28
*/
public interface UserService extends IService<User> {

    User getByOpenId(String openId);

    Long register(User user);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    User getByName(String name);

    List<BadgeResp> badges(Long uid);

    void wearingBadge(Long uid,Long itemId);
}
