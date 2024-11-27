package com.fulian.mallchat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fulian.mallchat.common.user.domain.entity.UserBackpack;

/**
* @author fulian
* @description 针对表【user_backpack(用户背包表)】的数据库操作Service
* @createDate 2024-11-24 21:20:42
*/
public interface UserBackpackService extends IService<UserBackpack> {

    Integer getCountByValidItemId(Long uid, Long itemId);
}
