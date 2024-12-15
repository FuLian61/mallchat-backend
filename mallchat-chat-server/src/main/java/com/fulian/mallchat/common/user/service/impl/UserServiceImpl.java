package com.fulian.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fulian.mallchat.common.common.annotation.RedissonLock;
import com.fulian.mallchat.common.common.exception.BusinessException;
import com.fulian.mallchat.common.common.utils.AssertUtil;
import com.fulian.mallchat.common.user.domain.entity.ItemConfig;
import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.domain.entity.UserBackpack;
import com.fulian.mallchat.common.user.domain.enums.ItemEnum;
import com.fulian.mallchat.common.user.domain.enums.ItemTypeEnum;
import com.fulian.mallchat.common.user.domain.vo.resp.BadgeResp;
import com.fulian.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.fulian.mallchat.common.user.mapper.UserMapper;
import com.fulian.mallchat.common.user.service.ItemConfigService;
import com.fulian.mallchat.common.user.service.UserBackpackService;
import com.fulian.mallchat.common.user.service.UserService;

import com.fulian.mallchat.common.user.service.adapter.UserAdapter;
import com.fulian.mallchat.common.user.service.cache.ItemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private ItemCache ItemCache;

    @Autowired
    private ItemConfigService ItemConfigService;

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
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "uid")
    public void modifyName(Long uid, String name) {
        User oldUser = getByName(name);
        AssertUtil.isEmpty(oldUser,"名字已经被抢占了，请换一个～");

        UserBackpack modifyNameItem = UserBackpackService.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem,"改名卡不够了，等后续活动送改名卡吧");

        // 使用改名卡
        boolean success = UserBackpackService.userItem(modifyNameItem);
        if (success) {
            // 改名
            this.lambdaUpdate().eq(User::getId,uid).set(User::getName,name).update();
        }
    }

    @Override
    public User getByName(String name) {
        return lambdaQuery().eq(User::getName, name).one();
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        // 查询所有徽章
        List<ItemConfig> itemConfigs = ItemCache.getByType(ItemTypeEnum.BADGE.getType());
        // 查询用户拥有徽章
        List<UserBackpack> backpacks = UserBackpackService.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).collect(Collectors.toList()));
        // 查询用户佩戴的徽章
        User user = this.getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs,backpacks,user);
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        // 确保有徽章
        UserBackpack firstValidItem = UserBackpackService.getFirstValidItem(uid, itemId);
        AssertUtil.isNotEmpty(firstValidItem,"您还没有这个徽章，快去获得吧");
        // 确保这个物品是徽章
        ItemConfig itemConfig = ItemConfigService.getById(firstValidItem.getItemId());
        AssertUtil.equal(itemConfig.getType(),ItemTypeEnum.BADGE.getType(),"只有徽章才能佩戴");
        // 佩戴
        lambdaUpdate()
                .eq(User::getId,uid)
                .set(User::getItemId,itemId)
                .update();
    }
}




